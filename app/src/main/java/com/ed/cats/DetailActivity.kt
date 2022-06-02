package com.ed.cats


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.ed.cats.data.Cat
import com.ed.cats.data.MainViewModel
import com.squareup.picasso.Picasso


class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel

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
    private lateinit var experimental : TextView
    private lateinit var hairless : TextView
    private lateinit var natural : TextView
    private lateinit var rare : TextView
    private lateinit var rex : TextView
    private lateinit var suppressedTail : TextView
    private lateinit var shortLegs : TextView
    private lateinit var hypoallergenic : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        image = findViewById(R.id.image_catId)
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
        experimental = findViewById(R.id.experimental)
        hairless = findViewById(R.id.hairless)
        natural = findViewById(R.id.natural)
        rare = findViewById(R.id.rare)
        rex = findViewById(R.id.rex)
        suppressedTail = findViewById(R.id.suppressed_tail)
        shortLegs = findViewById(R.id.short_legs)
        hypoallergenic = findViewById(R.id.hypoallergenic)
        id = intent.getStringExtra("id").toString()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val cat = viewModel.getCatById(id)
        cat?.observe(this) { t -> putDataToFields(t as Cat) }
    }
    private fun putDataToFields(cat: Cat?){
        if(cat!=null){
            Picasso.get().load(cat.image).resize(MainActivity.screenWidth,0).
            placeholder(R.drawable.placeholder).into(image)
            name.text = cat.name
            description.text = cat.description
            lifeSpan.text = getString(R.string.life_span) + cat.lifeSpan
            temperament.text = getString(R.string.temperament) + cat.temperament
            origin.text = getString(R.string.origin) + cat.origin
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