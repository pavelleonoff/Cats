package com.ed.cats.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    init {
        catsDB = CatsDB.getInstance(getApplication())
        cats = catsDB.catsDao().getAllCats()
        cat = MutableLiveData<Cat>()
        images = MutableLiveData()
    }

    companion object{

        private lateinit var catsDB : CatsDB
        private lateinit var cats : LiveData<List<Cat>>
        private lateinit var cat : MutableLiveData<Cat>
        private lateinit var images : LiveData<String>


    }
    fun isImagesExist(id:String):Boolean{
        return catsDB.catsDao().isImagesExist(id)
    }
    fun isExist():Boolean{
        return catsDB.catsDao().isExists()
    }

    fun getCatById(catId:String):LiveData<Cat>? {
        try {
            viewModelScope.launch {
                cat.postValue(catsDB.catsDao().getCatById(catId))
            }
            return cat
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }
    fun getCatImages(catId:String):LiveData<String>? {
        try {
            viewModelScope.launch {
                images = catsDB.catsDao().getCatImages(catId)
            }
            return images
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }

    fun insertCatsToDb(cats: List<Cat>) {
        try {
            viewModelScope.launch {
                catsDB.catsDao().insertCats(cats)
            }
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
    }
    fun insertCatImagesToDb(catImages: String,id:String) {
        try {
            viewModelScope.launch {
                catsDB.catsDao().updateCatImages(catImages,id)
            }
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
    }
    fun getCatsFromDb():LiveData<List<Cat>>? {
        try {
            viewModelScope.launch {
                cats = catsDB.catsDao().getAllCats()
            }
            return cats
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }
    fun getFilteredCats(query:SimpleSQLiteQuery):LiveData<List<Cat>>?{
        try {
            viewModelScope.launch {
                cats = catsDB.catsDao().getFilteredCats(query)
            }
            return cats
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }
}

