package com.ed.cats.data

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.substring
import androidx.compose.ui.text.toUpperCase
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ed.cats.R
import com.ed.cats.utils.JSON
import com.ed.cats.utils.Network
import org.json.JSONArray
import java.util.*

class DBQueries {
    companion object {
        internal const val rawQuery = "rawQuery"
        internal const val filterOn = "filterOn"
        internal val tags = listOf<String>(
            "hairless",
            "natural",
            "rare",
            "rex",
            "suppressedTail",
            "shortLegs",
            "hypoallergenic")
        internal val sortBy = listOf<String>(
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
        internal fun cleanFilter(pref: SharedPreferences){
            pref.edit().clear().commit()
        }

        internal fun downLoadDataFromNetwork(viewModel: MainViewModel) {
            if (!viewModel.isExist()) {
                val networkRequest = Network()
                val json = JSON()
                val catsJSON: JSONArray? = networkRequest.getJSONFromNetwork()
                val cats: List<Cat> = json.getCatsFromJSON(catsJSON)
                viewModel.insertCatsToDb(cats)
            }
        }

        private fun getDataFromDb(viewModel: MainViewModel): LiveData<List<Cat>>? {
            val cats = viewModel.getCatsFromDb()
            if (cats != null) {

                return cats
            }
            return null
        }

        private fun getFilterdCats(viewModel: MainViewModel, queryStr: String): LiveData<List<Cat>>?{
            val query = SimpleSQLiteQuery(queryStr)
            val cats = viewModel.getFilteredCats(query)
            if (cats != null) {

                return cats
            }
            return null
        }
        private fun queryBulder(pref: SharedPreferences): String{
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

        internal fun filterOn (pref: SharedPreferences): Boolean{
            return pref.getBoolean(filterOn, false)
        }

        internal fun setFilterStatus(pref: SharedPreferences):String {
            var str = StringBuilder()
            var str2 = String()
            var tags = false
            var sortBy = false
            pref.all.forEach { i ->
                if (i.key in DBQueries.tags && i.value == true) {
                    if (!tags) {
                        str.append("Tags: ")
                        tags = true
                    }
                    str.append(i.key.toString().substring(0 until 1).toUpperCase())
                    str.append(i.key.toString().substring(1 until i.key.toString().length))
                    str.append(", ")
                }
            }
            if(tags){
            str2 = str.dropLast(2).toString()
            str.clear()
            str.append(str2)
                }
            pref.all.forEach { i ->
                if (i.key in DBQueries.sortBy && i.value == true) {
                    if (!sortBy) {
                        if(tags){str.append(". Sort By: ")}
                        else{str.append("Sort By: ")}
                        sortBy = true
                    }
                    str.append(i.key.toString().substring(0 until 1).toUpperCase())
                    str.append(i.key.toString().substring(1 until i.key.toString().length))
                    str.append(", ")
                }
            }
            if(sortBy) {
            str2 = str.dropLast(2).toString()
            str.clear()
            str.append(str2)
            }
            str.append(".")
            return str.toString()
            }

        internal fun getActualCatsFromDB(pref: SharedPreferences,viewModel: MainViewModel):LiveData<List<Cat>>?{
            if (pref.getBoolean(filterOn, false)) {
                val res = queryBulder(pref)
                if(pref.getString(rawQuery,"").toString() != res) {
                    pref.edit().putString(rawQuery, res).apply()
                    return getFilterdCats(viewModel,res)
                }
            }
            else{
                if(pref.getString(rawQuery,"").toString() != "noFilters")
                {
                    pref.edit().putString(rawQuery, "NoFilters").apply()
                    return getDataFromDb(viewModel)
                }
            }
            return null
        }
    }
}