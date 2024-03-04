package com.xplo.code.ui.dashboard.alternate.forms


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kit.integrationmanager.model.Login
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
import com.xplo.code.databinding.FragmentSignupBinding
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
class SignUpFragment : BaseFragment(), LoginContract.SignUpView{

    companion object {
        const val TAG = "LoginFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): SignUpFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = SignUpFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: LoginViewModel by viewModels()

    private var interactor: LoginContract.View? = null
    private lateinit var btnBack: Button
    private lateinit var btnSignUp: Button

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
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }
    override fun initInitial() {
        btnBack = binding.viewButtonBackNext.btBack
        btnSignUp = binding.viewButtonBackNext.btNext
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        btnSignUp.text = "Sign Up"
    }
    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is LoginViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is LoginViewModel.Event.LoginSuccess -> {
                        hideLoading()
//                        onLoginSuccess(event.token!!, event.id)
                        viewModel.clearEvent()
                    }

                    is LoginViewModel.Event.LoginFailure -> {
                        hideLoading()
//                        onLoginFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }

        btnBack.setOnClickListener {
            navigateToLogin()
        }

    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Device Registration")
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


    override fun backToLogin() {
        TODO("Not yet implemented")
    }

    override fun onSignUpSuccess(id: String?) {
        TODO("Not yet implemented")
    }

    override fun onSignUpFailure(msg: String?) {
        TODO("Not yet implemented")
    }

}
