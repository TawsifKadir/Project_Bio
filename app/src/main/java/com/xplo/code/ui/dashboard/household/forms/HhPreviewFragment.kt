package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhPreviewBinding
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.data.BuildConfig
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
class HhPreviewFragment : BaseFragment(), HouseholdContract.PreviewView {

    companion object {
        const val TAG = "HhPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhPreviewBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is HouseholdContract.View) {
            interactor = activity as HouseholdContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHhPreviewBinding.inflate(inflater, container, false)
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
        val rootForm = interactor?.getRootForm()
        Log.d(TAG, "initView: $rootForm")
        //binding.tvDetails.text = rootForm.toJson()

        //Address
        binding.tvCountry.text = rootForm?.form1?.countryName ?: ""
        binding.tvState.text = rootForm?.form1?.stateName ?: ""
        binding.tvBoma.text = rootForm?.form1?.bomaName ?: ""
        binding.tvPayam.text = rootForm?.form1?.payamName ?: ""
        //HouseHold
        binding.tvFirstName.text = rootForm?.form2?.firstName ?: ""
        binding.tvMiddleName.text = rootForm?.form2?.middleName ?: ""
        binding.tvLastName.text = rootForm?.form2?.lastName ?: ""

        binding.tvAge.text = (rootForm?.form2?.age ?: "").toString()
        binding.tvId.text = rootForm?.form2?.idNumber ?: ""
        binding.tvPhoneNo.text = rootForm?.form2?.phoneNumber ?: ""

        binding.tvSourceOfIncome.text = rootForm?.form2?.mainSourceOfIncome ?: ""
        binding.tvAvgIncome.text = (rootForm?.form2?.monthlyAverageIncome ?: "").toString()
        binding.tvLegalStatus.text = rootForm?.form2?.legalStatus ?: ""
        binding.tvGender.text = rootForm?.form2?.gender ?: ""
        binding.tvMeritalStatus.text = rootForm?.form2?.maritalStatus ?: ""
        binding.tvSpouseName.text = rootForm?.form2?.spouseName ?: ""


        binding.tvHouseHoldSize.text = (rootForm?.form3?.householdSize ?: "").toString()
        binding.tvMaleDepen.text = (rootForm?.form3?.maleHouseholdMembers ?: "").toString()
        binding.tvFemaleDepen.text = (rootForm?.form3?.femaleHouseholdMembers ?: "").toString()
        binding.tv05YearsOld.text = (rootForm?.form3?.householdMembers0_2 ?: "").toString()
        binding.tv617YearsOld.text = (rootForm?.form3?.householdMembers18_35 ?: "").toString()
        binding.tv1845YearsOld.text = (rootForm?.form3?.householdMembers18_35 ?: "").toString()
        binding.tv4665YearsOld.text = (rootForm?.form3?.householdMembers46_64 ?: "").toString()
        binding.tv66YearsOld.text = (rootForm?.form3?.householdMembers65andAbove ?: "").toString()
        //binding.tvSelectionCriteria.text = rootForm?.form3?. ?: ""
        //binding.tvSelectionReason.text = rootForm?.form3?.phoneNumber ?: ""

        binding.tvRT.text = "true"
        binding.tvRI.text = "true"
        binding.tvRM.text = "true"
        binding.tvRR.text = "true"
        binding.tvRL.text = "true"

        binding.tvLT.text = "true"
        binding.tvLI.text = "true"
        binding.tvLM.text = "true"
        binding.tvLR.text = "true"
        binding.tvLL.text = "true"

        loadImage(rootForm?.form4?.img)
    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.SaveHouseholdFormSuccess -> {
                        hideLoading()
                        onSaveSuccess(event.id)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SaveHouseholdFormFailure -> {
                        hideLoading()
                        onSaveFailure(event.msg)
                        viewModel.clearEvent()
                    }


                    else -> Unit
                }
            }
        }


        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Preview")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Submit"
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(data: HouseholdForm?) {
        Log.d(TAG, "onGetCompleteData() called with: data = $data")
    }

    override fun onSaveSuccess(id: String?) {
        Log.d(TAG, "onSaveSuccess() called with: id = $id")

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.review_complete_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText("Alternate")
            .setNegButtonText(getString(R.string.home))
            .setNeuButtonText("Household")
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToAlternate(id)
                }

                override fun onClickNegativeButton() {
                    requireActivity().finish()
                    //navigateToHome()
                }

                override fun onClickNeutralButton() {
                    interactor?.navigateToHousehold()
                }
            })
            .build()
            .show()
    }

    override fun onSaveFailure(msg: String?) {
        Log.d(TAG, "onSaveFailure() called with: msg = $msg")
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()

        val rootForm = interactor?.getRootForm()

//        //Gson().fromJson(jsonTxt, ApiError::class.java).message
//        val jsonTxt = Gson().toJson(rootForm)
//
//        // save to room
//        val dao = DbController.getAppDb().householdDao()
//
//        val householdItem = HouseholdItem(data = jsonTxt)
//        dao.insert(householdItem)

        viewModel.saveHouseholdForm(rootForm)


        val name = interactor?.getRootForm()?.form2.getFullName()



    }

    override fun onReadInput() {
        Log.d(TAG, "onValidation() called")
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
    }

    private fun loadImage(url: String?) {
        if (url.isNullOrEmpty()) return

        Glide.with(this)
            .load(url)
            .into(this.binding.imgPhoto)

        binding.imgPhoto.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

    }

}
