package com.xplo.code.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xplo.code.ui.login.model.LoginCredentials
import com.xplo.data.model.user.LoginRqb
import com.xplo.data.repo.UserRepo
import com.xplo.data.utils.DispatcherProvider
import com.xplo.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class LoginSuccess(val token: String?, val id: String?) : Event()
        class LoginFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun clearEvent() {
        _event.value = Event.Empty
    }

    fun passwordLogin(loginCredentials: LoginCredentials) {
        Log.d(TAG, "passwordLogin() called with: loginCredentials = $loginCredentials")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = repo.passwordLogin(LoginRqb(null, null))) {

                is Resource.Failure -> {
                    _event.value = Event.LoginFailure(response.callInfo?.msg)
                }

                is Resource.Success -> {
                    _event.value = Event.LoginSuccess(response.data?.token, response.data?.token)
                }

                else -> {}
            }
        }


    }


}