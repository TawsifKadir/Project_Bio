package com.xplo.code.ui.dashboard.alternate

import androidx.lifecycle.ViewModel
import com.xplo.code.data_module.core.DispatcherProvider
import com.xplo.code.data_module.model.content.ContentItem
import com.xplo.code.data_module.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AlternateViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "AlternateViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetItemSuccess(val items: List<ContentItem>) : Event()
        class GetItemFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event


}