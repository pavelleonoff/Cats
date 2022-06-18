package com.ed.cats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CatsDao {
    @Query("SELECT * FROM cats")
    fun getAllCats (): LiveData<List<Cat>>

    @Query("SELECT * FROM cats WHERE id == :catId")
    fun getCatById(catId : String) : Cat

    @RawQuery(observedEntities = [Cat::class])
    fun getFilteredCats(query: SimpleSQLiteQuery):LiveData<List<Cat>>

    @Insert
    fun insertCats(cats: List<Cat>)

    @Query("SELECT EXISTS(SELECT * FROM cats)")
    fun isExists(): Boolean

    @Query("DELETE FROM cats")
    fun delete()
}