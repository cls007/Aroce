package com.wanxio.wanxio.aroce

import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v13.app.FragmentStatePagerAdapter

class PracticeSlidePageAdapter(var context: Context, fm: FragmentManager,
                               val onCorrectAnswerSelectedListener : () -> Unit)
    : FragmentStatePagerAdapter(fm) {

    //读取当前题目等级
    val currLevel = PreferenceManager.getDefaultSharedPreferences(this.context)
            .getString("list_preference_level", "LevelA")
    val db = QuestionDBHelper(context, currLevel)


    override fun getCount(): Int {
        return db.getNumOfItems()
    }

    override fun getItem(position: Int): Fragment {
        val frag = PracticeSlidePageFragment()
        frag.setOnCorrectAnswerSelectedListener(onCorrectAnswerSelectedListener)
        val arg = Bundle()
        arg.putString("currLevel", currLevel)
        arg.putInt("qid", position)
        frag.arguments = arg
        return frag
    }
}