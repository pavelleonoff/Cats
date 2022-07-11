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


class ImagesAdapter(width:Int,height:Int) :RecyclerView.Adapter<ImagesAdapter.ImagesHolder>()  {
    private val width = width
    private val height = height
    private var catImages = mutableListOf<String>()
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
        val curImg = catImages[position]

        val image = holder.catDetailImages
        Picasso.get().load(curImg)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.infinitive_progressbar_small).into(image,object: Callback{
                override fun onSuccess() {
                }
                override fun onError(e: Exception?) {

                    Picasso.get().load(curImg)
                        .resize(width ,0)
                        .centerInside()
                        .placeholder(R.drawable.infinitive_progressbar_small).into(image)
                }
            })
    }

    override fun getItemCount(): Int {
        return catImages.size
    }
    fun addImages(imgs: List<String>) {
        val rangeStart = catImages.size
        this.catImages.addAll(imgs)
        val rangeEnd = catImages.size
        notifyItemRangeInserted(rangeStart,rangeEnd)
    }
}






