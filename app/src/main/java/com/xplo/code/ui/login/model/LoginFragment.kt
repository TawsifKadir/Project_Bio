package com.xplo.code.ui.login.model


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.Contextor
import com.xplo.code.core.utils.NetUtils
import com.xplo.code.data.Constants
import com.xplo.code.data_module.core.Config
import com.xplo.code.databinding.FragmentLoginBinding
import com.xplo.code.ui.login.LoginContract
import com.xplo.code.ui.login.LoginViewModel
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
        taskDev()
    }


    override fun initObserver() {
        binding.btLogin.setOnClickListener {
            onClickLogin()
        }

        binding.btSignup.setOnClickListener {
            navigateToSignup()
        }

        binding.btResetPass.setOnClickListener {
            navigateToResetPassword(null)
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
                        onLoginSuccess(event.token, event.username)
                        viewModel.clearEvent()
                    }

                    is LoginViewModel.Event.LoginPending -> {
                        hideLoading()
                        onLoginPending(event.token, event.username)
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

    override fun navigateToResetPassword(userId: String?) {
        interactor?.navigateToReset(userId)
    }

    override fun onClickLogin() {
        Log.d(TAG, "onClickLogin() called")

        val userId = binding.etUserId.text.toString()
        val password = binding.etPassword.text.toString()
        val credentials = LoginCredentials(userId, password)

        if (userId.isEmpty() || password.isEmpty()) {
            showAlerter(null, "Enter valid login credentials")
            return
        }

        if (!NetUtils.isOnline(Contextor.getInstance().context)) {
            onOfflineLogin(credentials)
            return
        }

        onNormalLogin(credentials)


    }

    override fun onNormalLogin(credentials: LoginCredentials) {
        Log.d(TAG, "onNormalLogin() called with: credentials = $credentials")
        viewModel.passwordLogin(requireContext(), credentials)
    }

    override fun onOfflineLogin(credentials: LoginCredentials) {
        Log.d(TAG, "onOfflineLogin() called with: credentials = $credentials")
        viewModel.passwordLoginOffline(requireContext(), credentials)
    }

    override fun onLoginWithSdk(credentials: LoginCredentials) {
        Log.d(TAG, "onLoginWithSdk() called with: credentials = $credentials")
        viewModel.passwordLoginwithSDK(credentials, requireContext())
    }

    override fun onLoginSuccess(token: String, username: String?) {
        Log.d(TAG, "onLoginSuccess() called with: token = $token, id = $username")
        getPrefHelper().setAccessToken(token)
        getPrefHelper().setUserId(username)
        navigateToHome()
        requireActivity().finish()
    }

    override fun onLoginPending(token: String, username: String?) {
        Log.d(TAG, "onLoginPending() called with: token = $token, id = $username")
        Config.ACCESS_TOKEN = token
        //getPrefHelper().setUserId(username)
        navigateToResetPassword(username)
    }

    override fun onLoginFailure(msg: String?) {
        Log.d(TAG, "onLoginFailure() called with: msg = [$msg]")
        showErrorMessage(msg)
    }

    private fun taskDev() {
        if (!isDebugBuild()) return

//        if (TestConfig.isTestLoginEnabled) {
//            binding.etUserId.setText(Constants.TEST_USER_ID)
//            binding.etPassword.setText(Constants.TEST_PASSWORD)
//        }

        binding.ivBannar.setOnLongClickListener {
            //openActivity(LabActivity::class.java, null)
            binding.etUserId.setText(Constants.TEST_USER_ID)
            binding.etPassword.setText(Constants.TEST_PASSWORD)
            return@setOnLongClickListener true
        }

    }

    override fun update(o: java.util.Observable?, arg: Any?) {
        Log.d(TAG, "update() called with: o = $o, arg = $arg")

    }

}
