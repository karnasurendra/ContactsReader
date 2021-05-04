package com.tech.contactsreader.models

data class MyContactsModel(var name: String){
    var numbers = ArrayList<MyContactsNumbersModel>()
    var contactThumbnail = ""
    var contactPhoto = ""
}