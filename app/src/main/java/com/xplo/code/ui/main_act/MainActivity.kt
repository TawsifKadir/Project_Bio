package com.xplo.code.ui.main_act

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityMainBinding
import com.xplo.code.ui.dashboard.DashboardFragment
import com.xplo.code.ui.dashboard.household.list.HouseholdListFragment
import com.xplo.code.ui.favorite.FavoriteActivity
import com.xplo.code.ui.home.HomeFragment
import com.xplo.code.ui.user_profile.FormDetailsClickActivity
import com.xplo.code.ui.user_profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence


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
class MainActivity : BaseActivity(), MainContract.View,
    BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG = "MainActivity"
        var shouldRestart = false

        @JvmStatic
        fun open(context: Context, parent: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent")

            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    //private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initInitial()
        initView()
        initObserver()


    }

    override fun initInitial() {
//        presenter = MainPresenter()
//        presenter.attach(this)
    }

    override fun initView() {
        setupToolbar()

        setupNavigationDrawerToggle(getToolbar())

        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        //showLoading()

        binding.fab.setOnClickListener {
            onClickSearch()
        }

        selectMenuItem(R.id.mDashboard)

        //onRefreshLoginLogoutView()

        //onRunTuto()
    }

    override fun initObserver() {

    }

    override fun onResume() {
        super.onResume()

//        Log.d(TAG, "onResume: isLocaleChanged: " + RMemory.isLocaleChanged)
//        Log.d(TAG, "onResume: isThemeChanged: " + RMemory.isThemeChanged)
//        if (RMemory.isLocaleChanged || RMemory.isThemeChanged) {
//            RMemory.isLocaleChanged = false
//            RMemory.isThemeChanged = false
//            restartActivity()
//        }

        Log.d(TAG, "onResume: shouldRestart: $shouldRestart")
        if (shouldRestart) {
            shouldRestart = false
            restartActivity()
        }

        onRefreshLoginLogoutView()

    }

    override fun onDestroy() {
        //presenter.detach()
        super.onDestroy()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected() called with: item = $item")

        // Highlight the selected item, update the title, and close the drawer_menu
        //item.isChecked = true
        //title = item.title
        binding.drawerLayout.closeDrawers()

        return selectMenuItem(item.itemId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.menu_common, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mProfile -> {
                openActivity(ProfileActivity::class.java, null)
            }
           /* R.id.mProfile -> {
                openActivity(FormDetailsClickActivity::class.java, null)
            }*/



        }

        return super.onOptionsItemSelected(item)
    }

    override fun selectMenuItem(itemId: Int): Boolean {
        Log.d(TAG, "selectMenuItem() called with: itemId = $itemId")

        val menuItem = binding.navigationView.menu.findItem(itemId)

        when (itemId) {

            R.id.mHome -> {
                title = getString(R.string.home)
                menuItem.isChecked = true
                //new instance
                loadFragment(HomeFragment.newInstance(null), null)
                return true
            }

            R.id.mDashboard -> {
                title = menuItem.title
                menuItem.isChecked = true
                loadFragment(DashboardFragment.newInstance(null), null)
                return true
            }

            R.id.mContentList -> {
                title = menuItem.title
                menuItem.isChecked = true
                loadFragment(HouseholdListFragment.newInstance(null), null)
                return true
            }

            R.id.mFavorite -> {
                title = getString(R.string.favorite)
                menuItem.isChecked = true
                FavoriteActivity.open(this, null)
                return true
            }

            R.id.mSettings -> {
                menuItem.isChecked = false
                navigateToSettings()
                return false
            }

//            R.id.mLogin -> {
//                navigateToLogin()
//                return true
//            }
//
//            R.id.mLogout -> {
//                onLogout()
//                navigateToLogin()
//                return true
//            }

            R.id.mLoginLogout -> {
                menuItem.isChecked = true
                if (isLoggedIn()){
                    onLogout()
                    navigateToLogin()
                }else {
                    navigateToLogin()
                }

                return false
            }


        }
        return true
    }

    override fun onLogout() {
        super.onLogout()
        onRefreshLoginLogoutView()
    }

    override fun onRefreshLoginLogoutView() {
        super.onRefreshLoginLogoutView()

        if (getPrefHelper().isLoggedIn()) {
            val navMenu: Menu = binding.navigationView.menu
//            navMenu.findItem(R.id.mLogin).isVisible = false
//            navMenu.findItem(R.id.mLogout).isVisible = true

            navMenu.findItem(R.id.mLoginLogout).title = getString(R.string.logout)

        } else {
            val navMenu: Menu = binding.navigationView.menu
//            navMenu.findItem(R.id.mLogin).isVisible = true
//            navMenu.findItem(R.id.mLogout).isVisible = false

            navMenu.findItem(R.id.mLoginLogout).title = getString(R.string.login)
        }



    }

    override fun loadFragment(fragment: Fragment, bundle: Bundle?) {
        super.loadFragment(fragment, bundle)
    }

    override fun onClickSearch() {
        showToast(getString(R.string.search))
        showLoading()
    }

    override fun onRunTuto() {

        if (!getPrefHelper().isTooltipAlive()) return;

//        val child = getToolbar()?.getChildAt(2)
//        var actionMenuView: ActionMenuView? = null
//        if (child is ActionMenuView) {
//            actionMenuView = child as ActionMenuView
//        }
//
//        var promptOverflow: MaterialTapTargetPrompt? = null
//
//        if (actionMenuView != null) {
//            promptOverflow = MaterialTapTargetPrompt.Builder(this)
//                .setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1))
//                .setPrimaryText(getString(R.string.more))
//                .setSecondaryText(getString(R.string.click_to_know_more))
//                .setIcon(R.drawable.ic_more_vert_black_24dp)
//                .setBackgroundColour(resources.getColor(R.color.tuto_color))
//                .create()
//        }


        val promptSearch = MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.fab)
            .setSecondaryText(getString(R.string.dummy_text_medium))
            .setSecondaryTextColour(resources.getColor(R.color.white))
            .setIcon(R.drawable.ic_search_black_24dp)
            .setBackgroundColour(resources.getColor(R.color.li_accent))
            .create()


        val sequence = MaterialTapTargetSequence()
        sequence.addPrompt(promptSearch)
//        if (promptOverflow != null) {
//            sequence.addPrompt(promptOverflow);
//        }

        sequence.setSequenceCompleteListener {
            getPrefHelper().offTooltip()
        }
        sequence.show()
    }

    private fun setupNavigationDrawerToggle(toolbar: Toolbar?) {

        val actionBarDrawerToggle = object :
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
            ) {}

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }
}
