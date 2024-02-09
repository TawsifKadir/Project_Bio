package com.xplo.code.ui.testing_lab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityJvBinding
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
class JvActivity : BaseActivity() {

    companion object {
        private const val TAG = "FavoriteActivity"

        @JvmStatic
        fun open(context: Context, parent: String?, json: String?) {
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ITEM, json)
            val intent = Intent(context, JvActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityJvBinding
    //private val viewModel: FavoriteViewModel by viewModels()
    //private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJvBinding.inflate(layoutInflater)
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
        val jsonTxt = intent.getStringExtra(Bk.KEY_ITEM)
        Log.d(TAG, "initView: parent: $parent")

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }

        binding.tvDetails.movementMethod = ScrollingMovementMethod()
        binding.tvDetails.text = jsonTxt


    }

    override fun initObserver() {


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
