package com.ed.cats.utils

import org.junit.Before
import org.junit.Test

internal class JSONTest {
    lateinit var networkRequest : Network
    lateinit var json : JSON

    @Before
    fun initialize() {
        json = JSON()
        networkRequest = Network()
    }

    @Test
    fun getCatsFromJson(){
        val dataFromNetwork = networkRequest.getCatsJSONFromNetwork()
        val jsonArr = json.getCatsFromJSON(dataFromNetwork)
        assert(jsonArr.isNotEmpty())
        for(i in 0 until jsonArr.size){
            assert(jsonArr[i].id.isNotEmpty())
            assert(jsonArr[i].name.isNotEmpty())
            assert(jsonArr[i].description.isNotEmpty())
            assert(jsonArr[i].lifeSpan.isNotEmpty())
            assert(jsonArr[i].image.isNotEmpty())
            assert(jsonArr[i].temperament.isNotEmpty())
            assert(jsonArr[i].origin.isNotEmpty())
            assert(jsonArr[i].wikipediaUrl.isNotEmpty())
            assert(jsonArr[i].adaptability in 0..5)
            assert(jsonArr[i].affectionLevel in 0..5)
            assert(jsonArr[i].childFriendly in 0..5)
            assert(jsonArr[i].grooming in 0..5)
            assert(jsonArr[i].healthIssues in 0..5)
            assert(jsonArr[i].strangerFriendly in 0..5)
            assert(jsonArr[i].socialNeeds in 0..5)
            assert(jsonArr[i].vocalisation in 0..5)
            assert(jsonArr[i].experimental in 0..1)
            assert(jsonArr[i].hairless in 0..1)
            assert(jsonArr[i].natural in 0..1)
            assert(jsonArr[i].rare in 0..1)
            assert(jsonArr[i].suppressedTail in 0..1)
            assert(jsonArr[i].shortLegs in 0..1)
            assert(jsonArr[i].hypoallergenic in 0..1)
        }
    }
}