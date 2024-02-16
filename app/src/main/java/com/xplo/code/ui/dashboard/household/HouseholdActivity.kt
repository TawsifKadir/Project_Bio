package com.xplo.code.ui.dashboard.household

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.databinding.ActivityHouseholdBinding
import com.xplo.code.ui.dashboard.household.forms.FormDetailsFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm1RegSetupFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm2PerInfoFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm3HhBdFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm4CapPhotoFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm6NomineeFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm5FingerFragment
import com.xplo.code.ui.dashboard.household.forms.HhForm6Nominee2Fragment
import com.xplo.code.ui.dashboard.household.forms.HhFormAlternateFragment
import com.xplo.code.ui.dashboard.household.forms.HhPreviewFragment
import com.xplo.code.ui.dashboard.household.forms.HouseholdHomeFragment
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HouseholdForm
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
class HouseholdActivity : BaseActivity(), HouseholdContract.View {

    companion object {
        private const val TAG = "HouseholdActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent")
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, HouseholdActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityHouseholdBinding
    private val viewModel: HouseholdViewModel by viewModels()
    //private lateinit var toolbar: Toolbar

    private var rootForm: HouseholdForm? = HouseholdForm()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseholdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInitial()
        initView()
        initObserver()


    }

    override fun initInitial() {

    }

    override fun initView() {
        setToolBar()

        val parent = intent.getStringExtra(Bk.KEY_PARENT)
        Log.d(TAG, "initView: parent: $parent")

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }

        navigateToHouseholdHome()

        //navigateToForm6()

    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

//                    is HouseholdViewModel.Event.GetHouseholdItemsSuccess -> {
//                        hideLoading()
//                        //onGetHouseholds(event.items)
//                    }
//
//                    is HouseholdViewModel.Event.GetHouseholdItemsFailure -> {
//                        hideLoading()
//                        //onGetHouseholdsFailure(event.msg)
//                    }


                    else -> Unit
                }
            }
        }

    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Household")

    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
        //presenter.onDetachView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//            }
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateToHouseholdHome() {
        Log.d(TAG, "navigateToHouseholdHome() called")
        STEP = 1
        doFragmentTransaction(
            HouseholdHomeFragment.newInstance(null),
            HouseholdHomeFragment.TAG,
            false,
            true
        )
    }

    override fun navigateToForm1() {
        Log.d(TAG, "navigateToForm1() called")

        // will remove later, dev purpose
        if (TestConfig.isNavHackEnabled){
            navigateToForm6()
            return
        }

        STEP = 1
        doFragmentTransaction(
            HhForm1RegSetupFragment.newInstance(null),
            HhForm1RegSetupFragment.TAG,
            true,
            false
        )

    }

    override fun navigateToForm2() {
        Log.d(TAG, "navigateToForm2() called")
        STEP = 2
        doFragmentTransaction(
            HhForm2PerInfoFragment.newInstance(null),
            HhForm2PerInfoFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm3() {
        Log.d(TAG, "navigateToForm3() called")
        STEP = 3
        doFragmentTransaction(
            HhForm3HhBdFragment.newInstance(null),
            HhForm3HhBdFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm4() {
        Log.d(TAG, "navigateToForm4() called")
        STEP = 4
        doFragmentTransaction(
            HhForm4CapPhotoFragment.newInstance(null),
            HhForm4CapPhotoFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm5() {
        Log.d(TAG, "navigateToForm5() called")
        STEP = 5
        doFragmentTransaction(
            HhForm5FingerFragment.newInstance(null),
            HhForm5FingerFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm6() {
        Log.d(TAG, "navigateToForm6() called")

        if (TestConfig.isNomineeFlow2Enabled) {
            STEP = 6
            doFragmentTransaction(
                HhForm6Nominee2Fragment.newInstance(null),
                HhForm6Nominee2Fragment.TAG,
                true,
                false
            )
            return
        }

        STEP = 6
        doFragmentTransaction(
            HhForm6NomineeFragment.newInstance(null),
            HhForm6NomineeFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToAlternateAddForm() {
        Log.d(TAG, "navigateToAlternateAddForm() called")
        doFragmentTransaction(
            HhFormAlternateFragment.newInstance(null),
            HhFormAlternateFragment.TAG,
            true,
            false
        )
    }


    override fun navigateToPreview() {
        Log.d(TAG, "navigateToPreview() called")
        STEP = 7
        doFragmentTransaction(
            HhPreviewFragment.newInstance(null),
            HhPreviewFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToFormDetails(item: HouseholdItem?) {
        Log.d(TAG, "navigateToFormDetails() called with: item = ${item?.id}")
        doFragmentTransaction(
            FormDetailsFragment.newInstance(null, item),
            FormDetailsFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToAnotherHousehold(hhForm1: HhForm1?) {
        Log.d(TAG, "navigateToAnotherHousehold() called with: hhForm1 = $hhForm1")
        resetRootFormKeepSetup()
        supportFragmentManager.popBackStack(
            HhForm2PerInfoFragment.TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        navigateToForm2()
    }

    override fun onBackButton() {
        Log.d(TAG, "onBackButton() called")
        val entryCount = supportFragmentManager.backStackEntryCount
        Log.d(TAG, "onBackButton: $entryCount")

//        if (entryCount < 1) {
//            //finish()
//            return
//        }

        supportFragmentManager.popBackStack()
    }

    override fun onNextButton() {
        Log.d(TAG, "onNextButton() called")

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

    override fun onPageAdd() {
        Log.d(TAG, "onPageAdd() called")

    }

    override fun getRootForm(): HouseholdForm? {
        Log.d(TAG, "getRootForm() called")
        return rootForm
    }

    override fun setRootForm(form: HouseholdForm?) {
        Log.d(TAG, "setRootForm() called with: form = $form")
        this.rootForm = form
    }

    override fun resetRootForm() {
        Log.d(TAG, "resetRootForm() called")
        if (TestConfig.isNavHackEnabled) return
        this.rootForm = null
        this.rootForm = HouseholdForm()
        Log.d(TAG, "resetRootForm: rootForm: $rootForm")
    }

    override fun resetRootFormKeepSetup() {
        Log.d(TAG, "resetRootFormKeepSetup() called")
        val form1 = rootForm?.form1
        this.rootForm = null
        this.rootForm = HouseholdForm(form1 = form1)
        Log.d(TAG, "resetRootFormKeepSetup: rootForm: $rootForm")
    }

}
