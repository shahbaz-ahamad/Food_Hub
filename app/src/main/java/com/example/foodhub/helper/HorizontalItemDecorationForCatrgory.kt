package com.example.foodhub.helper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.time.temporal.TemporalAmount

class HorizontalItemDecorationForCatrgory (private val amount:Int = 40): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom=amount
    }
}