package com.redprism.cats


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.redprism.cats.data.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates


class DetailActivity : AppCompatActivity(){
    private lateinit var viewModel : MainViewModel


    private lateinit var pref : SharedPreferences
    private lateinit var id : String
    private lateinit var name:TextView
    private lateinit var description : TextView
    private lateinit var lifeSpan : TextView
    private lateinit var image : ImageView
    private lateinit var temperament : TextView
    private lateinit var origin : TextView
    private lateinit var wikipediaUrl : TextView
    private lateinit var adaptability : RatingBar
    private lateinit var affectionLevel : RatingBar
    private lateinit var childFriendly : RatingBar
    private lateinit var grooming : RatingBar
    private lateinit var healthIssues : RatingBar
    private lateinit var intelligence : RatingBar
    private lateinit var sheddingLevel : RatingBar
    private lateinit var socialNeeds : RatingBar
    private lateinit var strangerFriendly : RatingBar
    private lateinit var vocalisation : RatingBar
    private lateinit var adaptabilityText : TextView
    private lateinit var affectionLevelText : TextView
    private lateinit var childFriendlyText : TextView
    private lateinit var groomingText : TextView
    private lateinit var healthIssuesText : TextView
    private lateinit var intelligenceText : TextView
    private lateinit var sheddingLevelText : TextView
    private lateinit var socialNeedsText : TextView
    private lateinit var strangerFriendlyText : TextView
    private lateinit var vocalisationText : TextView
    private lateinit var experimental : TextView
    private lateinit var hairless : TextView
    private lateinit var natural : TextView
    private lateinit var rare : TextView
    private lateinit var rex : TextView
    private lateinit var suppressedTail : TextView
    private lateinit var shortLegs : TextView
    private lateinit var hypoallergenic : TextView
    private lateinit var catImagesButton : Button
    private var heightPixels by Delegates.notNull<Int>()
    private var widthPixels by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        name = findViewById(R.id.name)
        description = findViewById(R.id.description)
        lifeSpan = findViewById(R.id.life_span)
        temperament = findViewById(R.id.temperament)
        origin = findViewById(R.id.origin)
        wikipediaUrl = findViewById(R.id.wikipedia_url)
        adaptability = findViewById(R.id.adaptability_rb)
        affectionLevel = findViewById(R.id.affection_level_rb)
        childFriendly = findViewById(R.id.child_friendly_rb)
        grooming = findViewById(R.id.grooming_rb)
        healthIssues = findViewById(R.id.health_issues_rb)
        intelligence = findViewById(R.id.intelligence_rb)
        sheddingLevel = findViewById(R.id.shedding_level_rb)
        socialNeeds = findViewById(R.id.social_needs_rb)
        strangerFriendly = findViewById(R.id.stranger_friendly_rb)
        vocalisation = findViewById(R.id.vocalisation_rb)
        adaptabilityText = findViewById(R.id.adaptability)
        affectionLevelText = findViewById(R.id.affection_level)
        childFriendlyText = findViewById(R.id.child_friendly)
        groomingText = findViewById(R.id.grooming)
        healthIssuesText = findViewById(R.id.health_issues)
        intelligenceText = findViewById(R.id.intelligence)
        sheddingLevelText = findViewById(R.id.shedding_level)
        socialNeedsText = findViewById(R.id.social_needs)
        strangerFriendlyText = findViewById(R.id.stranger_friendly)
        vocalisationText = findViewById(R.id.vocalisation)
        experimental = findViewById(R.id.experimental)
        hairless = findViewById(R.id.hairless)
        natural = findViewById(R.id.natural)
        rare = findViewById(R.id.rare)
        rex = findViewById(R.id.rex)
        suppressedTail = findViewById(R.id.suppressed_tail)
        shortLegs = findViewById(R.id.short_legs)
        hypoallergenic = findViewById(R.id.hypoallergenic)
        image = findViewById(R.id.detailImage)
        catImagesButton = findViewById(R.id.catImagesButton)
        id = intent.getStringExtra("id").toString()
        val data = intent
        val image = data.getStringExtra("image")
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        widthPixels = resources.displayMetrics.widthPixels
        heightPixels = resources.displayMetrics.heightPixels
        val cat = viewModel.cat
        viewModel.getCatById(id)
        cat.observe(this) { t -> putDataToFields(t as Cat)}
        catImagesButton.setOnClickListener {
            val intent = Intent(this, CatImagesActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("image",image.toString())
            startActivity(intent)
        }
    }


    private fun putDataToFields(cat: Cat?){
        pref = getSharedPreferences("Filter",MODE_PRIVATE)
        if(cat!=null){

            Picasso.get().load(cat.image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.infinitive_progressbar_big).into(image,object: Callback {
                    override fun onSuccess() {}
                    override fun onError(e: Exception?) {
                        Picasso.get().load(cat.image)
                            .resize(widthPixels,heightPixels)
                            .centerInside()
                            .placeholder(R.drawable.infinitive_progressbar_small).into(image)
                    }
                })

            var sortByColor=ContextCompat.getColor(this, R.color.secondColor)

            name.text = cat.name
            description.text = cat.description
            lifeSpan.text = getString(R.string.life_span) + cat.lifeSpan
            temperament.text = getString(R.string.temperament) + cat.temperament
            origin.text = getString(R.string.origin) + cat.origin
            if(pref.getBoolean(Pref.sortBy[0],false)){
                adaptabilityText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[1],false)){
                affectionLevelText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[2],false)){
                childFriendlyText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[3],false)){
                groomingText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[4],false)){
                healthIssuesText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[5],false)){
                intelligenceText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[6],false)){
                sheddingLevelText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[7],false)){
                socialNeedsText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[8],false)){
                strangerFriendlyText.setTextColor(sortByColor)
            }
            if(pref.getBoolean(Pref.sortBy[9],false)){
                vocalisationText.setTextColor(sortByColor)
            }
            adaptability.rating = cat.adaptability.toFloat()
            affectionLevel.rating = cat.affectionLevel.toFloat()
            childFriendly.rating = cat.childFriendly.toFloat()
            grooming.rating = cat.grooming.toFloat()
            healthIssues.rating = cat.healthIssues.toFloat()
            intelligence.rating = cat.intelligence.toFloat()
            sheddingLevel.rating = cat.sheddingLevel.toFloat()
            socialNeeds.rating = cat.socialNeeds.toFloat()
            strangerFriendly.rating = cat.strangerFriendly.toFloat()
            vocalisation.rating = cat.vocalisation.toFloat()

            if(cat.experimental==1) experimental.visibility = View.VISIBLE
            if(cat.hairless==1) hairless.visibility = View.VISIBLE
            if(cat.natural==1) natural.visibility = View.VISIBLE
            if(cat.rare==1) rare.visibility = View.VISIBLE
            if(cat.rex==1) rex.visibility = View.VISIBLE
            if(cat.suppressedTail==1) suppressedTail.visibility = View.VISIBLE
            if(cat.shortLegs==1) shortLegs.visibility = View.VISIBLE
            if(cat.hypoallergenic==1) hypoallergenic.visibility = View.VISIBLE
            wikipediaUrl.text = cat.wikipediaUrl
            wikipediaUrl.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
