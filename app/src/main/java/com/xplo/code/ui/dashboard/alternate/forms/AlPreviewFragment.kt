package com.xplo.code.ui.dashboard.alternate.forms

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
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentAlPreviewBinding
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.alternate.AlternateViewModel
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlternateForm
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
    private val viewModel: HouseholdViewModel by viewModels()

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
        val rootForm = interactor?.getRootForm()

        //info
        binding.txtHouseHoldName.text = rootForm?.form1?.householdName ?: ""
        binding.txtAltName.text = rootForm?.form1?.alternateName ?: ""
        binding.txtAge.text = rootForm?.form1?.age ?: ""
        binding.txtIdNo.text = rootForm?.form1?.idNumber ?: ""
        binding.txtPhoneNo.text = rootForm?.form1?.phoneNumber ?: ""
        binding.txtAltRelation.text = rootForm?.form1?.selectAlternateRlt ?: ""
        binding.txtGender.text = rootForm?.form1?.gender ?: ""

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

        loadImage(rootForm?.form2?.img ?: "")

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


    override fun onValidated(form: AlternateForm) {
        Log.d(TAG, "onValidated() called with: form = $form")

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.alternate_reg))
            .setMessage(getString(R.string.alternate_details))
            .setPosButtonText(getString(R.string.next))
            .setNegButtonText(getString(R.string.cancel))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToForm3()
                }

                override fun onClickNegativeButton() {
                }

                override fun onClickNeutralButton() {
                }
            })
            .build()
            .show()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

    }

    override fun onLongClickDataGeneration() {

    }

    override fun onGenerateDummyInput() {

    }

    override fun onPopulateView() {

    }


}
