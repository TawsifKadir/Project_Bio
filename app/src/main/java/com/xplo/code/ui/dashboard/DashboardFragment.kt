package com.xplo.code.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kit.integrationmanager.model.SyncResult
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.databinding.FragmentDashboardBinding
import com.xplo.code.utils.DbExporter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Observable
import java.util.Observer


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class DashboardFragment : BaseFragment(), DashboardContract.View, Observer {

    companion object {
        private const val TAG = "DashboardFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): DashboardFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = DashboardFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    //private lateinit var presenter: DashboardContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = DashboardPresenter(DataRepoImpl())
        //presenter.attach(this)
    }

    override fun initView() {

    }

    override fun initObserver() {
        binding.viewHousehold.setOnClickListener {
            navigateToHousehold()
        }
        binding.viewAlternate.setOnClickListener {
            navigateToAlternate(null)
        }
        binding.viewReport.setOnClickListener {
            //navigateToReport()
            showAlerter("warning", "Not implemented yet")
        }
        binding.viewPayment.setOnClickListener {
            //navigateToPayment()
            binding.btTest.callOnClick()
        }

        binding.btTest.setOnClickListener {

            DbExporter.exportWithPermission(requireContext(), requireActivity())

//            if (!DbExporter.hasStoragePermission(requireContext())){
//                // Permission is already granted, proceed with your operation
//                DbExporter.askForPermission(requireActivity())
//                return@setOnClickListener
//            }
//
//            DbController.close()
//            DbExporter.exportToSQLite(requireContext())


//            // Check for permission and export the database
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                // Permission is not granted, request it
//                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
//            } else {
//
//            }
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && false == Environment.isExternalStorageManager()) {
//                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
//                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
//                return@setOnClickListener
//            }
//
//            DbController.close()
//            DbExporter.exportToSQLite(requireContext())


//            val builder = AlertDialog.Builder(requireContext())
//            builder.setMessage(getString(R.string.reset_all_msg))
//                .setTitle("hi")
//                .setCancelable(true)
//                .setPositiveButton(getString(R.string.ok)) { _, _ ->
//                    showLoading()
//                }
//                .setNegativeButton(getString(R.string.cancel), null)
//                .create()
//                .show()


        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun update(observable: Observable?, arg: Any?) {
        Log.d(TAG, "update() called with: observable = $observable, arg = $arg")
        if (arg == null) return

        val syncResult = arg as SyncResult?
        onGetSyncResult(syncResult)


    }

    private fun onGetSyncResult(syncResult: SyncResult?) {
        Log.d(TAG, "onGetSyncResult() called with: syncResult = ${syncResult?.syncStatus}")
        if (syncResult == null) return
        //showToast(syncResult.syncStatus.toString())
//        when (syncResult.syncStatus) {
//            SyncStatus.SUCCESS -> onSyncSuccess(syncResult)
//            else -> onSyncFailure(syncResult)
//        }


    }

}
