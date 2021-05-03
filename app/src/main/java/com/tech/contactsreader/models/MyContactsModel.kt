package com.tech.contactsreader.models

data class MyContactsModel(var name: String){
    var numbers = ArrayList<MyContactsNumbersModel>()
    var parsedCountryCode = ""
    var parsedNumber = ""
}