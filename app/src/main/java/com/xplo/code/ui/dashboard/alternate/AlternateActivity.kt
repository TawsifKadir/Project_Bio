package com.xplo.code.ui.dashboard.alternate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.databinding.ActivityAlternateBinding
import com.xplo.code.ui.dashboard.alternate.forms.AlForm1Fragment
import com.xplo.code.ui.dashboard.alternate.forms.AlForm2Fragment
import com.xplo.code.ui.dashboard.alternate.forms.AlForm3Fragment
import com.xplo.code.ui.dashboard.alternate.forms.AlPreviewFragment
import com.xplo.code.ui.dashboard.alternate.forms.AlternateHomeFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.FormDetailsFragment
import com.xplo.code.ui.dashboard.model.AlternateForm
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
class AlternateActivity : BaseActivity(), AlternateContract.View {

    companion object {
        private const val TAG = "AlternateActivity"

        @JvmStatic
        fun open(context: Context, parent: String?, id: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent, id = $id")
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            val intent = Intent(context, AlternateActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun openNew(context: Context, parent: String?, id: String?, hhName: String, type: String) {
            Log.d(
                TAG,
                "openNew() called with: context = $context, parent = $parent, id = $id, hhName = $hhName, type = $type"
            )
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            bundle.putString(Bk.HH_NAMME, hhName)
            bundle.putString(Bk.HH_TYPE, type)
            val intent = Intent(context, AlternateActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun openForResultLegacy(context: Activity, parent: String?, id: String?, rqCode: Int) {
            Log.d(
                TAG,
                "openForResultLegacy() called with: context = $context, parent = $parent, id = $id, rqCode = $rqCode"
            )
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            bundle.putInt(Bk.KEY_REQUEST_CODE, rqCode)
            val intent = Intent(context, AlternateActivity::class.java)
            intent.putExtras(bundle)
            context.startActivityForResult(intent, rqCode)
        }

        @JvmStatic
        fun openForResult(
            context: Activity,
            parent: String?,
            id: String?,
            activityResultLauncher: ActivityResultLauncher<Intent>
        ) {
            Log.d(
                TAG,
                "openForResult() called with: context = $context, parent = $parent, id = $id, activityResultLauncher = $activityResultLauncher"
            )
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            bundle.putInt(
                Bk.KEY_REQUEST_CODE,
                100
            )     // just to check will activity open for a result
            val intent = Intent(context, AlternateActivity::class.java)
            intent.putExtras(bundle)
            activityResultLauncher.launch(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityAlternateBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var toolbar: Toolbar
    private lateinit var householdItem: HouseholdItem

    private var rootForm: AlternateForm? = AlternateForm()

    //private var callForResult = false
    private var REQUEST_CODE: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlternateBinding.inflate(layoutInflater)
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
        val id = intent.getStringExtra(Bk.KEY_ID)
        val hhName = intent.getStringExtra(Bk.HH_NAMME)
        val hhType = intent.getStringExtra(Bk.HH_TYPE)
        REQUEST_CODE = intent.getIntExtra(Bk.KEY_REQUEST_CODE, -1)
        rootForm?.appId = id
        rootForm?.hhType = hhType
        Log.d(TAG, "initView: parent: $parent")

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }

        if (id != null) {
            navigateToForm1(id, hhName, hhType, false, true)
        } else {
            navigateToAlternateHome()
        }


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

//                    is HouseholdViewModel.Event.GetItemSuccess -> {
//                        hideLoading()
//                        //onGetAlternates(event.items)
//                    }
//
//                    is HouseholdViewModel.Event.GetItemFailure -> {
//                        hideLoading()
//                        //onGetAlternatesFailure(event.msg)
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

    override fun navigateToAlternateHome() {
        Log.d(TAG, "navigateToAlternateHome() called")
        doFragmentTransaction(
            AlternateHomeFragment.newInstance(null),
            AlternateHomeFragment.TAG,
            false,
            true
        )
    }

    override fun navigateToForm1(
        id: String?,
        hhName: String?,
        type: String?,
        addToBackStack: Boolean,
        clearBackStack: Boolean
    ) {
        Log.d(TAG, "navigateToForm1() called with: id = $id")

        STEP = 1

        doFragmentTransaction(
            AlForm1Fragment.newInstance(null, id,hhName),
            AlForm1Fragment.TAG,
            addToBackStack,
            clearBackStack
        )


    }

    override fun navigateToForm2() {
        Log.d(TAG, "navigateToForm2() called")
        STEP = 2
        doFragmentTransaction(
            AlForm2Fragment.newInstance(null),
            AlForm2Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToForm3() {
        Log.d(TAG, "navigateToForm3() called")
        STEP = 3
        doFragmentTransaction(
            AlForm3Fragment.newInstance(null),
            AlForm3Fragment.TAG,
            true,
            false
        )
    }

    override fun navigateToPreview() {
        Log.d(TAG, "navigateToPreview() called")
        STEP = 5
        doFragmentTransaction(
            AlPreviewFragment.newInstance(null),
            AlPreviewFragment.TAG,
            true,
            false
        )
    }

    override fun navigateToFormDetails(item: HouseholdItem?) {
        Log.d(TAG, "navigateToFormDetails() called with: item = $item")
        doFragmentTransaction(
            FormDetailsFragment.newInstance(null, item),
            FormDetailsFragment.TAG,
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

    override fun getRootForm(): AlternateForm? {
        return rootForm
    }

    override fun setRootForm(form: AlternateForm?) {
        this.rootForm = form
    }

//    override fun getHouseholdItem(): HouseholdItem? {
//        return this.householdItem
//    }

    override fun setHouseholdItem(item: HouseholdItem?) {
        if (item == null) return
        this.householdItem = item
    }

    override fun getRequestCode(): Int {
        return this.REQUEST_CODE
    }

    override fun isCallForResult(): Boolean {
        return this.REQUEST_CODE > 0
    }
}
