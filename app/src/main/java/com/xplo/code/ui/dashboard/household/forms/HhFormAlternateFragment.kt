package com.xplo.code.ui.dashboard.household.forms

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.databinding.FragmentHhFormAlternateBinding
import com.xplo.code.ui.dashboard.alternate.AlternateActivity
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.utils.DialogHandler
import com.xplo.code.ui.dashboard.household.list.AlternateSumListAdapter
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.getFullName

import com.xplo.code.BuildConfig
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
class HhFormAlternateFragment : BasicFormFragment(), HouseholdContract.FormAlternateView,
    AlternateSumListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HhFormAlternateFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhFormAlternateFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhFormAlternateFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhFormAlternateBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    private var adapter: AlternateSumListAdapter? = null
    //var REQUEST_CODE = 100

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is HouseholdContract.View) {
            interactor = activity as HouseholdContract.View
        }
    }

    private fun showDialogBox(){
        //val dialog = Dialog(requireContext())
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_reasource)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

       /* val window = dialog.window
        val width = resources.getDimensionPixelSize(R.dimen.dialog_min_width)
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
*/
        val btnOk : Button = dialog.findViewById<Button>(R.id.okButton)
        val btnCancel : Button = dialog.findViewById<Button>(R.id.cancelButton)

        btnOk.setOnClickListener {
            val dataset = adapter?.getDataset()
            ///onValidated(dataset)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(
            TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState"
        )
        binding = FragmentHhFormAlternateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            TAG,
            "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState"
        )

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = RegistrationPresenter(DataRepoImpl())
        //presenter.attach(this)

    }

    override fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = AlternateSumListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter
        onReinstateData(interactor?.getRootForm()?.alternates)
    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.btAdd.setOnClickListener {
            if(binding.recyclerView.adapter?.itemCount!! < 2) {
                onClickAddAlternate()
            }
            else{
/*                val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
                popupView.background = ContextCompat.getDrawable(requireContext(), R.drawable.popup_background)

                val popupWindow = PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )

                popupView.findViewById<Button>(R.id.btnnOk).setOnClickListener {
                    popupWindow.dismiss()
                }

                popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)*/
                val builder = AlertDialog.Builder(requireContext())
                val dialogView = layoutInflater.inflate(R.layout.popup_layout, null)
                builder.setView(dialogView)
                val alertDialog = builder.create()

                dialogView.findViewById<Button>(R.id.btnnOk).setOnClickListener {
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }
        }


        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }

    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Add Alternate")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()

        //onReinstateData(interactor?.getRootForm()?.alternates)

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(forms: ArrayList<AlternateForm>?) {
        Log.d(TAG, "onValidated() called with: form = $forms")
        //showToast(form.toString())

        if (forms != null) {
            val rootForm = interactor?.getRootForm()
            rootForm?.alternates = forms
            interactor?.setRootForm(rootForm)
            Log.d(TAG, "onValidated: $rootForm")
        }

        interactor?.navigateToForm6()
    }

    override fun onReinstateData(forms: ArrayList<AlternateForm>?) {
        Log.d(TAG, "onReinstateData() called with: forms = $forms")
        if (forms == null) return
        adapter?.addAll(forms)
    }

    override fun onClickAddAlternate() {
        Log.d(TAG, "onClickAddAlternate() called")
        //AlternateActivity.openForResult(requireActivity(), null, null, REQUEST_CODE)
        val name = interactor?.getRootForm()?.form2.getFullName()
        AlternateActivity.openForResult(requireActivity(), null, name, activityResultLauncher)

    }

    override fun onGetAnAlternate(form: AlternateForm?) {
        Log.d(TAG, "onGetAnAlternate() called with: form = $form")
        if (form == null) return
        showToast("1 alternate added")
//        Handler(Looper.getMainLooper()).postDelayed({
//            //Do something after 100ms
//            adapter?.addItem(form)
//        }, 500)
        adapter?.addItem(form)
        val rootForm = interactor?.getRootForm()
        if(adapter != null){
            rootForm?.alternates = adapter!!.getDataset()
        }
        interactor?.setRootForm(rootForm)
    }
    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")

        onReadInput()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        val dataset = adapter?.getDataset()
        if (dataset != null) {
            DialogHandler.showNomineeCOnfirmfationDialog(requireContext(),this,dataset)
        }
    }

    override fun onLongClickDataGeneration() {
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isLongClickDGEnabled) return

    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }

    override fun onClickAlternateForm(item: AlternateForm, pos: Int) {
        Log.d(TAG, "onClickAlternateForm() called with: item = $item, pos = $pos")

    }

    override fun onClickAlternateFormDelete(item: AlternateForm, pos: Int) {
        Log.d(TAG, "onClickAlternateFormDelete() called with: item = $item, pos = $pos")
        adapter?.remove(pos)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                Log.d(TAG, "intent: $intent")
                var form = intent?.getSerializableExtra(Bk.KEY_ITEM) as AlternateForm
                //onGetAnAlternate(form)

                Handler(Looper.getMainLooper()).postDelayed({
                    //Do something after 100ms
                    //adapter?.addItem(form)
                    onGetAnAlternate(form)
                }, 500)
            }
        }


}
