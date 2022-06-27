package com.ed.cats.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ed.cats.MainActivity
import com.ed.cats.R
import com.squareup.picasso.Picasso



class ImagesAdapter(images: String):RecyclerView.Adapter<ImagesAdapter.ImagesHolder>()  {
    private var catImages = mutableListOf(images)
    inner class ImagesHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var catDetailImages : ImageView
        init {
            catDetailImages = itemView.findViewById(R.id.catDetailImages)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cat_images,parent,false)
        return ImagesHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val curImg = catImages[position]
        val image = holder.catDetailImages
        Log.i("Cats","2")
        Log.i("Cats","CatsArrSize-"+catImages.size.toString())
        Picasso.get().load(curImg)
            .resize(MainActivity.screenWidth,0)
            .onlyScaleDown()
            .placeholder(R.drawable.placeholder).into(image)
    }

    override fun getItemCount(): Int {
        return catImages.size
    }
    fun addImages(imgs: List<String>) {
        this.catImages.addAll(imgs)
        notifyDataSetChanged()
    }
}






