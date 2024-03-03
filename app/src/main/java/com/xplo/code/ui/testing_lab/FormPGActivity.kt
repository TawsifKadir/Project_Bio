package com.xplo.code.ui.testing_lab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.db.models.toJson
import com.xplo.code.data.mapper.BeneficiaryMapper
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.data.mapper.RandomMapper
import com.xplo.code.databinding.ActivityFormPgActivityBinding
import com.xplo.code.network.fake.Fake
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.toJson
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
class FormPGActivity : BaseActivity() {

    companion object {
        private const val TAG = "FormPGActivity"

        @JvmStatic
        fun open(context: Context, parent: String?, id: String?) {
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            val intent = Intent(context, FormPGActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityFormPgActivityBinding
    private val viewModel: HouseholdViewModel by viewModels()
    //private lateinit var toolbar: Toolbar

    private var uuid: String? = null
    private var householdItem: HouseholdItem? = null
    private var beneficiaryEntity: BeneficiaryEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPgActivityBinding.inflate(layoutInflater)
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
        uuid = intent.getStringExtra(Bk.KEY_ID)
        Log.d(TAG, "initView: parent: $parent")

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }

        binding.tvDetails.movementMethod = ScrollingMovementMethod()
        //binding.tvDetails.text = id


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemSuccess -> {
                        hideLoading()
                        householdItem = event.item
                        binding.tvDetails.text = event.item.toString()
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetBeneficiaryEntitySuccess -> {
                        hideLoading()
                        beneficiaryEntity = event.item
                        binding.tvDetails.text = event.item.toJson()
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }

        // old form table
        binding.viewOptionButtonHi.btPreview.setOnClickListener {
            viewModel.getHouseholdItem(uuid)
        }

        binding.viewOptionButtonHi.btDelete.setOnClickListener {
            viewModel.deleteHouseholdItem(householdItem)
        }

        binding.viewOptionButtonHi.btSend.setOnClickListener {
            binding.tvDetails.text = householdItem.toHouseholdForm().toJson()
           // viewModel.sendHouseholdForm(householdItem.toHouseholdForm(), 0)
        }

        binding.viewOptionButtonHi.btSaveLocally.setOnClickListener {
            val item = EntityMapper.toBeneficiaryEntity(householdItem.toHouseholdForm())
            binding.tvDetails.text = item.toJson()
            viewModel.saveBeneficiaryEntity(item)
        }

        // entity table
        binding.viewOptionButtonEntity.btPreview.setOnClickListener {
            viewModel.getBeneficiaryEntity(uuid)
        }

        binding.viewOptionButtonEntity.btDelete.setOnClickListener {
            viewModel.deleteBeneficiaryEntity(beneficiaryEntity)
        }

        binding.viewOptionButtonEntity.btSend.setOnClickListener {
            binding.tvDetails.text = beneficiaryEntity.toJson()
            //viewModel.sendBeneficiaryEntity(beneficiaryEntity, 0)
        }

        binding.viewOptionButtonEntity.btSendWithRandomId.setOnClickListener {
            val beneficiary = BeneficiaryMapper.toBeneficiary(beneficiaryEntity)
            val random = RandomMapper.assignBeneficiaryARandomId(beneficiary)
            binding.tvDetails.text = Gson().toJson(random)
            //viewModel.sendBeneficiary(random, 0)
        }

        // dummy data
        binding.viewOptionButtonDummy.btPreview.setOnClickListener {
            val item = Fake.getABenificiary()
            binding.tvDetails.text = Gson().toJson(item)
        }

        binding.viewOptionButtonDummy.btSend.setOnClickListener {
            val item = Fake.getABenificiary()
            binding.tvDetails.text = Gson().toJson(item)
            //viewModel.sendBeneficiary(item, 0)
        }

        binding.viewOptionButtonDummy.btSendWithRandomId.setOnClickListener {
            val item = Fake.getABenificiary()
            val random = RandomMapper.assignBeneficiaryARandomId(item)
            binding.tvDetails.text = Gson().toJson(random)
            //viewModel.sendBeneficiary(random, 0)
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


}
