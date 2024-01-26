package com.xplo.code.ui.dashboard.alternate.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentAlPreviewBinding
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.alternate.AlternateViewModel
import com.xplo.code.ui.dashboard.base.BasicFormFragment
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
class AlPreviewFragment : BasicFormFragment(), AlternateContract.PreviewView {

    companion object {
        const val TAG = "AlPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlPreviewBinding
    private val viewModel: AlternateViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AlternateContract.View) {
            interactor = activity as AlternateContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = RegistrationPresenter(DataRepoImpl())
        //presenter.attach(this)
    }

    override fun initView() {

    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Alternate Fingerprints")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Submit"
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()
    }


}
