package com.redprism.cats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redprism.cats.MainActivity
import com.redprism.cats.R
import com.redprism.cats.data.Cat
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception


class CatsAdapter: RecyclerView.Adapter<CatsAdapter.CatsViewHolder>() {
    private var cats : ArrayList<Cat> = ArrayList()
    private var onCatClickListener: OnCatClickListener? = null

    interface OnCatClickListener{
        fun onCatClick(id:Int)
    }

    fun setOnCatClickListener(onCatClickListener: OnCatClickListener){
        this.onCatClickListener = onCatClickListener
    }

    inner class CatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var cat_item_name : TextView
        var cat_item_image : ImageView
        init {
            cat_item_name = itemView.findViewById(R.id.cat_item_name)
            cat_item_image = itemView.findViewById(R.id.cat_item_image)
            itemView.setOnClickListener {
                if (onCatClickListener != null) {
                    onCatClickListener!!.onCatClick(adapterPosition)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cat_item,parent,false)
        return CatsViewHolder(view)
    }
    override fun onBindViewHolder(holder: CatsViewHolder, position: Int) {
        val name = cats[position].name
        val nameTextView: TextView = holder.cat_item_name
        val img = cats[position].image
        val imgImageView: ImageView = holder.cat_item_image
        nameTextView.text = name
        Picasso.get().load(img).resize(MainActivity.screenWidth,0)
            .onlyScaleDown()
            .placeholder(R.drawable.infinitive_progressbar)
            .into(imgImageView,object: Callback {
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(img)
                        .resize(MainActivity.screenWidth,0)
                        .centerCrop()
                        .placeholder(R.drawable.infinitive_progressbar).into(imgImageView)
                }

            })

    }
    override fun getItemCount(): Int {
        return cats.size
    }
    fun setCats(catsArr: ArrayList<Cat>) {
        cats.clear()
        cats.addAll(catsArr)
        notifyDataSetChanged()
    }
    fun getCats(): ArrayList<Cat> {
        return cats
    }
}




