package com.xplo.code.ui.content_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xplo.data.model.content.ContentItem
import com.xplo.data.repo.UserRepo
import com.xplo.data.utils.DispatcherProvider
import com.xplo.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentListViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "ContentListViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetItemSuccess(val items: List<ContentItem>) : Event()
        class GetItemFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun getContents(offset: Int, limit: Int) {


    }

}