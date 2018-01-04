package com.wanxio.wanxio.aroce


import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
        //这个设置项会清空数据库中的答题数据
        val restButton = preferenceManager.findPreference("RestApp")
        restButton.setOnPreferenceClickListener({
            //显示清空数据操作的提示
            ResetConfirmDialogFragment().show(activity.fragmentManager, "ConfirmDialog")
            true
        })

        activity.title = getString(R.string.title_settings_fragment)
        Log.d("Aroce","onCreate Preference")

    }

    override fun onResume() {
        Log.d("Aroce","onResume Preference ")
        Log.d("Level:",
                PreferenceManager.getDefaultSharedPreferences(this.context)
                        .getString("list_preference_level", "-1"))
        super.onResume()
    }

}