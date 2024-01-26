package com.xplo.code.ui.favorite

import androidx.lifecycle.ViewModel
import com.xplo.data.model.content.ContentItem
import com.xplo.data.repo.UserRepo
import com.xplo.data.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "FavoriteViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetItemSuccess(val items: List<ContentItem>) : Event()
        class GetItemFailure(val msg: String?) : Event()

        class UpdateFavoriteSuccess(val position: Int) : Event()
        class UpdateFavoriteFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun getFavorites(offset: Int, limit: Int) {

//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = repo.getFavorites(offset, limit)) {
//
//                is Resource.Failure -> {
//                    _event.value = Event.GetItemFailure(response.callInfo?.msg)
//                }
//
//                is Resource.Success -> {
//                    //_data.value = Event.Success(response.data?.items)
//                    val items = response.data?.items
//                    if (items.isNullOrEmpty()) {
//                        _event.value = Event.GetItemFailure(response.callInfo?.msg)
//                    } else {
//                        _event.value = Event.GetItemSuccess(items)
//                    }
//                }
//            }
//        }
//

    }


}