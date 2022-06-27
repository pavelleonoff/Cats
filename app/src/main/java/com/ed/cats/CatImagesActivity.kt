package com.ed.cats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ed.cats.adapters.ImagesAdapter
import com.ed.cats.data.MainViewModel
import com.ed.cats.animation.PageImageTransform
import com.ed.cats.data.DBQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatImagesActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_images)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewPager = findViewById(R.id.image_catId)
        viewPager.setPageTransformer(PageImageTransform())
        val data = getIntent()
        val id = data.getStringExtra("id")
        val image = data.getStringExtra("image")

        val adapter = ImagesAdapter(image!!)
        viewPager.adapter = adapter

        if (!viewModel.isImagesExist(id!!)) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                DBQueries.downloadCatImagesFromNetwork(viewModel, id, image)
                setImages(id,adapter)
            }
        }
        else{
            setImages(id,adapter)
        }

    }
    private fun setImages(id:String,adapter:ImagesAdapter){
        CoroutineScope(Dispatchers.Main).launch {
                viewModel.getCatImages(id).let {
                    it?.observe(this@CatImagesActivity) { it ->
                        adapter.addImages(it.split(","))
                    }
            }
        }

    }
}

