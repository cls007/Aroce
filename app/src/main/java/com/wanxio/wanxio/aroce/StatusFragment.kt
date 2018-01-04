package com.wanxio.wanxio.aroce


import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_status.*


class StatusFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("Aroce","onCreate Status")

        return inflater!!.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //start my code
        activity.title = getString(R.string.title_status_fragment)
        status_gridview.setAdapter(StatusFragmentGridViewAdapter(this.context))

        status_gridview.setOnItemClickListener({ _, _, position, _ ->

            //Log.d("StatusFragment", "saving currqid")
            //设置回到练习界面要显示的题目
            val settings = activity.getSharedPreferences(AStatus.PREFS_NAME, 0)
            val edit = settings.edit()
            val currLevel = PreferenceManager.getDefaultSharedPreferences(this.context)
                    .getString("list_preference_level", "LevelA")
            edit.putString(currLevel + "currqid", position.toString())
            edit.apply()

            activity.nav_view.menu.findItem(R.id.nav_practice).isChecked = true
            activity.fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main, PracticeFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
        })
    }

//    override fun onResume() {
//        Log.d("Aroce","onResume Status ")
//        Log.d("Level:",
//                PreferenceManager.getDefaultSharedPreferences(this.context)
//                        .getString("list_preference_level", "-1"))
//        super.onResume()
//    }
}
