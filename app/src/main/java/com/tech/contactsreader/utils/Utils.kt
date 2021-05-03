package com.tech.contactsreader.utils

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.tech.contactsreader.models.MyContactsModel
import com.tech.contactsreader.models.MyContactsNumbersModel
import java.util.HashSet
import kotlin.reflect.KFunction0

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

                        // Parsing the Mobile Number which is not required for now
                        try {
                            val parsedNumber =
                                phoneNumberUtil.parse(phoneNumber, countryCode)
                            val checkingContact = MyContactsModel(name)
                            // Checking the List if it is already added. If it is continuing the Loop
                            if (mContactsList.contains(checkingContact)) {
                                val oldPos = mContactsList.indexOf(checkingContact)
                                val numberForOldPos = MyContactsNumbersModel(
                                    parsedNumber.nationalNumber.toString(),
                                    parsedNumber.countryCode.toString()
                                )
                                mContactsList[oldPos].numbers.add(numberForOldPos)
                            }
                            val syncContacts = MyContactsModel("")
                            syncContacts.name = name
                            val number = MyContactsNumbersModel(
                                parsedNumber.nationalNumber.toString(),
                                parsedNumber.countryCode.toString()
                            )
                            syncContacts.numbers.add(number)
                            syncContacts.parsedCountryCode = parsedNumber.countryCode.toString()
                            syncContacts.parsedNumber = parsedNumber.nationalNumber.toString()
                            mContactsList.add(syncContacts)
                        } catch (e: Exception) {
                            // Number Format exception might trigger
                        }

                    } else {
                        // Number already added
                    }

                }
        } finally {
            cursor?.close()
            Handler(Looper.getMainLooper()).post {
                contactsUpdate(mContactsList)
            }
        }
    }

}