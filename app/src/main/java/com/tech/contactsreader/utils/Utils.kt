package com.tech.contactsreader.utils

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
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
            if (cursor != null && cursor.count > 0) {
                val normalizedNumberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
                val displayNameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val displayNumberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val thumbnailIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI)
                val photoIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
                while (cursor.moveToNext()) {
                    val normalizedNum =
                        cursor.getString(normalizedNumberIndex)
                    // duplicates won't add again
                    if (normalizedNumbersAlreadyFound.add(normalizedNum)) {
                        val name =
                            cursor.getString(displayNameIndex)
                        val phoneNumber =
                            cursor.getString(displayNumberIndex)
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
                        mContact.contactThumbnail =
                            cursor.getString(thumbnailIndex)
                                ?: ""
                        mContact.contactPhoto =
                            cursor.getString(photoIndex)
                                ?: ""
                        mContactsList.add(mContact)
                    } else {
                        // Number already added
                    }

                }
            }
        } catch (e: java.lang.Exception) {
            // Number format exception might trigger
        } finally {
            cursor?.close()
            Handler(Looper.getMainLooper()).post {
                contactsUpdate(mContactsList)
            }
        }
    }

    fun hasPermissions(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}