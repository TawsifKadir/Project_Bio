package com.xplo.code.ui.testing_lab

import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        findViewById<Button>(R.id.btTest).setOnClickListener {
            val bottomSheet = SimpleBottomSheet(){
                Log.d(TAG, "initView() called: $it")
            }
            bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
        }


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
