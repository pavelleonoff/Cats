package com.redprism.cats.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cats")
class Cat(
    @PrimaryKey
    val id : String,
    val name : String,
    val description : String,
    val lifeSpan : String,
    val image : String,
    val temperament : String,
    val origin : String,
    val wikipediaUrl : String,
    val adaptability : Int,
    val affectionLevel : Int,
    val childFriendly : Int,
    val grooming : Int,
    val healthIssues : Int,
    val intelligence : Int,
    val sheddingLevel : Int,
    val socialNeeds : Int,
    val strangerFriendly : Int,
    val vocalisation : Int,
    val experimental : Int,
    val hairless : Int,
    val natural : Int,
    val rare : Int,
    val rex : Int,
    val suppressedTail : Int,
    val shortLegs : Int,
    val hypoallergenic : Int,
    val images : String)