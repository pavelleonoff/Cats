package com.redprism.cats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.redprism.cats.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImagesAdapter(val width:Int,val height:Int) :RecyclerView.Adapter<ImagesAdapter.ImagesHolder>()  {

    private var catImages = mutableListOf<String>()
    private var imagesDownload = false
    inner class ImagesHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var catDetailImages : ImageView
        init {
            catDetailImages = itemView.findViewById(R.id.catDetailImages)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout
            .cat_images,parent,false)
        return ImagesHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        if(!imagesDownload&&position==1){
            Picasso.get().load(R.drawable.infinitive_progressbar_small)
        }
        else {
            val curImg = catImages[position]
            val image = holder.catDetailImages
            Picasso.get().load(curImg)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.infinitive_progressbar_small)
                .into(image, object : Callback { override fun onSuccess() {}
                    override fun onError(e: Exception?) {
                        Picasso.get().load(curImg)
                            .resize(width, 0)
                            .centerInside()
                            .placeholder(R.drawable.infinitive_progressbar_small).into(image)
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return if(!imagesDownload){
            2
        } else{
            catImages.size
        }
    }
    fun addFirstImage(imgs: List<String>){
        this.catImages.addAll(imgs)
        notifyItemRangeInserted(0,2)
    }
    fun addImages(imgs: List<String>) {
        this.catImages.addAll(imgs)
        notifyDataSetChanged()
        imagesDownload = true
    }
}






