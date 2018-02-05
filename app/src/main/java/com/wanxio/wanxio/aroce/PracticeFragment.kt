package com.wanxio.wanxio.aroce


import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_practice.*
import java.util.*

class PracticeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //Log.d("Aroce","onCreate Practice")
        return inflater!!.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        practiceViewPager.adapter = PracticeSlidePageAdapter(context, fragmentManager)
    }


    //TODO: 进度保存功能~~
//
//    override fun onDetach() {
//        //在离开的时候保存当前等级的题目进度
//        super.onDetach()
//        //Log.d("practice", "detach now, saving currqid")
//        val settings = activity.getSharedPreferences(AStatus.PREFS_NAME, 0)
//        val edit = settings.edit()
//        edit.putString(currLevel + "currqid", AStatus.PracticeStatus.currentQid.toString())
//        edit.apply()
//    }
//


}// Required empty public constructor
