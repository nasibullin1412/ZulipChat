package com.homework.coursework.presentation.discuss

import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PagingScrollListener(
    private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
        var firstVisibleItemPosition = 0
        val totalItemCount: Int = layoutManager.itemCount
        if (layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        if (!loading && firstVisibleItemPosition < VISIBLE_THRESHOLD) {
            onLoadMore(true, totalItemCount, recyclerView)
            loading = true
        }
    }

    abstract fun onLoadMore(top: Boolean, totalItemsCount: Int, view: RecyclerView?): Boolean

    companion object {
        const val VISIBLE_THRESHOLD = 5
    }
}
