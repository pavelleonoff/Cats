package com.redprism.cats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redprism.cats.R
import com.redprism.cats.data.Cat
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception


class CatsAdapter(width:Int,height:Int): RecyclerView.Adapter<CatsAdapter.CatsViewHolder>() {
    private val width = width
    private val height = height

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
        var cat_item_experimental : TextView
        var cat_item_hairless : TextView
        var cat_item_natural : TextView
        var cat_item_rare : TextView
        var cat_item_rex : TextView
        var cat_item_suppressedTail : TextView
        var cat_item_shortLegs : TextView
        var cat_item_hypoallergenic : TextView
        init {
            cat_item_name = itemView.findViewById(R.id.cat_item_name)
            cat_item_image = itemView.findViewById(R.id.cat_item_image)
            cat_item_experimental = itemView.findViewById(R.id.item_experimental)
            cat_item_hairless = itemView.findViewById(R.id.item_hairless)
            cat_item_natural = itemView.findViewById(R.id.item_natural)
            cat_item_rare = itemView.findViewById(R.id.item_rare)
            cat_item_rex = itemView.findViewById(R.id.item_rex)
            cat_item_suppressedTail = itemView.findViewById(R.id.item_suppressed_tail)
            cat_item_shortLegs = itemView.findViewById(R.id.item_short_legs)
            cat_item_hypoallergenic = itemView.findViewById(R.id.item_hypoallergenic)

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
        val cat_item_experimental = holder.cat_item_experimental
        val cat_item_hairless = holder.cat_item_hairless
        val cat_item_natural = holder.cat_item_natural
        val cat_item_rare = holder.cat_item_rare
        val cat_item_rex = holder.cat_item_rex
        val cat_item_suppressedTail = holder.cat_item_suppressedTail
        val cat_item_shortLegs = holder.cat_item_shortLegs
        val cat_item_hypoallergenic = holder.cat_item_hypoallergenic
        nameTextView.text = name

        if(cats[position].experimental==1) cat_item_experimental.visibility = View.VISIBLE
        else{cat_item_experimental.visibility = View.GONE}
        if(cats[position].hairless==1) cat_item_hairless.visibility = View.VISIBLE
        else{cat_item_hairless.visibility  = View.GONE}
        if(cats[position].natural==1) cat_item_natural.visibility = View.VISIBLE
        else{cat_item_natural.visibility = View.GONE}
        if(cats[position].rare==1) cat_item_rare.visibility = View.VISIBLE
        else{cat_item_rare.visibility = View.GONE}
        if(cats[position].rex==1) cat_item_rex.visibility = View.VISIBLE
        else{cat_item_rex.visibility = View.GONE}
        if(cats[position].suppressedTail==1) cat_item_suppressedTail.visibility = View.VISIBLE
        else{cat_item_suppressedTail.visibility = View.GONE}
        if(cats[position].shortLegs==1) cat_item_shortLegs.visibility = View.VISIBLE
        else{cat_item_shortLegs.visibility = View.GONE}
        if(cats[position].hypoallergenic==1) cat_item_hypoallergenic.visibility = View.VISIBLE
        else{cat_item_hypoallergenic.visibility = View.GONE}
        Picasso.get().load(img)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.infinitive_progressbar_small)
            .into(imgImageView,object: Callback {
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(img)
                        .resize(width,height)
                        .centerInside()
                        .placeholder(R.drawable.infinitive_progressbar_big).into(imgImageView)
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
    fun getAdapterCats(): ArrayList<Cat> {
        return cats
    }
}




