package com.ed.cats.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ed.cats.R
import com.squareup.picasso.Picasso


class CatsAdapter: RecyclerView.Adapter<CatsAdapter.CatsViewHolder>() {
    private var cats : ArrayList<Cats>
    private var onCatClickListener: OnCatClickListener? = null

    interface OnCatClickListener{
        fun onCatClick(id:Int)
    }

    init {
        cats = ArrayList()
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
        val name = cats.get(position).name
        val nameTextView: TextView = holder.cat_item_name
        val img = cats.get(position).image
        val imgImageView: ImageView = holder.cat_item_image
        Picasso.get().load(img).into(imgImageView);
        nameTextView.setText(name)
    }
    override fun getItemCount(): Int {
        return cats.size
    }
    fun setCats(catsArr: ArrayList<Cats>) {
        cats.addAll(catsArr)
        notifyDataSetChanged()
    }
    fun getCats(): ArrayList<Cats> {
        return cats
    }
    fun addCats(catArr: ArrayList<Cats>){
        cats.addAll(catArr)
        notifyDataSetChanged()
    }
}




