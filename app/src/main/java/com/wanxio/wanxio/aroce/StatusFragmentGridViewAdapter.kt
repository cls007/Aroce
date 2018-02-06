package com.wanxio.wanxio.aroce

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.ProgressBar
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.support.v7.widget.RecyclerView


class StatusFragmentGridViewAdapter(context: Context): BaseAdapter() {

    private var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val currLevel = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("list_preference_level", "LevelA")
    private val dbhelper = QuestionDBHelper(context, currLevel)

    override fun getCount(): Int {
        return dbhelper.getNumOfItems()
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(view: View?) {
        var textView: TextView = view!!.findViewById<TextView>(R.id.textView_status)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        val holder: ViewHolder
        if (convertView == null){
            v = inflater.inflate(R.layout.status_grid_view_item, parent, false)
            holder = ViewHolder(v)
            v.tag = holder
        }else{
            v = convertView
            holder = convertView.tag as ViewHolder
        }

        val q = dbhelper.readQuestion(position.toString())
        when (q.status){
            "-1" -> {
                holder.textView.setBackgroundResource(R.drawable.grid_view_item_error)
                holder.textView.setTextColor(Color.WHITE)
            }
            "1" -> {
                holder.textView.setBackgroundResource(R.drawable.grid_view_item_correct)
                holder.textView.setTextColor(Color.WHITE)
            }
            "0" -> {
                holder.textView.setBackgroundResource(R.drawable.grid_view_item_normal)
                holder.textView.setTextColor(Color.DKGRAY)
            }
        }
        holder.textView.text = (position + 1).toString()
        return v
    }
}