package com.example.notesapp_roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eTable")
data class Datas(

    @PrimaryKey(autoGenerate = true)
    var Id:Int,
    var title_content:String,
    var body_content:String
)