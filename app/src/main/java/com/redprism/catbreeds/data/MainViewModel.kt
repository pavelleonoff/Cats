package com.redprism.catbreeds.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.redprism.catbreeds.utils.JSON
import com.redprism.catbreeds.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray


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

    internal fun getActualCatsFromDB() {
        viewModelScope.launch {
            if (filterPref.getBoolean(Pref.filterOn, false)) {
                val res = Operation().queryBuilder(filterPref)
                getFilteredCats(SimpleSQLiteQuery(res))
            } else {
                getAllCatsFromDb()
            }
        }
    }

    internal fun getCatById(catId: String) {
        viewModelScope.launch {
            cat.value = catsDB.catsDao().getCatById(catId)
        }
    }

    internal fun getCatImages(id: String, image: String) {
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
    internal fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }

    private fun isExist(): Boolean {
        return catsDB.catsDao().isExists()
    }

    private fun downloadCatsFromNetwork(): List<Cat> {
        val networkRequest = Network()
        val json = JSON()
        val catsJSON: JSONArray? = networkRequest.getCatsJSONFromNetwork()
        if (catsJSON != null) {
           dBPref.edit().putBoolean(Pref.dataNotDownloads, false).apply()
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

