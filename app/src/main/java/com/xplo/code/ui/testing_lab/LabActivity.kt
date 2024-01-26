package com.xplo.code.ui.testing_lab

import android.os.Bundle
import com.xplo.code.R
import com.xplo.code.base.BaseActivity


class LabActivity : BaseActivity(), LabContract.View {

    companion object {
        private const val TAG = "LabActivity"
    }

    private lateinit var presenter: LabContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab)

        initInitial()

        initView()


    }

    override fun initView() {
        setupToolbar()
        setToolbarTitle("my lab")


    }

    override fun initObserver() {

    }

    override fun initInitial() {
        presenter = LabPresenter()
        presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }


}
