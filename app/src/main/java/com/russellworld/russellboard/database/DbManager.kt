package com.russellworld.russellboard.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val datebase = Firebase.database.getReference("Main")

    fun publishAdd(){
        datebase.setValue("Hola")
    }
}