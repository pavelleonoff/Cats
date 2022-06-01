package com.ed.cats


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod

import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

import androidx.lifecycle.ViewModelProvider
import com.ed.cats.data.Cat

import com.ed.cats.data.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel

    lateinit var id : String
    lateinit var name:TextView
    lateinit var description : TextView
    lateinit var life_span : TextView
    lateinit var image : ImageView
    lateinit var temperament : TextView
    lateinit var origin : TextView
    lateinit var wikipedia_url : TextView
    lateinit var adaptability : RatingBar
    lateinit var affection_level : RatingBar
    lateinit var child_friendly : RatingBar
    lateinit var grooming : RatingBar
    lateinit var health_issues : RatingBar
    lateinit var intelligence : RatingBar
    lateinit var shedding_level : RatingBar
    lateinit var social_needs : RatingBar
    lateinit var stranger_friendly : RatingBar
    lateinit var vocalisation : RatingBar
    lateinit var experimental : TextView
    lateinit var hairless : TextView
    lateinit var natural : TextView
    lateinit var rare : TextView
    lateinit var rex : TextView
    lateinit var suppressed_tail : TextView
    lateinit var short_legs : TextView
    lateinit var hypoallergenic : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        image = findViewById(R.id.image_catId)
        name = findViewById(R.id.name)
        description = findViewById(R.id.description)
        life_span = findViewById(R.id.life_span)
        temperament = findViewById(R.id.temperament)
        origin = findViewById(R.id.origin)
        wikipedia_url = findViewById(R.id.wikipedia_url)
        adaptability = findViewById(R.id.adaptability_rb)
        affection_level = findViewById(R.id.affection_level_rb)
        child_friendly = findViewById(R.id.child_friendly_rb)
        grooming = findViewById(R.id.grooming_rb)
        health_issues = findViewById(R.id.health_issues_rb)
        intelligence = findViewById(R.id.intelligence_rb)
        shedding_level = findViewById(R.id.shedding_level_rb)
        social_needs = findViewById(R.id.social_needs_rb)
        stranger_friendly = findViewById(R.id.stranger_friendly_rb)
        vocalisation = findViewById(R.id.vocalisation_rb)
        experimental = findViewById(R.id.experimental)
        hairless = findViewById(R.id.hairless)
        natural = findViewById(R.id.natural)
        rare = findViewById(R.id.rare)
        rex = findViewById(R.id.rex)
        suppressed_tail = findViewById(R.id.suppressed_tail)
        short_legs = findViewById(R.id.short_legs)
        hypoallergenic = findViewById(R.id.hypoallergenic)
        id = getIntent().getStringExtra("id").toString()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val cat = viewModel.getCatById(id)
        if (cat != null) {
            cat.observe(this) { t -> putDataToFields(t as Cat) }
        }

    }
    fun putDataToFields(cat: Cat?){
        if(cat!=null){
            Picasso.get().load(cat.image).resize(MainActivity.screenWidth,0).placeholder(R.drawable.placeholder).into(image);
            name.setText(cat.name)
            description.setText(cat.description)
            life_span.setText(life_span.text.toString() + cat.life_span)
            temperament.setText(temperament.text.toString() + cat.temperament)
            origin.setText(origin.text.toString() + cat.origin)
            adaptability.rating = cat.adaptability.toFloat()
            affection_level.rating = cat.affection_level.toFloat()
            child_friendly.rating = cat.child_friendly.toFloat()
            grooming.rating = cat.grooming.toFloat()
            health_issues.rating = cat.health_issues.toFloat()
            intelligence.rating = cat.intelligence.toFloat()
            shedding_level.rating = cat.shedding_level.toFloat()
            social_needs.rating = cat.social_needs.toFloat()
            stranger_friendly.rating = cat.stranger_friendly.toFloat()
            vocalisation.rating = cat.vocalisation.toFloat()
            if(cat.experimental==1) experimental.visibility = View.VISIBLE
            if(cat.hairless==1) hairless.visibility = View.VISIBLE
            if(cat.natural==1) natural.visibility = View.VISIBLE
            if(cat.rare==1) rare.visibility = View.VISIBLE
            if(cat.rex==1) rex.visibility = View.VISIBLE
            if(cat.suppressed_tail==1) suppressed_tail.visibility = View.VISIBLE
            if(cat.short_legs==1) short_legs.visibility = View.VISIBLE
            if(cat.hypoallergenic==1) hypoallergenic.visibility = View.VISIBLE
            wikipedia_url.setText(cat.wikipedia_url)
            wikipedia_url.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}