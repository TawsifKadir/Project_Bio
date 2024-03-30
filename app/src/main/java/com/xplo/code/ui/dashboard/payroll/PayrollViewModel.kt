package com.xplo.code.ui.dashboard.payroll

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xplo.code.ui.dashboard.model.PayrollEntry

class PayrollViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var entries
        get() = savedStateHandle.get<List<PayrollEntry.Result>>("entries")
        set(value) {
            savedStateHandle["entries"] = value
            savedStateHandle["currentPageEntries"] = value?.slice(0 ..9)
        }
    val currentPageEntries= savedStateHandle.getLiveData<List<PayrollEntry.Result>>("currentPageEntries")
    var currentPageEntry
        get() = savedStateHandle.get<PayrollEntry.Result>("currentPageEntry")
        set(value) {
            savedStateHandle["currentPageEntry"] = value
        }

    var currentPageNo = 1

    fun changePage(next : Boolean = true, lastOrFirstIndexValue: PayrollEntry.Result){
        val totalPage = (entries?.size?:0) / 10
        if(next && currentPageNo+1 <= totalPage){
            val lastIndex = entries?.indexOf(lastOrFirstIndexValue) ?:0
            val newLastIndex = if((entries?.size?:0) >= lastIndex+10) lastIndex+10 else entries?.indexOf(entries?.last()) ?:0
            currentPageNo++
            savedStateHandle["currentPageEntries"] = entries?.slice(lastIndex+1 ..newLastIndex)
        }
        else if (!next && currentPageNo > 1){
            val firstIndex = entries?.indexOf(lastOrFirstIndexValue) ?:0
            currentPageNo--
            savedStateHandle["currentPageEntries"] = entries?.slice(firstIndex-10..<firstIndex)
        }
    }

}