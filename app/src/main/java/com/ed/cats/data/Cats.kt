package com.ed.cats.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ed.cats.utils.JSON

@Entity(tableName = "cats")
class Cats(
    @PrimaryKey
    val id : String,
    val name : String,
    val description : String,
    val life_span : String,
    val image : String,
    val temperament : String,
    val origin : String,
    val wikipedia_url : String,
    val adaptability : Int,
    val affection_level : Int,
    val child_friendly : Int,
    val grooming : Int,
    val health_issues : Int,
    val intelligence : Int,
    val shedding_level : Int,
    val social_needs : Int,
    val stranger_friendly : Int,
    val vocalisation : Int,
    val experimental : Int,
    val hairless : Int,
    val natural : Int,
    val rare : Int,
    val rex : Int,
    val suppressed_tail : Int,
    val short_legs : Int,
    val hypoallergenic : Int)