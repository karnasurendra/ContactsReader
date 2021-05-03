package com.tech.contactsreader.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.lifecycle.ViewModelProvider
import com.tech.contactsreader.R
import com.tech.contactsreader.viewmodels.MainScreenViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var mainScreenViewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainScreenViewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)

        telephonyManager =
            getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        setAdapters()

    }

    override fun onStart() {
        super.onStart()
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.getDefault())
        mainScreenViewModel.initReadContacts(contentResolver,countryCode)
    }

    private fun setAdapters() {

    }
}