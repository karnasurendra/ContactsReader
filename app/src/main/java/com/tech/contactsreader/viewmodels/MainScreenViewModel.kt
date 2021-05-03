package com.tech.contactsreader.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.tech.contactsreader.models.MyContactsModel
import com.tech.contactsreader.utils.Utils

class MainScreenViewModel : ViewModel() {

    fun initReadContacts(
        contentResolver: ContentResolver,
        countryCode: String
    ) {
        Utils.readContacts(contentResolver, countryCode, ::updateContacts)
    }

    private fun updateContacts(mList: ArrayList<MyContactsModel>) {

    }

}