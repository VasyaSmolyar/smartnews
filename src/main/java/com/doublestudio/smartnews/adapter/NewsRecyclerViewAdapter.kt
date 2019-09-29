package com.doublestudio.smartnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doublestudio.smartnews.R

class NewsRecyclerViewAdapter(private val mCtx: Context, private val itemList: List<NewsRecyclerViewItem>)
    : RecyclerView.Adapter<NewsRecyclerViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):NewsRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.layout_news, parent, false)
        val vh = NewsRecyclerViewHolder(inflater)
        //val card = inflater.findViewById<CardView>(R.id.card)
        //card.setOnClickListener {

        //}
        return vh
    }
    override fun onBindViewHolder(holder:NewsRecyclerViewHolder, position:Int) {
        holder.message.text = itemList[position].message
        holder.dateMessage.text = itemList[position].dateMessage
        holder.name.text = itemList[position].name
        Glide.with(mCtx).load(itemList[position].ava).into(holder.ava)
    }
    override fun getItemId(position:Int):Long {
        return position.toLong()
    }
    override fun getItemViewType(position:Int):Int {
        return position
    }

}