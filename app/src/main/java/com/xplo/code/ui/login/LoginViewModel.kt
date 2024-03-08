package com.xplo.code.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kit.integrationmanager.model.Address
import com.kit.integrationmanager.model.Location
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.payload.login.request.LoginRequest
import com.kit.integrationmanager.service.DeviceManager
import com.kit.integrationmanager.service.LoginServiceImpl
import com.kit.integrationmanager.store.AuthStore
import com.xplo.code.core.TestConfig
import com.xplo.code.data_module.core.DispatcherProvider
import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.fake.Fake
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRsp
import com.xplo.code.data_module.model.user.ResetPassRqb
import com.xplo.code.data_module.model.user.ResetPassRsp
import com.xplo.code.data_module.model.user.isPending
import com.xplo.code.data_module.repo.UserRepo
import com.xplo.code.ui.login.model.LoginCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Observable
import java.util.Observer
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel(), Observer {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class LoginSuccess(val token: String, val username: String?) : Event()
        class LoginPending(val token: String, val username: String?) : Event()
        class LoginFailure(val msg: String?) : Event()

        class ResetPasswordSuccess(val rsp: ResetPassRsp?) : Event()
        class ResetPasswordFailure(val msg: String?) : Event()

        class RegisterDeviceSuccess(val rsp: RegisterDeviceRsp?) : Event()
        class RegisterDeviceFailure(val msg: String?) : Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun clearEvent() {
        _event.value = Event.Empty
    }

    fun passwordLogin(credentials: LoginCredentials) {
        Log.d(TAG, "passwordLogin() called with: loginCredentials = $credentials")

        if (TestConfig.isFakeLoginEnabled) {
            _event.value = Event.LoginSuccess(Fake.token, null)
            return
        }

        val body = LoginRqb(credentials.userId, credentials.password)
        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = repo.passwordLogin(body)) {

                is Resource.Failure -> {
                    _event.value = Event.LoginFailure(response.callInfo?.msg)
                }

                is Resource.Success -> {
                    val token = response.data?.token
                    if (token.isNullOrEmpty()) {
                        _event.value = Event.LoginFailure(response.callInfo?.msg)
                    } else {

                        if (response.data.isPending()){
                            _event.value = Event.LoginPending(token, response.data.userName)
                        }else{
                            _event.value = Event.LoginSuccess(token, response.data.userName)
                        }

                    }
                }

                else -> {}
            }
        }


    }


    fun resetPassword(newPassword: String?) {
        Log.d(TAG, "resetPassword() called with: newPassword = $newPassword")

        val body = ResetPassRqb(newPassword)
        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = repo.resetPassword(body)) {

                is Resource.Failure -> {
                    _event.value = Event.ResetPasswordFailure(response.callInfo?.msg)
                }

                is Resource.Success -> {
                    _event.value = Event.ResetPasswordSuccess(response.data)
                }

                else -> {}
            }
        }

    }


    fun registerDevice(address: Address, location: Location, deviceId: String?, imei: String?) {
        Log.d(
            TAG,
            "registerDevice() called with: address = $address, location = $location, deviceId = $deviceId, imei = $imei"
        )

        val body = RegisterDeviceRqb(
            address = address,
            location = location,
            deviceId = deviceId,
            imei = imei
        )
        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = repo.registerDevice(body)) {

                is Resource.Failure -> {
                    _event.value = Event.RegisterDeviceFailure(response.callInfo?.msg)
                }

                is Resource.Success -> {
                    _event.value = Event.RegisterDeviceSuccess(response.data)
                }

                else -> {}
            }
        }

    }

    fun passwordLoginwithSDK(credentials: LoginCredentials, context: Context) {
        Log.d(TAG, "passwordLoginwithSdk() called with: loginCredentials = $credentials")

        if (TestConfig.isFakeLoginEnabled) {
            _event.value = Event.LoginSuccess(Fake.token, null)
            return
        }
            try{
                if(DeviceManager.getInstance(context).isOnline) {
                    val serverInfo = ServerInfo();
                    serverInfo.port=8090;
                    serverInfo.protocol="http"
                    serverInfo.host_name="snsopafis.karoothitbd.com"
                    val loginService = LoginServiceImpl(context,this,serverInfo)
                    val headers = HashMap<String,String>()
                    val loginRequest = LoginRequest.builder().userName(credentials.userId).password(credentials.password).deviceId(DeviceManager.getInstance(context).deviceUniqueID).build()
                    loginService.doOnlineLogin(loginRequest,headers)
                }else {
                    val login = AuthStore.getInstance(context).loginInfoFromCache
                    if(login==null){
                        Log.e(TAG,"Intenet connectivity is required for device setup.")
                    }
                    else if(login.userName==credentials.userId && login.password==credentials.password){
                        Log.e(TAG,"Offline Login Successful")
                    }else{
                        Log.e(TAG,"Offline login failed")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG,"Login Failed")
            }

    }

    override fun update(o: Observable?, arg: Any?) {
        Log.d(TAG,"Got response")
    }
}