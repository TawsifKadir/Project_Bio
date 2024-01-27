package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
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
import com.xplo.code.ui.dashboard.model.toJson
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
            binding.txtCountry.text = rootForm?.form1?.countryName ?: ""
            binding.txtState.text = rootForm?.form1?.stateName ?: ""
            binding.txtBoma.text = rootForm?.form1?.bomaName ?: ""
            binding.txtPayam.text = rootForm?.form1?.payamName ?: ""
        //HouseHold
            binding.txtFirstName.text = rootForm?.form2?.name ?: ""
            binding.txtMiddleName.text = rootForm?.form2?.name ?: ""
            binding.txtLastName.text = rootForm?.form2?.name ?: ""

            binding.txtAge.text = (rootForm?.form2?.age ?: "").toString()
            binding.txtId.text = rootForm?.form2?.idNumber ?: ""
            binding.txtPhoneNo.text = rootForm?.form2?.phoneNumber ?: ""

            binding.txtSourceOfIncome.text = rootForm?.form2?.mainSourceOfIncome ?: ""
            binding.txtAvgIncome.text = (rootForm?.form2?.monthlyAverageIncome ?: "").toString()
            binding.txtLegalStatus.text = rootForm?.form2?.legalStatus ?: ""
            binding.txtGender.text = rootForm?.form2?.gender ?: ""
            binding.txtMeritalStatus.text = rootForm?.form2?.maritalStatus ?: ""
            binding.txtSpouseName.text = rootForm?.form2?.spouseName ?: ""


            binding.txtHouseHoldSize.text = (rootForm?.form3?.householdSize ?: "").toString()
            binding.txtMaleDepen.text = (rootForm?.form3?.maleHouseholdMembers ?: "").toString()
            binding.txtFemaleDepen.text = (rootForm?.form3?.femaleHouseholdMembers ?: "").toString()
            binding.txt05YearsOld.text = (rootForm?.form3?.householdMembers0_2 ?: "").toString()
            binding.txt617YearsOld.text = (rootForm?.form3?.householdMembers18_35 ?: "").toString()
            binding.txt1845YearsOld.text = (rootForm?.form3?.householdMembers18_35 ?: "").toString()
            binding.txt4665YearsOld.text = (rootForm?.form3?.householdMembers46_64 ?: "").toString()
            binding.txt66YearsOld.text = (rootForm?.form3?.householdMembers65andAbove ?: "").toString()
            //binding.txtSelectionCriteria.text = rootForm?.form3?. ?: ""
            //binding.txtSelectionReason.text = rootForm?.form3?.phoneNumber ?: ""

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

        loadImage(rootForm?.form4?.img ?: "")
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


        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.review_complete_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText(getString(R.string.alternate_reg))
            .setNegButtonText(getString(R.string.cancel))
            .setNeuButtonText(getString(R.string.household_reg))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToAlternate()
                }

                override fun onClickNegativeButton() {
                    
                }
                override fun onClickNeutralButton() {
                    interactor?.navigateToAlternate()
                }
            })
            .build()
            .show()
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
    private fun loadImage(url: String) {
        if(url != ""){
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

}
