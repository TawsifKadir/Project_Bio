package com.xplo.code.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.databinding.FragmentDashboardBinding
import com.xplo.code.ui.components.XDialog
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
class DashboardFragment : BaseFragment(), DashboardContract.View {

    companion object {
        private const val TAG = "DashboardFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): DashboardFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = DashboardFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    //private lateinit var presenter: DashboardContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = DashboardPresenter(DataRepoImpl())
        //presenter.attach(this)
    }

    override fun initView() {

    }

    override fun initObserver() {
        binding.viewHousehold.setOnClickListener {
            navigateToHousehold()
        }
        binding.viewAlternate.setOnClickListener {
            navigateToAlternate(null)
        }
        binding.viewReport.setOnClickListener {
            //navigateToReport()
            showAlerter("Not implemented yet", null)
        }
        binding.viewPayment.setOnClickListener {
            navigateToPayment()
            //binding.btTest.callOnClick()
        }

        binding.btTest.setOnClickListener {

//            val builder = AlertDialog.Builder(requireContext())
//            builder.setMessage(getString(R.string.reset_all_msg))
//                .setTitle("hi")
//                .setCancelable(true)
//                .setPositiveButton(getString(R.string.ok)) { _, _ ->
//                    showLoading()
//                }
//                .setNegativeButton(getString(R.string.cancel), null)
//                .create()
//                .show()

//            XDialog.Builder(requireActivity().supportFragmentManager)
//                .setLayoutId(R.layout.custom_dialog_pnn)
//                .setTitle(getString(R.string.dummy_text_small))
//                .setMessage(getString(R.string.dummy_text_big))
//                .setPosButtonText(getString(R.string.confirm))
//                .setNegButtonText(getString(R.string.cancel))
//                .setNeuButtonText(getString(R.string.day))
//                .setCancelable(false)
//                .setListener(object : XDialog.DialogListener {
//                    override fun onClickPositiveButton() {
//
//                    }
//
//                    override fun onClickNegativeButton() {
//
//                    }
//
//                    override fun onClickNeutralButton() {
//
//                    }
//                })
//                .build()
//                .show()

            XDialog.Builder(requireActivity().supportFragmentManager)
                .setLayoutId(R.layout.custom_dialog_pnn)
                .setTitle(getString(R.string.review_complete_reg))
                .setMessage(getString(R.string.review_complete_reg_msg))
                .setPosButtonText("Alternate Registration")
                .setNegButtonText(getString(R.string.home))
                .setNeuButtonText("Household Registration")
                .setThumbId(R.drawable.ic_logo_photo)
                .setCancelable(false)
                .setListener(object : XDialog.DialogListener {
                    override fun onClickPositiveButton() {

                    }

                    override fun onClickNegativeButton() {

                    }

                    override fun onClickNeutralButton() {

                    }
                })
                .build()
                .show()

        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


}
