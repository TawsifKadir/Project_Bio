package com.xplo.code.ui.settings


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.xplo.code.R
import com.xplo.code.base.BaseSettingsFragment
import com.xplo.code.core.Bk
import com.xplo.code.data.RMemory
import com.xplo.code.data.pref.PkSettings
import com.xplo.code.ui.main_act.MainActivity
import com.xplo.code.ui.user_profile.ProfileActivity
import com.xplo.code.utils.AppInfo
import com.xplo.code.utils.EasyMenu

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class SettingsFragment : BaseSettingsFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    companion object {
        private const val TAG = "SettingsFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): SettingsFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = SettingsFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }


    private var mSettingsActivity: SettingsActivity? = null

    private lateinit var mPreferenceScreen: PreferenceScreen


    private var pfTheme: ListPreference? = null
    private var pfLanguage: ListPreference? = null
    private var pfResetAll: Preference? = null

    private var pfRate: Preference? = null
    private var pfShare: Preference? = null
    private var pfFeedback: Preference? = null
    private var pfAbout: Preference? = null
    private var pfDeveloper: Preference? = null

    private var pfDevOption: Preference? = null
    private var pfCatAdvanceSettings: Preference? = null


    //private lateinit var em: EasyMenu

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsActivity) {
            mSettingsActivity = context
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

        //em = EasyMenu(activity)

        mPreferenceScreen = preferenceScreen

        pfTheme = findPreference(PkSettings.pfTheme)
        pfLanguage = findPreference(PkSettings.pfLanguage)
        pfResetAll = findPreference(PkSettings.pfResetAll)

        pfRate = findPreference(PkSettings.pfRate)
        pfShare = findPreference(PkSettings.pfShare)
        pfFeedback = findPreference(PkSettings.pfFeedback)
        pfAbout = findPreference(PkSettings.pfAbout)
        pfDeveloper = findPreference(PkSettings.pfDeveloper)

        pfDevOption = findPreference(PkSettings.pfDevOption)
        pfCatAdvanceSettings = findPreference(PkSettings.pfCatAdvanceSettings)



        pfRate?.onPreferenceClickListener = this
        pfShare?.onPreferenceClickListener = this
        pfFeedback?.onPreferenceClickListener = this
        pfDevOption?.onPreferenceClickListener = this

        pfAbout?.onPreferenceClickListener = this
        pfDeveloper?.onPreferenceClickListener = this
        pfResetAll?.onPreferenceClickListener = this


        if (pfTheme?.value == null) {
            // to ensure we don't get a null value set first value by default
            pfTheme?.setValueIndex(0)
        }
        pfTheme?.summary = pfTheme?.value
        pfTheme?.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            true
        }

        pfAbout?.summary = "Version " + AppInfo.appVersionName

        if (getPrefHelper().getLocale().equals("en")) {
            pfLanguage?.setValueIndex(0)
        } else {
            pfLanguage?.setValueIndex(1)
        }


    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        Log.d(TAG, "onSharedPreferenceChanged: key: $key")

        when (key) {

            PkSettings.pfTheme -> {

                MainActivity.shouldRestart = true
                ProfileActivity.shouldRestart = true
                //RMemory.isThemeChanged = true
                Log.d(TAG, "onSharedPreferenceChanged: isThemeChanged: ${RMemory.isThemeChanged}")
                restartActivity()

            }

            PkSettings.pfLanguage -> {

                val language = sharedPreferences.getString(key, null)
                Log.d(TAG, "onSharedPreferenceChanged: language: $language")

                val locale: String? = getLocaleFromLanguage(language)
                Log.d(TAG, "onSharedPreferenceChanged: locale: $locale")

                getPrefHelper().setLocale(locale!!)
                //getBaseActivity().updateBaseContextLocale(activity!!.baseContext)
                //getBaseActivity().onConfigurationChanged(Configuration());

                MainActivity.shouldRestart = true
                ProfileActivity.shouldRestart = true
//                RMemory.isLocaleChanged = true
                Log.d(TAG, "onSharedPreferenceChanged: isLocaleChanged: ${RMemory.isLocaleChanged}")
                restartActivity()


//                locale = getPrefHelper().getLocale()
//                Log.d(TAG, "onSharedPreferenceChanged: locale: $locale")


            }

        }
    }

    private fun getLocaleFromLanguage(language: String?): String {

        if (language.equals("English", true)) return "en"
        if (language.equals("বাংলা", true)) return "bn"
        return "en"

    }


    override fun onPreferenceClick(preference: Preference): Boolean {

        when (preference.key) {

            PkSettings.pfRate -> {
                EasyMenu.rate(requireContext())
                return true
            }

            PkSettings.pfAbout -> {
                EasyMenu.showAboutText(requireContext())
                return true
            }

            PkSettings.pfDeveloper -> {
                jobDevOption()
                return true
            }

            PkSettings.pfShare -> {
                EasyMenu.shareApp(requireContext())
                return true
            }

            PkSettings.pfFeedback -> {
                EasyMenu.feedback(requireContext())
                return true
            }

            PkSettings.pfResetAll -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(getString(R.string.reset_all_msg))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                        getPrefHelper().resetAll()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .create()
                    .show()
                return true
            }

            PkSettings.pfDevOption -> {

                return true
            }

        }

        return true
    }

    private fun jobDevOption() {

        if (pfDevOption == null) return

        val doublePressInterval: Long = 250 // in millis
        val counterThreshold = 3

        var lastPressTime: Long = 0
        var counter = 0


        Log.d(TAG, "counter: $counter")

        if (counter == counterThreshold) {
            showMessage("One more time")
        }

        if (counter > counterThreshold) {

            if (pfDevOption!!.isEnabled) {
                pfDevOption!!.isEnabled = false
                showMessage("Developer Option Disabled")
            } else {
                pfDevOption!!.isEnabled = true
                showMessage("Developer Option Enabled")
            }

            counter = 0
            return
        }
        // // Get current time in nano seconds.
        val pressTime = System.currentTimeMillis()


        // If double click...
        if (pressTime - lastPressTime <= doublePressInterval) {

            //showMessage("Double Click Event");
            //mHasDoubleClicked = true;

            counter++

        } else {

        }
        lastPressTime = pressTime
    }


    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }


}
