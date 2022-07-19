package com.redprism.catbreeds

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import com.redprism.catbreeds.utils.Pref

class FilterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var filterButtonOk: Button
    private lateinit var pref: SharedPreferences

    private lateinit var queryString: MutableMap<String, ToggleButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        pref = getSharedPreferences("Filter", MODE_PRIVATE)

        queryString = mutableMapOf()
        queryString[Pref.tags[0]] = findViewById(R.id.filterExperimental)
        queryString[Pref.tags[1]] = findViewById(R.id.filterHairless)
        queryString[Pref.tags[2]] = findViewById(R.id.filterNatural)
        queryString[Pref.tags[3]] = findViewById(R.id.filterRare)
        queryString[Pref.tags[4]] = findViewById(R.id.filterRex)
        queryString[Pref.tags[5]] = findViewById(R.id.filterSuppressed_tail)
        queryString[Pref.tags[6]] = findViewById(R.id.filterShort_legs)
        queryString[Pref.tags[7]] = findViewById(R.id.filterHypoallergenic)

        queryString[Pref.sortBy[0]] = findViewById(R.id.filterAdaptability)
        queryString[Pref.sortBy[1]] = findViewById(R.id.filterAffection_level)
        queryString[Pref.sortBy[2]] = findViewById(R.id.filterChild_friendly)
        queryString[Pref.sortBy[3]] = findViewById(R.id.filterGrooming)
        queryString[Pref.sortBy[4]] = findViewById(R.id.filterHealth_issues)
        queryString[Pref.sortBy[5]] = findViewById(R.id.filterIntelligence)
        queryString[Pref.sortBy[6]] = findViewById(R.id.filterShedding_level)
        queryString[Pref.sortBy[7]] = findViewById(R.id.filterSocial_needs)
        queryString[Pref.sortBy[8]] = findViewById(R.id.filterStranger_friendly)
        queryString[Pref.sortBy[9]] = findViewById(R.id.filterVocalisation)

        filterButtonOk = findViewById(R.id.filterButtonOk)
        filterButtonOk.setOnClickListener(this)

        buttonsSet()
    }

    private fun buttonsSet() {
        pref.all.forEach { i ->
            if (i.value == true) {
                queryString[i.key]?.isChecked = true
            }
        }
    }

    private fun buttonsCheck() {
        var filterOn = 0
        for (i in queryString) {
            if (i.value.isChecked) {
                pref.edit().putBoolean(i.key, true).apply();filterOn++
            } else {
                pref.edit().putBoolean(i.key, false).apply()
            }
        }
        if (filterOn > 0) {
            pref.edit().putBoolean(Pref.filterOn, true).apply()
        } else {
            pref.edit().putBoolean(Pref.filterOn, false).apply()
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.filterButtonOk -> {
                buttonsCheck()
                finish()
            }
        }
    }
}