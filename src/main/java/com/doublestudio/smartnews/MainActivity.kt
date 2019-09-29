package com.doublestudio.smartnews

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublestudio.smartnews.adapter.MainRecyclerViewAdapter
import com.doublestudio.smartnews.adapter.MainRecyclerViewItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext
import android.R.attr.data
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import java.util.*
import kotlin.Comparator


class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    val okHttpContext = coroutineContext + CoroutineName("OkHttp")
    private val client = OkHttpClient()
    val handler = Handler()
    //var page = 0
    private val list = mutableListOf<MainRecyclerViewItem>()
    private val list2 = mutableListOf<MainRecyclerViewItem>()
    private val host = "http://demo17.charlie.vkhackathon.com:5000/api/news/get?format=json"
    lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_filter -> {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun addS(){

        //if(swipe.isRefreshing)
            //return
        launch (okHttpContext) {
        swipe.isRefreshing = true

            try {
                val request = Request.Builder().url(host).build()
                val response = client.newCall(request).execute()
                val str = response.body?.string()
                JsonParser().parse(str).asJsonArray.forEach {
                    val o = it.asJsonObject
                    //withContext(Dispatchers.Main) {
                            list.add(
                                MainRecyclerViewItem(
                                    o["title"].asString,
                                    o["date"].asString.replace("T", " ").replace("Z", " "),
                                    o["intro"].asString,
                                    o["concrete"].asFloat,
                                    o["authorPosition"].asFloat.toInt(),
                                    o["author"].asString,
                                    o["cover"].asString,
                                    o["article"].asString,
                                    o["article_id"].asInt
                                )
                            )

                        //Log.e("", "444")
                    //}

                }
                //page++
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //recycler_view.adapter?.notifyDataSetChanged()
            swipe.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        addS()
        swipe.setOnRefreshListener {
            //page = 0
            list.clear()
            recycler_view.adapter?.notifyDataSetChanged()
            addS()

            //recycler_view.adapter?.notifyDataSetChanged()
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager!!.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    //addS()
                }
            }
        })

        setSupportActionBar(toolbar.apply {
            setOnClickListener {
                recycler_view.layoutManager?.scrollToPosition(0)
            }
        })
        //spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf(""))
        //startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        concrete.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                list.sortWith(Comparator { lhs, rhs ->
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if ( rhs.concrete < lhs.concrete) 1 else  -1
                })
            }else{
                list.sortWith(Comparator { lhs, rhs ->
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if ( rhs.id < lhs.id) 1 else  -1
                })
                recycler_view.adapter?.notifyDataSetChanged()
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 2){
                    list.sortWith(Comparator { lhs, rhs ->
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        if ( rhs.id < lhs.id) 1 else  -1
                    })
                    recycler_view.adapter?.notifyDataSetChanged()
                }else{
                    list.sortWith(Comparator { lhs, rhs ->
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        if ( rhs.authorPosition == position + 1) 1 else  -1
                    })
                    recycler_view.adapter?.notifyDataSetChanged()
                }
            }
        }


        recycler_view.adapter = MainRecyclerViewAdapter(this, list)
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                launch (okHttpContext) {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({

                        if(!newText.isNullOrEmpty()) {
                            list.sortWith(Comparator { lhs, rhs ->
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if (( rhs.intro.contains(newText) || rhs.title.contains(newText))) 1 else  -1
                            })
                            recycler_view.adapter?.notifyDataSetChanged()
                        }else {
                            list.sortWith(Comparator { lhs, rhs ->
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if ( rhs.id < lhs.id) 1 else  -1
                            })
                            recycler_view.adapter?.notifyDataSetChanged()
                        }

                    }, 500)
                }
                return true
            }
        })
    }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {

                val outRect = Rect()
                bottom_sheet.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()))
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        return super.dispatchTouchEvent(event)
    }
}
