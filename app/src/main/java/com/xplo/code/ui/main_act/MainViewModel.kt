package com.xplo.code.ui.main_act

import androidx.lifecycle.ViewModel
import com.xplo.data.repo.UserRepo
import com.xplo.data.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class TaskSuccess(val id: String?) : Event()
        class TaskFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun clearEvent() {
        _event.value = Event.Empty
    }


}