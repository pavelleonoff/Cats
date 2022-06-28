package com.redprism.cats.data

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.redprism.cats.utils.JSON
import com.redprism.cats.utils.Network
import org.json.JSONArray
import java.util.*

class DBQueries {
    companion object {
        internal const val rawQuery = "rawQuery"
        internal const val filterOn = "filterOn"
        internal val tags = listOf(
            "hairless",
            "natural",
            "rare",
            "rex",
            "suppressedTail",
            "shortLegs",
            "hypoallergenic"
        )
        internal val sortBy = listOf(
            "adaptability",
            "affectionLevel",
            "childFriendly",
            "grooming",
            "healthIssues",
            "intelligence",
            "sheddingLevel",
            "socialNeeds",
            "strangerFriendly",
            "vocalisation"
        )

    }

    internal fun cleanFilter(pref: SharedPreferences) {
        pref.edit().clear().commit()
    }

    internal fun downloadCatsFromNetwork(viewModel: MainViewModel) {
        val networkRequest = Network()
        val json = JSON()
        val catsJSON: JSONArray? = networkRequest.getCatsJSONFromNetwork()
        val cats: List<Cat> = json.getCatsFromJSON(catsJSON)
        viewModel.insertCatsToDb(cats)
    }

    internal fun downloadCatImagesFromNetwork(
        viewModel: MainViewModel,
        id: String,
        image: String?
    ): String {
        val networkRequest = Network()
        val json = JSON()
        val catsImagesJSON: JSONArray? = networkRequest.getCatsImagesJSONFromNetwork(id)
        var catImages: ArrayList<String> = json.getCatImagesFromJSON(catsImagesJSON)
        catImages =
            ArrayList(catImages.distinct().filter { i -> i.isNotEmpty() && i != image })
        val res = catImages.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
        viewModel.insertCatImagesToDb(res, id)
        return res
    }

    private fun getCatsFromDb(viewModel: MainViewModel): LiveData<List<Cat>>? {
        val cats = viewModel.getCatsFromDb()
        if (cats != null) {

            return cats
        }
        return null
    }


    private fun getFilteredCats(
        viewModel: MainViewModel,
        queryStr: String
    ): LiveData<List<Cat>>? {
        val query = SimpleSQLiteQuery(queryStr)
        val cats = viewModel.getFilteredCats(query)
        if (cats != null) {

            return cats
        }
        return null
    }

    private fun queryBuilder(pref: SharedPreferences): String {
        val str = StringBuilder()
        str.append("SELECT * FROM cats WHERE")
        pref.all.forEach { i ->
            if (i.key in tags && i.value == true) {
                str.append(" ")
                str.append(i.key.toString() + " = 1")
                str.append(" AND")
            }
            if (i.key in sortBy && i.value == true) {
                str.append(" ")
                str.append(i.key.toString() + " = 5")
                str.append(" AND")
            }
        }
        return str.dropLast(4).toString()
    }

    internal fun filterOn(pref: SharedPreferences): Boolean {
        return pref.getBoolean(filterOn, false)
    }

    internal fun setFilterStatus(pref: SharedPreferences): String {
        val str = StringBuilder()
        var str2 = String()
        var tags = false
        var sortBy = false
        pref.all.forEach { i ->
            if (i.key in DBQueries.tags && i.value == true) {
                if (!tags) {
                    str.append("Tags: ")
                    tags = true
                }
                str.append(i.key.toString().substring(0 until 1).uppercase(Locale.ROOT))
                str.append(i.key.toString().substring(1 until i.key.toString().length))
                str.append(", ")
            }
        }
        if (tags) {
            str2 = str.dropLast(2).toString()
            str.clear()
            str.append(str2)
        }
        pref.all.forEach { i ->
            if (i.key in DBQueries.sortBy && i.value == true) {
                if (!sortBy) {
                    if (tags) {
                        str.append(". Sort By: ")
                    } else {
                        str.append("Sort By: ")
                    }
                    sortBy = true
                }
                str.append(i.key.toString().substring(0 until 1).uppercase(Locale.ROOT))
                str.append(i.key.toString().substring(1 until i.key.toString().length))
                str.append(", ")
            }
        }
        if (sortBy) {
            str2 = str.dropLast(2).toString()
            str.clear()
            str.append(str2)
        }
        str.append(".")
        return str.toString()
    }

    internal fun getActualCatsFromDB(
        pref: SharedPreferences,
        viewModel: MainViewModel
    ): LiveData<List<Cat>>? {
        if (pref.getBoolean(filterOn, false)) {
            Log.i("filter", "on")
            val res = queryBuilder(pref)
            if (pref.getString(rawQuery, "").toString() != res) {
                Log.i("filter", "newQuery")
                pref.edit().putString(rawQuery, res).apply()

            }
            return getFilteredCats(viewModel, res)
        } else {
            Log.i("filter", "off")
            if (pref.getString(rawQuery, "").toString() != "noFilters") {
                pref.edit().putString(rawQuery, "NoFilters").apply()

            }
            return getCatsFromDb(viewModel)
        }
        return null
    }
}