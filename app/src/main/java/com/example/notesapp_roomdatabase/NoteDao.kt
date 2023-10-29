package com.example.notesapp_roomdatabase


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface NoteDao {

    ///insert
    @Insert
    fun insert(note:Datas)

    ///Update
    @Update
    fun update(note: Datas)

    ///Delete
    @Delete
    fun delete(note: Datas)

    ///Get data or Fetch DAta
    @Query("Select * from eTable  order by id desc")
    fun getAllData():List<Datas>

    @Query("delete from eTable ")
    fun deleteAllData()

    @Query("delete from eTable where id like :id1")
    fun deletebyId(id1:Int)
}