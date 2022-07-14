package com.redprism.cats.data

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.redprism.cats.utils.JSON
import com.redprism.cats.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    init {
        catsDB = CatsDB.getInstance(getApplication())
    }

    var filteredCats = MutableLiveData<List<Cat>>()
    var catImages = MutableLiveData("")
    var cat = MutableLiveData<Cat>()

    companion object {
        private lateinit var catsDB: CatsDB
    }

    private val filterPref = application.getSharedPreferences(
        Pref.prefFilterName, AppCompatActivity.MODE_PRIVATE)
    private val dBPref = application.getSharedPreferences(
        Pref.prefDBDownloadName, AppCompatActivity.MODE_PRIVATE)

    fun getActualCatsFromDB() {
        viewModelScope.launch {
            if (filterPref.getBoolean(Pref.filterOn, false)) {
                val res = Operation().queryBuilder(filterPref)
                getFilteredCats(SimpleSQLiteQuery(res))
            } else {
                getAllCatsFromDb()
            }
        }
    }

    fun getCatById(catId: String) {
        viewModelScope.launch {
            cat.value = catsDB.catsDao().getCatById(catId)
        }
    }

    fun getCatImages(id: String, image: String) {
        viewModelScope.launch {
            val cat = catsDB.catsDao().getCatImages(id)
            if (cat == "") {
                val res = CoroutineScope(Dispatchers.IO).async {
                    val catImagesFromNetwork = downloadCatImagesFromNetwork(id)
                    val res = Operation().cleanString(catImagesFromNetwork, image)
                    catsDB.catsDao().updateCatImages(res, id)
                    return@async res
                }.await()
                catImages.value = res
            } else {
                catImages.value = cat
            }
        }
    }

    private fun isExist(): Boolean {
        return catsDB.catsDao().isExists()
    }

    private fun downloadCatsFromNetwork(): List<Cat> {
        val networkRequest = Network()
        val json = JSON()
        val catsJSON: JSONArray? = networkRequest.getCatsJSONFromNetwork()
        if (catsJSON != null) {
           dBPref.edit().putBoolean(Pref.dataDownloads, true).apply()
        }
        val cats: List<Cat> = json.getCatsFromJSON(catsJSON)
        catsDB.catsDao().insertCats(cats)
        return cats
    }

    private fun downloadCatImagesFromNetwork(id: String): ArrayList<String> {
        val networkRequest = Network()
        val json = JSON()
        val catsImagesJSON: JSONArray? = networkRequest.getCatsImagesJSONFromNetwork(id)
        return json.getCatImagesFromJSON(catsImagesJSON)
    }

    private suspend fun getAllCatsFromDb() {
        if (!isExist()) {
            val cat = CoroutineScope(Dispatchers.IO).async {
                 return@async downloadCatsFromNetwork()
            }.await()
            filteredCats.value = cat
        } else {
            filteredCats.value = catsDB.catsDao().getAllCats()
        }

    }

    private fun getFilteredCats(query: SimpleSQLiteQuery) {
        filteredCats.value = catsDB.catsDao().getFilteredCats(query)
    }


}

