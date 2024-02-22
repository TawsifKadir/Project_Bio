package com.xplo.code.core

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 1/31/22
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
abstract class PaginationListener : RecyclerView.OnScrollListener() {

    companion object {
        private const val TAG = "PaginationListener"
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        Log.d(TAG, "onScrolled() called with: recyclerView = $recyclerView, dx = $dx, dy = $dy")

        if (dy <= 0) return

        if (isLoading()) return
        if (isLastPage()) return

        if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
            onLoadMore()
        }

    }

    protected abstract fun onLoadMore()
    protected abstract fun isLoading(): Boolean
    protected abstract fun isLastPage(): Boolean
    protected abstract fun getPageSize(): Int
}