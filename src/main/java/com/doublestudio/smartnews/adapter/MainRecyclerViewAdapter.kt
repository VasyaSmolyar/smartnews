package com.doublestudio.smartnews.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doublestudio.smartnews.NewsActivity
import com.doublestudio.smartnews.R

class MainRecyclerViewAdapter(private val mCtx: Context, private val itemList: List<MainRecyclerViewItem>)
    : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):MainRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.layout_main, parent, false)
        val vh = MainRecyclerViewHolder(inflater)
        val card = inflater.findViewById<CardView>(R.id.card)
        card.setOnClickListener {
            val a = itemList[vh.adapterPosition]
            mCtx.startActivity(Intent(mCtx, NewsActivity::class.java).apply {
                putExtra("list", arrayListOf(a.cover,
                    a.article,
                    a.author,
                    a.date,
                    a.title,
                    a.id.toString()))
            })
        }
        return vh
    }
    override fun onBindViewHolder(holder:MainRecyclerViewHolder, position:Int) {
        holder.title.text = itemList[position].title
        holder.intro.text = itemList[position].intro
        holder.date.text = itemList[position].date
        holder.author.text = itemList[position].author
        if  (itemList[position].cover != "")
            Glide.with(mCtx).load(itemList[position].cover).into(holder.image)
        else
            holder.image.setImageDrawable(null)
    }
    override fun getItemId(position:Int):Long {
        return position.toLong()
    }
    override fun getItemViewType(position:Int):Int {
        return position
    }

}