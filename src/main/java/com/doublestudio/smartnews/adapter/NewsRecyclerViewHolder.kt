package com.doublestudio.smartnews.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.doublestudio.smartnews.R

class NewsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.findViewById(R.id.name)
    var message: TextView = itemView.findViewById(R.id.message)
    var dateMessage: TextView = itemView.findViewById(R.id.dateMessage)
    var ava: ImageView = itemView.findViewById(R.id.ava)
}