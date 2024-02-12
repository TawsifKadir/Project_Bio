package com.xplo.code.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
//import android.text.method.HideReturnsTransformationMethod
//import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.data.Constants
import com.xplo.code.databinding.ActivityLoginBinding
import com.xplo.code.ui.login.model.LoginCredentials
import com.xplo.code.ui.testing_lab.LabActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), LoginContract.View {

    companion object {
        private const val TAG = "LoginActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent")
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    //private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            //presenter.passwordLogin(loginCredentials)
            viewModel.passwordLogin(loginCredentials)
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

        taskDev()


    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
    }

    override fun navigateToNext() {

    }

    override fun navigateToSignup() {

    }

    override fun navigateToOtpLogin() {
        Log.d(TAG, "navigateToOtpLogin: ")

    }

    override fun onLoginSuccess(token: String, id: String?) {
        Log.d(TAG, "onLoginSuccess() called with: token = $token, id = $id")
        getPrefHelper().setAccessToken(token)
        getPrefHelper().setUserId("abc07")
        navigateToHome()
        finish()

    }

    override fun onLoginFailure(msg: String?) {
        Log.d(TAG, "onLoginFailure() called with: msg = [$msg]")
        showErrorMessage(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(
            TAG,
            "onActivityResult() called with: requestCode = $requestCode, resultCode = $resultCode, data = $data"
        )

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
}
