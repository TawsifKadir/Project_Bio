package com.xplo.code.ui.login.model


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.data_module.model.user.ResetPassRsp
import com.xplo.code.databinding.FragmentResetBinding
import com.xplo.code.ui.login.LoginContract
import com.xplo.code.ui.login.LoginViewModel
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
class ResetFragment : BaseFragment(), LoginContract.ResetPasswordView {

    companion object {
        const val TAG = "ResetFragment"

        @JvmStatic
        fun newInstance(
            parent: String?,
            userId: String?
        ): ResetFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, userId = $userId")
            val fragment = ResetFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentResetBinding
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRetypePassword: EditText
    private lateinit var btResetPassword: Button
    private val viewModel: LoginViewModel by viewModels()

    private var interactor: LoginContract.View? = null

    private var userId: String? = null

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
        binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        etUserName = binding.etUserId
        etPassword = binding.etPassword
        etRetypePassword = binding.etRetypePassword
        btResetPassword = binding.btResetPassword
    }

    override fun initView() {

        arguments.let {
            userId = it?.getString(Bk.KEY_ID)
        }
        etUserName.setText(userId)
        etUserName.isEnabled = false
    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is LoginViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is LoginViewModel.Event.ResetPasswordSuccess -> {
                        hideLoading()
                        onResetPasswordSuccess(event.rsp)
                        viewModel.clearEvent()
                    }

                    is LoginViewModel.Event.ResetPasswordFailure -> {
                        hideLoading()
                        onResetPasswordFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }
        btResetPassword.setOnClickListener {

           onClickResetPassword()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Reset Password")
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun backToLogin() {
        interactor?.navigateToLogin()
    }

    override fun onClickResetPassword() {
        Log.d(TAG, "onClickResetPassword() called")

        val password = etPassword.text.toString()
        val retypePassword = etRetypePassword.text.toString()
        if (password.isEmpty()) return

        //val userId = getPrefHelper().getUserId()
        if (password == retypePassword){
            viewModel.resetPassword(requireContext(), userId, password)
        }else {
            showAlerter(null, "Password didn't match")
        }


    }

    override fun onResetPasswordSuccess(rsp: ResetPassRsp?) {
        Log.d(TAG, "onResetPasswordSuccess() called with: rsp = $rsp")
        if (rsp == null) return
        if (rsp.token == null) return

        showMessage("Reset Password Success")
        getPrefHelper().setAccessToken(rsp.token)
        navigateToHome()
        requireActivity().finish()

    }

    override fun onResetPasswordFailure(msg: String?) {
        Log.d(TAG, "onResetPasswordFailure() called with: msg = $msg")
        showMessage("Reset Password Failure")
    }


}
