package com.redprism.catbreeds

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.redprism.catbreeds.adapters.CatImagesAdapter
import com.redprism.catbreeds.animation.PageImageTransform
import com.redprism.catbreeds.viewmodel.MainViewModel
import kotlin.properties.Delegates

class CatImagesActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var viewModel: MainViewModel
    private var heightPixels by Delegates.notNull<Int>()
    private var widthPixels by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_images)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewPager = findViewById(R.id.image_catId)
        viewPager.setPageTransformer(PageImageTransform())
        widthPixels = resources.displayMetrics.widthPixels
        heightPixels = resources.displayMetrics.heightPixels
        val data = intent
        val id = data.getStringExtra("id")!!
        val image = data.getStringExtra("image")!!
        val adapter = CatImagesAdapter(widthPixels, heightPixels)
        viewPager.adapter = adapter
        adapter.addFirstImage(listOf(image))
        val catImages = viewModel.catImages
        catImages.observe(this) { cImg->
            if (!cImg.equals("")) {
                adapter.addImages(cImg.split(","))
            }
        }
        viewModel.getCatImages(id, image)
    }
}






