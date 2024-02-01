package com.xplo.code.ui.dashboard.alternate.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.isActivityFinishing
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentAlForm3Binding
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.alternate.AlternateViewModel
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlForm3Fragment : BasicFormFragment(), AlternateContract.Form3View {

    companion object {
        const val TAG = "AlForm3Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlForm3Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlForm3Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlForm3Binding
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
        binding = FragmentAlForm3Binding.inflate(inflater, container, false)
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

    override fun onValidated(form: AlForm3?) {
        TODO("Not yet implemented")
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()
        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.fingerprint_enroll_title))
            .setMessage(getString(R.string.fingerprint_enroll_msg))
            .setPosButtonText(getString(R.string.household_reg))
            .setNegButtonText(getString(R.string.home))
            .setNeuButtonText(getString(R.string.alternate_reg_title))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToHousehold()
                }

                override fun onClickNegativeButton() {

                    interactor?.navigateToHome()
                }

                override fun onClickNeutralButton() {
                    //interactor?.navigateToAlternate(id)
                }
            })
            .build()
            .show()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")
    }

    override fun onLongClickDataGeneration() {
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isLongClickDGEnabled) return

        binding.viewButtonBackNext.btNext.setOnLongClickListener {
            onGenerateDummyInput()
            return@setOnLongClickListener true
        }
    }

    override fun onGenerateDummyInput() {
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

    }

    override fun onPopulateView() {
        TODO("Not yet implemented")
    }


}
