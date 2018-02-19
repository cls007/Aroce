package com.wanxio.wanxio.aroce

import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_practice.*

class PracticeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //Log.d("Aroce","onCreate Practice")
        return inflater!!.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        practiceViewPager.adapter = PracticeSlidePageAdapter(context, fragmentManager,
                {//会在viewpage中选中正确答案的时候被调用
                    practiceViewPager.currentItem = practiceViewPager.currentItem + 1
                })
        //恢复上次的进度
        val currLevel = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString("list_preference_level", "LevelA")
        val pref = activity.getSharedPreferences(AStatus.PREFS_NAME, 0)

        practiceViewPager.currentItem = pref.getInt(currLevel + "currqid", 0)
        Log.d("Practice", "onViewCreated: currentItem = ${practiceViewPager.currentItem} ")
    }





}// Required empty public constructor
