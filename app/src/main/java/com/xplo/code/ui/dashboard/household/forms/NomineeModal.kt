package com.xplo.code.ui.dashboard.household.forms

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xplo.code.R
import com.xplo.code.databinding.BsdNomineeRootBinding
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.Nominee
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 2022/06/28
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class NomineeModal(builder: Builder) : BottomSheetDialogFragment(), NomineeModalContract.View {

    companion object {
        const val TAG = "NomineeModal"


//        @JvmStatic
//        fun newInstance(): LoginBottomSheet {
//            val fragment = LoginBottomSheet()
//            val bundle = Bundle()
//            fragment.arguments = bundle
//            //bundle.putBoolean(LoginOptionFragment.KEY_IS_SIGNUP, isSignup)
//            return fragment
//        }
    }

    private lateinit var binding: BsdNomineeRootBinding
    private val viewModel: HouseholdViewModel by viewModels()

    private var mBehavior: BottomSheetBehavior<*>? = null
    private var listener: Listener? = null
    private var fm: FragmentManager

    private var parent: String? = null

    init {
        this.fm = builder.fm
        this.listener = builder.listener
        this.parent = builder.parent
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog_Login)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog() called with: savedInstanceState = [$savedInstanceState]")
        //val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = BsdNomineeRootBinding.inflate(LayoutInflater.from(context))
        val view: View = binding.root
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        mBehavior = BottomSheetBehavior.from(view.parent as View)

//        // full screen
//        val parentLayout = dialog.findViewById<View>(
//            com.google.android.material.R.id.design_bottom_sheet
//        )
//        parentLayout?.let { bottomSheet ->
//            val behaviour = BottomSheetBehavior.from(bottomSheet)
//            val layoutParams = bottomSheet.layoutParams
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//            bottomSheet.layoutParams = layoutParams
//            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        initInitial()
        initView()
        initObserver()

        return dialog
    }


    private fun initInitial() {
        listener?.onNomineeModalOpen()

//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(this, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d(TAG, "handleOnBackPressed() called")
//                    // Do custom work here
//
//                    // if you want onBackPressed() to be called as normal afterwards
////                    if (isEnabled) {
////                        isEnabled = false
////                        requireActivity().onBackPressed()
////                    }
//                }
//            }
//            )

    }

    private fun initView() {

        navigateToFirstPage()

    }

    private fun initObserver() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         */
        mBehavior?.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d(
                            TAG,
                            "onStateChanged() called with: bottomSheet = $bottomSheet, newState = $newState"
                        )
                        onCloseDialog()
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.bsdLoginTopBar.btBack.setOnClickListener {
            onBackButton()
        }
        binding.bsdLoginTopBar.btCancel.setOnClickListener {
            onCrossButton()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

                    else -> Unit
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onResume() {
        super.onResume()
        //logPage(Screens.PAGE_LOGIN, null)

//        if (view == null) return
//        view?.isFocusableInTouchMode = true
//        view?.requestFocus()
//        view?.setOnKeyListener { v, keyCode, event ->
//
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                Log.d(TAG, "onResumeBack() called with: v = $v, keyCode = $keyCode, event = $event")
//                // handle back button's click listener
//                onBackButton()
//                true
//            } else false
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
        listener?.onNomineeModalClose()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    override fun showLoading() {
        binding.bsdLoginTopBar.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.bsdLoginTopBar.progressBar.visibility = View.GONE
    }

    override fun showBackButton() {
        binding.bsdLoginTopBar.btBack.visibility = View.VISIBLE
    }

    override fun hideBackButton() {
        binding.bsdLoginTopBar.btBack.visibility = View.INVISIBLE
    }

    override fun showCrossButton() {
        binding.bsdLoginTopBar.btCancel.visibility = View.VISIBLE
    }

    override fun hideCrossButton() {
        binding.bsdLoginTopBar.btCancel.visibility = View.INVISIBLE
    }

    override fun onBackButton() {
        val entryCount = childFragmentManager.backStackEntryCount
        Log.d(TAG, "onBackButton: $entryCount")

        if (entryCount <= 1) {
            onCloseDialog()
            return
        }

        childFragmentManager.popBackStack()
    }

    override fun onCrossButton() {
        onCloseDialog()
    }

    override fun onCloseDialog() {
        Log.d(TAG, "onCloseDialog() called")
        dismiss()
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
            try {
                childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame2, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        if (!childFragmentManager.isStateSaved && !childFragmentManager.isDestroyed) {
            transaction.commitAllowingStateLoss()
        }
        onPageAdd()
    }

    override fun onPageAdd() {
        Log.d(TAG, "onPageAdd() called")
        showBackButton()
//        val entryCount = childFragmentManager.backStackEntryCount
//        Log.d(TAG, "onPageAddOrRemove: $entryCount")
//        if (entryCount < 1) {
//            hideBackButton()
//        } else {
//            showBackButton()
//        }
    }

    override fun navigateToFirstPage() {
        Log.d(TAG, "navigateToFirstPage() called")

        doFragmentTransaction(
            NomineeInputFragment.newInstance(null),
            NomineeInputFragment.TAG,
            true,
            false
        )

    }

    override fun onCompleteModal(item: Nominee?) {
        Log.d(TAG, "onCompleteModal() called with: item = $item")
        listener?.onNomineeModalNomineeInputSuccess(item)
        onCloseDialog()
    }


    fun show() {
        if (!fm.isDestroyed) {
            this.show(fm, TAG)
        }
    }

//    fun setListener(listener: Listener?) {
//        this.listener = listener
//    }


    /**
     * Implement in base activity and fragment
     */
    interface Listener {
        fun onNomineeModalOpen()
        fun onNomineeModalClose()
        fun onNomineeModalNomineeInputSuccess(item: Nominee?)
        fun onNomineeModalNomineeInputFailure(msg: String?)
    }

    class Builder(var fm: FragmentManager) {

        var listener: Listener? = null
        var parent: String? = null

        fun listener(listener: Listener?) = apply { this.listener = listener }

        fun parent(parent: String?) = apply { this.parent = parent }

        fun build() = NomineeModal(this)

    }


}