package com.xplo.code.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
//import android.text.method.HideReturnsTransformationMethod
//import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityLoginBinding
import com.xplo.code.ui.login.model.LoginFragment
import com.xplo.code.ui.login.model.ResetFragment
import com.xplo.code.ui.dashboard.alternate.forms.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
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
        navigateToLogin()
    }

    override fun initObserver() {

    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
    }

    override fun doFragmentTransaction(
        fragment: Fragment,
        tag: String,
        addToBackStack: Boolean,
        clearBackStack: Boolean
    ) {
        Log.d(
            TAG,
            "doFragmentTransaction() called with: fragment = $fragment, tag = $tag, addToBackStack = $addToBackStack, clearBackStack = $clearBackStack"
        )

        if (clearBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()

    }

    override fun navigateToLogin() {
        doFragmentTransaction(LoginFragment.newInstance(null), LoginFragment.TAG,false,true)
    }

    override fun navigateToSignUp() {
        doFragmentTransaction(SignUpFragment.newInstance(null),SignUpFragment.TAG,true,false)
    }

    override fun navigateToReset(userId: String?) {
        doFragmentTransaction(ResetFragment.newInstance(null, userId), ResetFragment.TAG,false,true)
    }


    override fun onLoginSuccess(token: String, id: String?) {
        Log.d(TAG, "onLoginSuccess() called with: token = $token, id = $id")

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

}
