package com.tech.contactsreader.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tech.contactsreader.adapters.MyContactsAdapter
import com.tech.contactsreader.models.MyContactsModel
import com.tech.contactsreader.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    lateinit var myContactsAdapter: MyContactsAdapter
    var mContactsList = ArrayList<MyContactsModel>()

    fun initReadContacts(
        contentResolver: ContentResolver,
        countryCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Utils.readContacts(contentResolver, countryCode, ::updateContacts)
        }
    }

    private fun updateContacts(mList: ArrayList<MyContactsModel>) {
        Log.d("ContactsReader::", "Checking the Contacts Size ${mList.size}")
        if (mList.size > 0) {
            for (i in mList.indices) {
                Log.d("ContactsReader::", "Checking the Contacts ${Gson().toJson(mList[i])}")
            }
            mContactsList.clear()
            mContactsList.addAll(mList)
            myContactsAdapter.notifyDataSetChanged()
        }
    }

}