package com.xplo.code.ui.dashboard.household.forms

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.getFullName
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class FormDetailsFragment : BaseFragment(), HouseholdContract.FormDetailsView {

    companion object {
        const val TAG = "FormDetailsFragment"

        @JvmStatic
        fun newInstance(parent: String?, item: HouseholdItem?): FormDetailsFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.id}")
            val fragment = FormDetailsFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putSerializable(Bk.KEY_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentFormDetailsBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    //private var interactor: HouseholdContract.View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }


    override fun initInitial() {
        binding.tvDetails.movementMethod = ScrollingMovementMethod()

    }

    private fun loadImage(url: String) {
        if (url != "") {
            Glide.with(this).load(url)
                .into(this!!.binding.imgPhoto!!)
            binding.imgPhoto!!.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            )
        }

    }
    override fun initView() {

        if (arguments != null) {
            val householdItem = arguments?.getSerializable(Bk.KEY_ITEM) as HouseholdItem
            onGetCompleteData(householdItem)
        }
    }

    override fun initObserver() {


    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Form Details")

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(item: HouseholdItem?) {
        Log.d(TAG, "onGetCompleteData() called with: item = $item")

        binding.tvDetails.text = item.toString()


        //info
        var name =  item?.toHouseholdForm()?.form2.getFullName()
        binding.txtHouseHoldName.text = name
        binding.txtAge.text = item?.toHouseholdForm()?.form2?.age
        binding.txtIdNo.text = item?.toHouseholdForm()?.form2?.idNumber
        binding.txtPhoneNo.text = item?.toHouseholdForm()?.form2?.phoneNumber
        binding.txtGender.text = item?.toHouseholdForm()?.form2?.gender

        binding.txtRT.text = "true"
        binding.txtRI.text = "true"
        binding.txtRM.text = "true"
        binding.txtRR.text = "true"
        binding.txtRL.text = "true"

        binding.txtLT.text = "true"
        binding.txtLI.text = "true"
        binding.txtLM.text = "true"
        binding.txtLR.text = "true"
        binding.txtLL.text = "true"

        loadImage(item?.toHouseholdForm()?.form4?.img ?: "")
    }


}