package com.doublestudio.smartnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.bumptech.glide.Glide
import com.doublestudio.smartnews.adapter.NewsRecyclerViewAdapter
import com.doublestudio.smartnews.adapter.NewsRecyclerViewItem
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.layout_news.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext

class NewsActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val okHttpContext = coroutineContext + CoroutineName("OkHttp2")
    private val host = "http://demo17.charlie.vkhackathon.com:5000/api/comments/get?article_id="
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        recycler_view.adapter = NewsRecyclerViewAdapter(this, listOf<NewsRecyclerViewItem>())
        val a = intent?.extras!!.getStringArrayList("list")!!

        findViewById<TextView>(R.id.title).text = a[4]
        author.text = a[2]
        text.text = a[1].trim()
        date.text  = a[3].replace("T", " ").replace("Z", " ")
        if(!a[0].isNullOrEmpty())
            Glide.with(this).load(a[0]).into(imageView)
        launch(okHttpContext){
            try {
                val request = Request.Builder().url(host+a[5]).build()
                val response = client.newCall(request).execute()
                val str = response.body?.string()

            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

}
