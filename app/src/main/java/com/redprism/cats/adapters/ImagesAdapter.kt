package com.redprism.cats.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.redprism.cats.MainActivity
import com.redprism.cats.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception


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
        Picasso.get().load(curImg)
            .resize(MainActivity.screenWidth,0)
            .centerCrop()
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.infinitive_progressbar).into(image,object: Callback{
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(curImg)
                        .resize(MainActivity.screenWidth,0)
                        .centerCrop()
                        .placeholder(R.drawable.infinitive_progressbar).into(image)
                }

            })


    }

    override fun getItemCount(): Int {
        return catImages.size
    }
    fun addImages(imgs: List<String>) {
        this.catImages.addAll(imgs)
        notifyItemRangeInserted(1,imgs.size)
    }
}






