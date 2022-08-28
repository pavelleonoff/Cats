package com.redprism.catbreeds.utils

import android.content.SharedPreferences
import kotlin.collections.ArrayList

class Filtering {

    fun queryBuilder(pref: SharedPreferences): String {
        val str = StringBuilder()
        str.append("SELECT * FROM cats WHERE")
        pref.all.forEach { i ->
            if (i.key in Pref.tags && i.value == true) {
                str.append(" ")
                str.append(i.key.toString() + " = 1")
                str.append(" AND")
            }
            if (i.key in Pref.sortBy && i.value == true) {
                str.append(" ")
                str.append(i.key.toString() + " = 5")
                str.append(" AND")
            }
        }
        return str.dropLast(4).toString()
    }

    internal fun setFilterStatus(pref: SharedPreferences, filter: Array<String>): String {
        val str = StringBuilder()
        var str2: String
        var tags = false
        var sortBy = false
        val filterMap = mutableMapOf<String, String>()
        for (i in filter.indices) {
            if(i< Pref.tags.size) {
                filterMap[Pref.tags[i]] = filter[i]
            }
            else{
                filterMap[Pref.sortBy[i- Pref.tags.size]] = filter[i]
            }
        }
        pref.all.forEach { i ->
            if (i.key in Pref.tags && i.value == true) {
                if (!tags) {
                    str.append("Tags: ")
                    tags = true
                }
                str.append(filterMap[i.key])
                str.append(", ")
            }
        }
        if (tags) {
            str2 = str.dropLast(2).toString()
            str.clear()
            str.append(str2)
        }
        pref.all.forEach { i ->
            if (i.key in Pref.sortBy && i.value == true) {
                if (!sortBy) {
                    if (tags) {
                        str.append(". Sort By: ")
                    } else {
                        str.append("Sort By: ")
                    }
                    sortBy = true
                }
                str.append(filterMap[i.key])
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

    fun cleanString(catImages: ArrayList<String>, image: String): String {
        val res = ArrayList(catImages.distinct().filter { i -> i.isNotEmpty() && i != image })
        return res.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
    }
}