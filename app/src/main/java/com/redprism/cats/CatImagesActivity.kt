package com.redprism.cats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.redprism.cats.adapters.ImagesAdapter
import com.redprism.cats.data.MainViewModel
import com.redprism.cats.animation.PageImageTransform

import com.redprism.cats.data.DBQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
        val data = intent
        val id = data.getStringExtra("id")!!
        val image = data.getStringExtra("image")!!
        val dbQuery = DBQueries()
        val adapter = ImagesAdapter(image!!)
        viewPager.adapter = adapter

        val cat = viewModel.getCatById(id)

        cat?.observe(this) { t ->
            Log.i("Test33",(t.images.equals("").toString()))
            if (t.images.equals("")) {
                CoroutineScope(Dispatchers.Main).launch {
                    val res = getImages(dbQuery, viewModel, id, image)
                    Log.i("Test4",res)
                    setImages(res, adapter)
                }

            } else {
                setImages(t.images, adapter)
            }
        }

    }

}

private suspend fun getImages(dbQuery: DBQueries,
                              viewModel: MainViewModel,
                              id: String,
                              image: String) = CoroutineScope(Dispatchers.IO).async {
    return@async dbQuery.downloadCatImagesFromNetwork(viewModel, id, image)
}.await()





        private fun setImages(images:String,adapter:ImagesAdapter) {
    adapter.addImages(images.split(","))


}




