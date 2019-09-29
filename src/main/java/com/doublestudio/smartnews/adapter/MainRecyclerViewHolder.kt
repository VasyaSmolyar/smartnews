package com.doublestudio.smartnews.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doublestudio.smartnews.R

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.title_view)
    var intro: TextView = itemView.findViewById(R.id.intro_view)
    var author: TextView = itemView.findViewById(R.id.author)
    var image: ImageView = itemView.findViewById(R.id.image_view)
    var date: TextView = itemView.findViewById(R.id.date_view)
}