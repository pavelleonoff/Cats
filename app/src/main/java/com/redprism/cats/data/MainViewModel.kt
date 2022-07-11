package com.redprism.cats.data

import android.app.Application
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
        //cats = catsDB.catsDao().getAllCats()
        cat = MutableLiveData<Cat>()
        images = MutableLiveData()
    }
    var filteredCats = MutableLiveData<List<Cat>>()
    companion object {
        private lateinit var catsDB: CatsDB
        private lateinit var cat: MutableLiveData<Cat>
        private lateinit var images: LiveData<String>
    }

    private val pref = application.getSharedPreferences("Filter", AppCompatActivity.MODE_PRIVATE)

    private fun isExist(): Boolean {
        return catsDB.catsDao().isExists()
    }

    private fun downloadCatsFromNetwork():List<Cat>{
        val networkRequest = Network()
        val json = JSON()
        val catsJSON: JSONArray? = networkRequest.getCatsJSONFromNetwork()
        val cats: List<Cat> = json.getCatsFromJSON(catsJSON)
        catsDB.catsDao().insertCats(cats)
        return cats
    }

    fun downloadCatImagesFromNetwork(
        id: String,
        image: String
    ): String {
        val networkRequest = Network()
        val json = JSON()
        val catsImagesJSON: JSONArray? = networkRequest.getCatsImagesJSONFromNetwork(id)
        val catImages: ArrayList<String> = json.getCatImagesFromJSON(catsImagesJSON)
        val res = Operation().cleanString(catImages, image)
            catsDB.catsDao().updateCatImages(res, id)
            return res
    }


    fun getCatById(catId: String): LiveData<Cat>? {
        try {
            viewModelScope.launch {
                cat.postValue(catsDB.catsDao().getCatById(catId))
            }
            return cat
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private suspend fun getAllCatsFromDb() {
            viewModelScope.launch {
                if (!isExist()){
                    val cats = CoroutineScope(Dispatchers.IO).async {
                        return@async downloadCatsFromNetwork()
                    }.await()
                    filteredCats.value = cats
                }
                filteredCats.value = catsDB.catsDao().getAllCats()
            }
    }

    private fun getFilteredCats(query: SimpleSQLiteQuery) {
            viewModelScope.launch {
                filteredCats.value = catsDB.catsDao().getFilteredCats(query)
            }
    }
    fun getActualCats():LiveData<List<Cat>>?{
        return this.filteredCats
    }
    suspend fun getActualCatsFromDB() {

        if (pref.getBoolean(Pref.filterOn, false)) {
            val res = Operation().queryBuilder(pref)
            pref.edit().putString(Pref.rawQuery, res).commit()
            return getFilteredCats(SimpleSQLiteQuery(res))
        } else {
            getAllCatsFromDb()
        }
    }
}

