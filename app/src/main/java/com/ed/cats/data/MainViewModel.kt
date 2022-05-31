package com.ed.cats.data

import android.app.Application
import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


open class MainViewModel(application: Application) : AndroidViewModel(application) {

    init {
        catsDB = CatsDB.getInstance(getApplication())
        cats = catsDB.catsDao().getAllCats()
    }

    companion object{

        private lateinit var catsDB : CatsDB
        private lateinit var cats : LiveData<List<Cats>>

        private class DeleteDBTask : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                catsDB.catsDao().delete()
            }
        }
        private class GetCatByIdTask : AsyncTask<String,Unit,Cats>() {
            override fun doInBackground(vararg p0: String): Cats? {
                if (p0 != null && p0.size>0){
                    return catsDB.catsDao().getCatById(p0[0])
                }
                return null
            }
        }
        private class GetCatsFromDbTask : AsyncTask<Unit,Unit,LiveData<List<Cats>>>() {
            override fun doInBackground(vararg p0: Unit?): LiveData<List<Cats>>? {
                    return catsDB.catsDao().getAllCats()
            }

        }
        private class InsertCatsTask : AsyncTask<List<Cats>,Unit,Unit>() {
            override fun doInBackground(vararg p0: List<Cats>) {
                if (p0 != null && p0.size>0){
                    catsDB.catsDao().insertCats(p0[0])
                }
            }

        }
        private class isExistsTask : AsyncTask<Unit,Unit,Boolean>() {
            override fun doInBackground(vararg params: Unit?): Boolean {
                    return catsDB.catsDao().isExists()
            }


        }

    }
    fun isExist():Boolean{
        try {
            return isExistsTask().execute().get()
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return false
    }
    fun getCatById(catId:String): Cats? {
        try {
            return GetCatByIdTask().execute(catId).get()
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }
    fun insertCatsToDb(cats: List<Cats>) {
        try {
            InsertCatsTask().execute(cats).get()
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
    }
    fun getCatsFromDb():LiveData<List<Cats>>? {
        try {
            return GetCatsFromDbTask().execute().get()
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
        return null
    }
    fun deleteDB():Unit{
        try {
            DeleteDBTask().execute().get()
        }
        catch (e:InterruptedException){
            e.printStackTrace()
        }
    }
}

