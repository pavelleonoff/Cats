package com.redprism.cats

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import com.redprism.cats.data.Pref


class FilterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var filterHairless : ToggleButton
    private lateinit var filterNatural : ToggleButton
    private lateinit var filterRare : ToggleButton
    private lateinit var filterRex : ToggleButton
    private lateinit var filterSuppressed_tail : ToggleButton
    private lateinit var filterShort_legs : ToggleButton
    private lateinit var filterHypoallergenic : ToggleButton

    private lateinit var filterAdaptability : ToggleButton
    private lateinit var filterAffection_level : ToggleButton
    private lateinit var filterChild_friendly : ToggleButton
    private lateinit var filterGrooming : ToggleButton
    private lateinit var filterHealth_issues : ToggleButton
    private lateinit var filterIntelligence : ToggleButton
    private lateinit var filterShedding_level : ToggleButton
    private lateinit var filterSocial_needs : ToggleButton
    private lateinit var filterStranger_friendly : ToggleButton
    private lateinit var filterVocalisation : ToggleButton
    private lateinit var filterButtonOk : Button
    private lateinit var pref : SharedPreferences

    private lateinit var queryString : MutableMap<String,ToggleButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        pref = getSharedPreferences("Filter",MODE_PRIVATE)



        filterHairless = findViewById(R.id.filterHairless)
        queryString = mutableMapOf(Pref.tags[0] to filterHairless)
        filterNatural = findViewById(R.id.filterNatural)
        queryString[Pref.tags[1]] = filterNatural
        filterRare = findViewById(R.id.filterRare)
        queryString[Pref.tags[2]] = filterRare
        filterRex = findViewById(R.id.filterRex)
        queryString[Pref.tags[3]] = filterRex
        filterSuppressed_tail = findViewById(R.id.filterSuppressed_tail)
        queryString[Pref.tags[4]] = filterSuppressed_tail
        filterShort_legs = findViewById(R.id.filterShort_legs)
        queryString[Pref.tags[5]] = filterShort_legs
        filterHypoallergenic = findViewById(R.id.filterHypoallergenic)
        queryString[Pref.tags[6]] = filterHypoallergenic

        filterAdaptability = findViewById(R.id.filterAdaptability)
        queryString[Pref.sortBy[0]] = filterAdaptability
        filterAffection_level = findViewById(R.id.filterAffection_level)
        queryString[Pref.sortBy[1]] = filterAffection_level
        filterChild_friendly = findViewById(R.id.filterChild_friendly)
        queryString[Pref.sortBy[2]] = filterChild_friendly
        filterGrooming = findViewById(R.id.filterGrooming)
        queryString[Pref.sortBy[3]] = filterGrooming
        filterHealth_issues = findViewById(R.id.filterHealth_issues)
        queryString[Pref.sortBy[4]] = filterHealth_issues
        filterIntelligence = findViewById(R.id.filterIntelligence)
        queryString[Pref.sortBy[5]] = filterIntelligence
        filterShedding_level = findViewById(R.id.filterShedding_level)
        queryString[Pref.sortBy[6]] = filterShedding_level
        filterSocial_needs = findViewById(R.id.filterSocial_needs)
        queryString[Pref.sortBy[7]] = filterSocial_needs
        filterStranger_friendly = findViewById(R.id.filterStranger_friendly)
        queryString[Pref.sortBy[8]] = filterStranger_friendly
        filterVocalisation = findViewById(R.id.filterVocalisation)
        queryString[Pref.sortBy[9]] = filterVocalisation

        filterButtonOk = findViewById(R.id.filterButtonOk)
        filterButtonOk.setOnClickListener(this)

        buttonsSet()
    }

    private fun buttonsSet(){
        pref.all.forEach { i ->
            if(i.value==true){
                queryString[i.key]?.isChecked = true
            }
        }
    }

    private fun buttonsCheck(){
        var filterOn = 0
        for(i in queryString){
            if(i.value.isChecked){pref.edit().putBoolean(i.key,true).apply();filterOn++}
            else{pref.edit().putBoolean(i.key,false).apply()}
        }
        if(filterOn>0){pref.edit().putBoolean(Pref.filterOn,true).apply()}
        else{pref.edit().putBoolean(Pref.filterOn,false).apply()}
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.filterButtonOk ->{
                buttonsCheck()
                finish()
            }
        }
    }
}