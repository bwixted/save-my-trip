package com.openclassrooms.savemytripkt.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
class User(

    @PrimaryKey
    var id: Long = 0,

    var username: String = "",

    var urlPicture: String = ""

) {
    @Ignore
    constructor() : this(0) {}

}