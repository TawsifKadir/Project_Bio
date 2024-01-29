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
import com.xplo.code.databinding.ActivityHouseholdBinding
import com.xplo.code.ui.dashboard.household.forms.HhForm1Fragment
import com.xplo.code.ui.dashboard.household.forms.HhForm2Fragment
import com.xplo.code.ui.dashboard.household.forms.HhForm3Fragment
import com.xplo.code.ui.dashboard.household.forms.HhForm4Fragment
import com.xplo.code.ui.dashboard.household.forms.HhForm5Fragment
import com.xplo.code.ui.dashboard.household.forms.HhForm6Fragment
import com.xplo.code.ui.dashboard.household.forms.HhPreviewFragment
import com.xplo.code.ui.dashboard.household.forms.HouseholdHomeFragment
import com.xplo.code.ui.dashboard.household.list.HouseholdListFragment
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

    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemSuccess -> {
                        hideLoading()
                        //onGetHouseholds(event.items)
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemFailure -> {
                        hideLoading()
                        //onGetHouseholdsFailure(event.msg)
                    }


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

        STEP = 1
        doFragmentTransaction(
            HhForm1Fragment.newInstance(null),
            HhForm1Fragment.TAG,
            true,
            false
        )

    }

    override fun navigateToForm2() {
        Log.d(TAG, "navigateToForm2() called")
        STEP = 2
        doFragmentTransaction(
            HhForm2Fragment.newInstance(null),
            HhForm2Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm3() {
        Log.d(TAG, "navigateToForm3() called")
        STEP = 3
        doFragmentTransaction(
            HhForm3Fragment.newInstance(null),
            HhForm3Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm4() {
        Log.d(TAG, "navigateToForm4() called")
        STEP = 4
        doFragmentTransaction(
            HhForm4Fragment.newInstance(null),
            HhForm4Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm5() {
        Log.d(TAG, "navigateToForm5() called")
        STEP = 5
        doFragmentTransaction(
            HhForm5Fragment.newInstance(null),
            HhForm5Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm6() {
        Log.d(TAG, "navigateToForm6() called")
        STEP = 6
        doFragmentTransaction(
            HhForm6Fragment.newInstance(null),
            HhForm6Fragment.TAG,
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

}
