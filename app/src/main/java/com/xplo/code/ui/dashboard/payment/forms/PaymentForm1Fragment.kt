package com.xplo.code.ui.dashboard.payment.forms


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentPaymentForm1Binding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.household.list.CheckboxListAdapter
import com.xplo.code.ui.dashboard.household.list.HouseholdListAdapter
import com.xplo.code.ui.dashboard.model.CheckboxItem
import com.xplo.code.ui.dashboard.payment.PaymentContract
import com.xplo.code.ui.dashboard.payment.PaymentViewModel
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
class PaymentForm1Fragment : BaseFragment(), PaymentContract.Form1View, CheckboxListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "PaymentForm1Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): PaymentForm1Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = PaymentForm1Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentPaymentForm1Binding
    private val viewModel: PaymentViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: PaymentContract.View? = null

    private var adapter: CheckboxListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is PaymentContract.View) {
            interactor = activity as PaymentContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentForm1Binding.inflate(inflater, container, false)
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


        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        adapter = CheckboxListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        adapter?.addAll(UiData.getReason())

    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

    }

    override fun onPause() {
        super.onPause()
        //EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        //EventBus.getDefault().register(this)
        setToolbarTitle("Payment step 1")

        binding.viewButtonBackNext.btBack.gone()
        binding.viewButtonBackNext.btNext.visible()


    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEventButtonAction(event: EventButtonAction?) {
//        Log.d(TAG, "onEventContentClick() called with: event = $event")
//        if (event == null) return
//
//        when (event.buttonAction) {
//            ButtonAction.BACK -> onBackButton()
//            ButtonAction.NEXT -> onNextButton()
//            ButtonAction.SUBMIT -> onSubmitButton()
//            else -> {}
//        }
//
//    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToForm2()
        
        var txt = adapter?.getCheckedItems().toString()
        Log.d(TAG, "onClickNextButton: $txt")

    }

    override fun onStatusChangeCheckboxItem(item: CheckboxItem, pos: Int, isChecked: Boolean) {
        Log.d(
            TAG,
            "onStatusChangeCheckboxItem() called with: item = $item, pos = $pos, isChecked = $isChecked"
        )

    }


}
