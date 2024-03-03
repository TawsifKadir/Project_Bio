package com.xplo.code.ui.dashboard.alternate.forms


import android.content.Context
import android.database.Observable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import java.util.Observer
import androidx.lifecycle.lifecycleScope
import com.kit.integrationmanager.model.Login
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.payload.login.request.LoginRequest
import com.kit.integrationmanager.service.DeviceManager
import com.kit.integrationmanager.service.LoginService
import com.kit.integrationmanager.service.LoginServiceImpl
import com.kit.integrationmanager.store.AuthStore
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.checkRbOpAB
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentAlForm1PerInfoBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.CheckboxListAdapter
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.CheckboxItem
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.ui.dashboard.model.isOk

import com.xplo.code.BuildConfig
import com.xplo.code.base.BaseFragment
import com.xplo.code.databinding.FragmentLoginBinding
import com.xplo.code.ui.login.LoginActivity
import com.xplo.code.ui.login.LoginContract
import com.xplo.code.ui.login.LoginViewModel
import com.xplo.code.ui.login.model.LoginCredentials
import dagger.hilt.android.AndroidEntryPoint


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
            try{
                if(DeviceManager.getInstance(requireContext()).isOnline) {


                    val serverInfo = ServerInfo();
                    serverInfo.port=8090;
                    serverInfo.protocol="http"
                    serverInfo.host_name=""


                    val loginService = LoginServiceImpl(requireContext(),this,serverInfo)
                    val headers = HashMap<String,String>()

                    val loginRequest = LoginRequest.builder().userName(userId).password(password).deviceId(DeviceManager.getInstance(requireContext()).deviceUniqueID).build()
                    loginService.doOnlineLogin(loginRequest,headers)

                }else
                {
                    val login = AuthStore.getInstance(requireContext()).loginInfoFromCache
                    if(login==null){
                        Log.e(TAG,"Intenet connectivity is required for device setup.")
                    }
                    else if(login.userName==userId && login.password==password){
                        Log.e(TAG,"Offline Login Successful")
                    }else{
                        Log.e(TAG,"Offline login failed")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG,"Login Failed")
            }
            //presenter.passwordLogin(loginCredentials)
            viewModel.passwordLogin(loginCredentials)
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
