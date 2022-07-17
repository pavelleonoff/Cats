package com.redprism.catbreeds.utils

import org.junit.Before
import org.junit.Test


internal class NetworkTest{
    lateinit var networkRequest : Network

    @Before
    fun initialize() {
        networkRequest = Network()
    }
    @Test
    fun getCatsFromNetworkTest() {
        val dataFromNetwork = networkRequest.getCatsJSONFromNetwork()
        if (dataFromNetwork != null) {
            assert(dataFromNetwork.length()>0)
        }
    }
}