package com.ed.cats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CatsDao {
    @Query("SELECT * FROM cats")
    fun getAllCats (): LiveData<List<Cats>>

    @Query("SELECT * FROM cats WHERE id == :catId")
    fun getCatById(catId : String) : Cats

    @Insert
    fun insertCats(cats: List<Cats>)

    @Query("SELECT EXISTS(SELECT * FROM cats)")
    fun isExists(): Boolean

    @Query("DELETE FROM cats")
    fun delete():Unit
}