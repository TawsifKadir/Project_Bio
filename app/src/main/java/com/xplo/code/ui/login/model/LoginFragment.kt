package com.xplo.code.ui.dashboard.alternate.forms


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kit.integrationmanager.model.Login
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.payload.login.request.LoginRequest
import com.kit.integrationmanager.service.DeviceManager
import com.kit.integrationmanager.service.LoginServiceImpl
import com.kit.integrationmanager.store.AuthStore
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.databinding.FragmentLoginBinding
import com.xplo.code.ui.login.LoginContract
import com.xplo.code.ui.login.LoginViewModel
import com.xplo.code.ui.login.model.LoginCredentials
import dagger.hilt.android.AndroidEntryPoint
import java.util.Observer


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class LoginFragment : BaseFragment(), LoginContract.LoginView, Observer {

    companion object {
        const val TAG = "LoginFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): LoginFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var interactor: LoginContract.View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LoginContract.View) {
            interactor = activity as LoginContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }
    override fun initInitial() {

    }

    override fun initView() {

    }
    override fun initObserver() {
        binding.btLogin.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val password = binding.etPassword.text.toString()
            val loginCredentials = LoginCredentials(userId, password)
//            try{
//                if(DeviceManager.getInstance(requireContext()).isOnline) {
//                    val serverInfo = ServerInfo();
//                    serverInfo.port=8090;
//                    serverInfo.protocol="http"
//                    serverInfo.host_name="snsopafis.karoothitbd.com"
//                    val loginService = LoginServiceImpl(requireContext(),this,serverInfo)
//                    val headers = HashMap<String,String>()
//                    val loginRequest = LoginRequest.builder().userName(userId).password(password).deviceId(DeviceManager.getInstance(requireContext()).deviceUniqueID).build()
//                    loginService.doOnlineLogin(loginRequest,headers)
//                }else {
//                    val login = AuthStore.getInstance(requireContext()).loginInfoFromCache
//                    if(login==null){
//                        Log.e(TAG,"Intenet connectivity is required for device setup.")
//                    }
//                    else if(login.userName==userId && login.password==password){
//                        Log.e(TAG,"Offline Login Successful")
//                    }else{
//                        Log.e(TAG,"Offline login failed")
//                    }
//                }
//            }catch (e : Exception){
//                Log.d(TAG,"Login Failed")
//            }
            //presenter.passwordLogin(loginCredentials)
            viewModel.passwordLoginwithSDK(loginCredentials,requireContext())
        }

        binding.btSignup.setOnClickListener {
            navigateToSignup()
        }

        binding.btResetPass.setOnClickListener {
            navigateToResetPassword()
        }
        //btSignup.setOnClickListener(this)

//        binding.cbShowPassword.setOnCheckedChangeListener { buttonView, isChecked ->
//            // checkbox status is changed from uncheck to checked.
//            if (!isChecked) {
//                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
//            } else {
//                binding.etPassword.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//            }
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is LoginViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is LoginViewModel.Event.LoginSuccess -> {
                        hideLoading()
                        onLoginSuccess(event.token!!, event.id)
                        viewModel.clearEvent()
                    }

                    is LoginViewModel.Event.LoginFailure -> {
                        hideLoading()
                        onLoginFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }

    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun navigateToSignup() {
        interactor?.navigateToSignUp()
    }

    override fun navigateToResetPassword() {
        interactor?.navigateToReset()
    }

    override fun onLoginSuccess(token: String, id: String?) {
        Log.d(TAG, "onLoginSuccess() called with: token = $token, id = $id")
        getPrefHelper().setAccessToken(token)
        getPrefHelper().setUserId("abc07")
        navigateToHome()
        requireActivity().finish()
    }

    override fun onLoginFailure(msg: String?) {
        Log.d(TAG, "onLoginFailure() called with: msg = [$msg]")
        showErrorMessage(msg)
    }

    override fun update(o: java.util.Observable?, arg: Any?) {
        Log.d(TAG,"Update Called")
    }

}
