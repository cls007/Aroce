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
            restDB()
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


    //执行清除操作的函数
    private fun restDB() {
        QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_A).clearAllStatus()
        QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_B).clearAllStatus()
        QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_C).clearAllStatus()
        AStatus.PracticeStatus.currentQid = 0
    }

}// Required empty public constructor
