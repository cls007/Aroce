package com.wanxio.wanxio.aroce

import android.content.Context
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StatusFragmentGridViewAdapter(context: Context): BaseAdapter() {

    private var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val currLevel = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("list_preference_level", "LevelA")
    private val dbhelper = QuestionDBHelper(context, currLevel)

    override fun getCount(): Int {
        return dbhelper.readAllQuestion().size
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = inflater.inflate(R.layout.status_grid_view_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView_status)
        val q = dbhelper.readQuestion(position.toString())
        when (q[q.lastIndex].status){
            "-1" -> {
                textView.setBackgroundResource(R.drawable.grid_view_item_error)
                textView.setTextColor(Color.WHITE)
            }
            "1" -> {
                textView.setBackgroundResource(R.drawable.grid_view_item_correct)
                textView.setTextColor(Color.WHITE)
            }
        }
        textView.text = (position + 1).toString()
        return view
    }
}