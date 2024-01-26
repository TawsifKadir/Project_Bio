package com.xplo.code.ui.user_profile

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.xplo.code.BuildConfig
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.databinding.ActivityProfileBinding
import com.xplo.data.model.user.ProfileInfo


class ProfileActivity : BaseActivity(), ProfileContract.View {

    companion object {
        private const val TAG = "ProfileActivity"
        var shouldRestart = false

    }

    private lateinit var binding: ActivityProfileBinding
    private lateinit var presenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        //hideStatusBar()
        //setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInitial()

        initView()

        fetchData()
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

    }

    override fun initTheme() {
        Log.d(TAG, "initTheme: ")

        if (isDarkTheme()) {
            setTheme(R.style.ProfileActivityTheme_Dark)
        }
    }

    override fun initView() {
        setupToolbar()

        setToolbarTitle(getString(R.string.profile))

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            Log.d(TAG, "initView: verticalOffset: $verticalOffset")

            val minHeightAppbar = ViewCompat.getMinimumHeight(binding.collapsingToolbar) * 2
            val scaleAppbar = (minHeightAppbar + verticalOffset).toFloat() / minHeightAppbar

            binding.ivAvatar.scaleX = if (scaleAppbar >= 0) scaleAppbar else 0.toFloat()
            binding.ivAvatar.scaleY = if (scaleAppbar >= 0) scaleAppbar else 0.toFloat()

            Log.d(TAG, "initView: scaleAppbar: $scaleAppbar")

//            val minHeightViewSpace = ViewCompat.getMinimumHeight(viewSpace) * 2
//            var scaleViewSpace = (minHeightViewSpace + verticalOffset).toFloat() / minHeightViewSpace
//
//            viewSpace.scaleX = if (scaleViewSpace >= 0) scaleViewSpace else 0.toFloat()
//            viewSpace.scaleY = if (scaleViewSpace >= 0) scaleViewSpace else 0.toFloat()
//
//            Log.d(TAG, "initView: scaleViewSpace: $scaleViewSpace")

//            if (scale <= 0.0) {
//                viewSpace.visibility = View.GONE
//
//            }

        })

        binding.contentProfile.tvPhone.visibility = View.GONE
        binding.contentProfile.tvEmail.visibility = View.GONE


    }

    override fun initObserver() {

    }

    private fun fetchData() {
        val userId = getPrefHelper().getUserId()
        if (userId != null) {
            presenter.getProfileData(userId)
        }
    }

    override fun initInitial() {
        //presenter = ProfilePresenter(UserRepoImpl())
        presenter.attach(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mUpdateProfile -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onGetProfileData(profileInfo: ProfileInfo) {
        Log.d(TAG, "onGetProfileData() called with: profileInfo = $profileInfo")

//        setTextAtView(tvName, profileInfo.fullName)
//        setTextAtView(tvUserId, profileInfo.username)
//        setTextAtView(tvPhone, profileInfo.phoneNo)
//        setTextAtView(tvEmail, profileInfo.email)
//        setTextAtView(tvBirthday, profileInfo.birthDate.date)
//        setTextAtView(tvGender, profileInfo.gender)

//        tvName.text = profileInfo.fullName
//        tvUserId.text = profileInfo.username
//        tvPhone.text = profileInfo.phoneNo
//        tvEmail.text = profileInfo.email
//        tvBirthday.text = profileInfo.birthDate.date
//        tvGender.text = profileInfo.gender

        var avatar = "profileInfo.avatar"
        Log.d(TAG, "onGetProfileData: $avatar")

        if (BuildConfig.DEBUG) {
            avatar = "https://cdn.xdbbd.com/users/pp/5d873835e7b3f.jpeg"
        }

        Glide.with(this)
            .load(avatar)
            .into(binding.ivAvatar)
    }

    private fun setTextAtView(view: TextView, text: String?) {
        if (text.isNullOrBlank()) {
            view.text = "N/A"
            return
        }
        view.text = text
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }


}
