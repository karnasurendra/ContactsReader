package com.tech.contactsreader.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.contactsreader.adapters.MyContactsAdapter
import com.tech.contactsreader.models.MyContactsModel
import com.tech.contactsreader.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    lateinit var myContactsAdapter: MyContactsAdapter
    var mContactsList = ArrayList<MyContactsModel>()
    lateinit var uiConnector: UiConnector

    fun updateUiConnector(uiConnector: UiConnector){
        this.uiConnector = uiConnector
    }

    fun initReadContacts(
        contentResolver: ContentResolver,
        countryCode: String
    ) {
        uiConnector.updatedContacts(true)
        viewModelScope.launch(Dispatchers.IO) {
            Utils.readContacts(contentResolver, countryCode, ::updateContacts)
        }
    }

    private fun updateContacts(mList: ArrayList<MyContactsModel>) {
        if (mList.size > 0) {
            mContactsList.clear()
            mContactsList.addAll(mList)
            myContactsAdapter.notifyDataSetChanged()
        }
        uiConnector.updateContacts(mList.size)
        uiConnector.updatedContacts(false)
    }

    interface UiConnector{
        fun updatedContacts(isShow:Boolean)
        fun updateContacts(size:Int)
    }

}