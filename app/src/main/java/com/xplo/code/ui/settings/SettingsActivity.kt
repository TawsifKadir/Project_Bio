package com.xplo.code.ui.settings


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk

class SettingsActivity : BaseActivity() {

    companion object {
        private const val TAG = "SettingsActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupToolbar()
        title = getString(R.string.settings)

        showAFragment(SettingsFragment.newInstance(null))
    }

    override fun initInitial() {

    }

    override fun initView() {
    }

    override fun initObserver() {

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showAFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        //transaction.addToBackStack(null);
        transaction.commit()
    }
}
