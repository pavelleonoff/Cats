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
    }
    var filteredCats = MutableLiveData<List<Cat>>()
    var catImages = MutableLiveData<String>("")
    var cat = MutableLiveData<Cat>()
    companion object {
        private lateinit var catsDB: CatsDB
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

    private fun downloadCatImagesFromNetwork(
        id: String,
    ): ArrayList<String> {
        val networkRequest = Network()
        val json = JSON()
        val catsImagesJSON: JSONArray? = networkRequest.getCatsImagesJSONFromNetwork(id)
        return json.getCatImagesFromJSON(catsImagesJSON)
    }

    fun getCatImages(id: String,image:String){
        viewModelScope.launch {
            val cat = catsDB.catsDao().getCatImages(id)
            var res = cat
            if(cat.equals("")){
                val res = CoroutineScope(Dispatchers.IO).async {
                    val catImagesFromNetwork = downloadCatImagesFromNetwork(id)
                    res = Operation().cleanString(catImagesFromNetwork, image)
                    catsDB.catsDao().updateCatImages(res, id)
                    return@async res
                }.await()
                catImages.value = res
            }
            else{catImages.value = res}
        }
    }

    fun getCatById(catId: String) {
        viewModelScope.launch {
            cat.value = catsDB.catsDao().getCatById(catId)
        }
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

