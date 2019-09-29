package com.doublestudio.smartnews.util

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class AutofitRecyclerView : RecyclerView {

    private var manager: WrapContentGridLayoutManager? = null

    private var columnWidth: Int = 0
    private var maxColumnCount: Int = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    @SuppressWarnings("ResourceType")
    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth, android.R.attr.columnCount)
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            maxColumnCount = array.getInt(1, 999)
            array.recycle()
        }

        manager = WrapContentGridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            val mSpanCount = constrains(measuredWidth / columnWidth, 1, maxColumnCount)
            manager!!.spanCount = mSpanCount
        }
    }

    companion object {

        fun constrains(input: Int, a: Int, b: Int): Int {
            var result = input
            val min = min(a, b)
            val max = max(a, b)
            result = if (result > min) result else min
            result = if (result < max) result else max
            return result
        }
    }
}