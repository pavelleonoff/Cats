package com.redprism.cats.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery

@Dao
interface CatsDao {
    @Query("SELECT * FROM cats")
    fun getAllCats (): List<Cat>

    @Query("SELECT * FROM cats WHERE id == :catId")
    fun getCatById(catId : String) : Cat

    @Query("SELECT images FROM cats WHERE id == :catId")
    fun getCatImages(catId : String) : String

    @RawQuery(observedEntities = [Cat::class])
    fun getFilteredCats(query: SimpleSQLiteQuery):List<Cat>

    @Insert
    fun insertCats(cats: List<Cat>)

    @Query("UPDATE cats SET images = :images WHERE id =:id")
    fun updateCatImages(images: String, id: String)

    @Query("SELECT EXISTS(SELECT * FROM cats)")
    fun isExists(): Boolean

    @Query("DELETE FROM cats")
    fun delete()
}