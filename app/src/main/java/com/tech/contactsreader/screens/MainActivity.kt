package com.tech.contactsreader.screens

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.contactsreader.R
import com.tech.contactsreader.adapters.MyContactsAdapter
import com.tech.contactsreader.utils.Utils
import com.tech.contactsreader.viewmodels.MainScreenViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),MainScreenViewModel.UiConnector {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var mainScreenViewModel: MainScreenViewModel
    private val contactPermission = arrayOf(
        Manifest.permission.READ_CONTACTS
    )
    private val contactPermissionRequest = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainScreenViewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        mainScreenViewModel.updateUiConnector(this)

        telephonyManager =
            getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        setAdapters()

    }

    override fun onStart() {
        super.onStart()
        if (Utils.hasPermissions(this, contactPermission)) {
            readContacts()
        } else {
            ActivityCompat.requestPermissions(this, contactPermission, contactPermissionRequest)
        }

    }

    private fun readContacts() {
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.getDefault())
        mainScreenViewModel.initReadContacts(contentResolver, countryCode)
    }

    private fun setAdapters() {
        contacts_recycler_view.layoutManager = LinearLayoutManager(this)
        mainScreenViewModel.myContactsAdapter = MyContactsAdapter(mainScreenViewModel.mContactsList)
        contacts_recycler_view.adapter = mainScreenViewModel.myContactsAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == contactPermissionRequest) {
            if (Utils.hasPermissions(this, contactPermission)) {
                readContacts()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updatedContacts(isShow:Boolean) {
        if (isShow){
            contacts_loader.visibility = View.VISIBLE
        }else{
            contacts_loader.visibility = View.GONE
        }
    }
}