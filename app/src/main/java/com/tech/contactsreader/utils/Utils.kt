package com.tech.contactsreader.utils

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.tech.contactsreader.models.MyContactsModel
import com.tech.contactsreader.models.MyContactsNumbersModel
import java.util.HashSet

object Utils {

    fun readContacts(
        contentResolver: ContentResolver,
        countryCode: String,
        contactsUpdate: (mList: ArrayList<MyContactsModel>) -> Unit
    ) {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val mContactsList = ArrayList<MyContactsModel>()
        val order =
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            order
        )
        try {
            val normalizedNumbersAlreadyFound: HashSet<String> = HashSet()
            if (cursor != null && cursor.count > 0)
                while (cursor.moveToNext()) {
                    val normalizedNum =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))
                    if (normalizedNumbersAlreadyFound.add(normalizedNum)) {
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        val phoneNumber =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val parsedNumber =
                            phoneNumberUtil.parse(phoneNumber, countryCode)
                        val mContact = MyContactsModel(name)
                        val number = MyContactsNumbersModel(
                            parsedNumber.nationalNumber.toString(),
                            parsedNumber.countryCode.toString()
                        )
                        // Checking the contact is already added to List. If it is then continuing the Loop
                        if (mContactsList.contains(mContact)) {
                            val oldPos = mContactsList.indexOf(mContact)
                            mContactsList[oldPos].numbers.add(number)
                            continue
                        }
                        mContact.numbers.add(number)
                        mContact.contactThumbnail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI))
                            ?: ""
                        mContact.contactPhoto = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))
                            ?: ""
                        mContact.parsedCountryCode = parsedNumber.countryCode.toString()
                        mContact.parsedNumber = parsedNumber.nationalNumber.toString()
                        mContactsList.add(mContact)
                    } else {
                        // Number already added
                    }

                }
        } catch (e: java.lang.Exception) {
            Log.d("ContactsReader::", "Checking the Contacts Exception ${e.localizedMessage}")
        } finally {
            cursor?.close()
            Handler(Looper.getMainLooper()).post {
                contactsUpdate(mContactsList)
            }
        }
    }

}