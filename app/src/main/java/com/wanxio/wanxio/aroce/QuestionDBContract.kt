package com.wanxio.wanxio.aroce

import android.provider.BaseColumns

//数据库中的表项,包括了表名
object QuestionDBContract {
    class Entry: BaseColumns{
        companion object {
            val TABLE_NAME_LEVEL_A = "LevelA"
            val TABLE_NAME_LEVEL_B = "LevelB"
            val TABLE_NAME_LEVEL_C = "LevelC"
            val COLUMN_QID = "qid"
            val COLUMN_LK = "lk"
            val COLUMN_QUESTION = "question"
            val COLUMN_ANSA = "ansA"
            val COLUMN_ANSB = "ansB"
            val COLUMN_ANSC = "ansC"
            val COLUMN_ANSD = "ansD"
            val COLUMN_STATUS = "status"
        }
    }
}