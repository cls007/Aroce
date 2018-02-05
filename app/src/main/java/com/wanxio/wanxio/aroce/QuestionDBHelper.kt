package com.wanxio.wanxio.aroce

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class QuestionDBHelper(context: Context, table: String ): SQLiteOpenHelper(context, DATABASE_NAME,
        null, DATABASE_VERSION ) {

//    init {
//        TABLE_NAME = table
//    }
    val TABLE_NAME = table
    override fun onCreate(db: SQLiteDatabase?) {

    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {    }

    //创建一个表
    fun createDBTable(): QuestionDBHelper {
        writableDatabase.execSQL(SQL_CREATE_TABLE)
        return this
    }
    //删除一个表
    fun deleteDBTable(): QuestionDBHelper {
        writableDatabase.execSQL(SQL_DELETE_TABLE)
        return this
    }

    //插入一个问题
    fun insertQuestion(question: QuestionModel) {
        //content values
        val values = ContentValues()
        values.put(QuestionDBContract.Entry.COLUMN_QID, question.qid)
        values.put(QuestionDBContract.Entry.COLUMN_LK, question.lk)
        values.put(QuestionDBContract.Entry.COLUMN_QUESTION, question.question)
        values.put(QuestionDBContract.Entry.COLUMN_ANSA, question.ansA)
        values.put(QuestionDBContract.Entry.COLUMN_ANSB, question.ansB)
        values.put(QuestionDBContract.Entry.COLUMN_ANSC, question.ansC)
        values.put(QuestionDBContract.Entry.COLUMN_ANSD, question.ansD)
        values.put(QuestionDBContract.Entry.COLUMN_STATUS, question.status)
        //insert
        writableDatabase.insert(TABLE_NAME, null, values)
    }
    //读取一个问题
    fun readQuestion(questionid: String): QuestionModel  {
        val questions = ArrayList<QuestionModel>()
        var cursor: Cursor? = null
        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM " +
                    TABLE_NAME +
                    " WHERE " +
                    QuestionDBContract.Entry.COLUMN_QID + "='" + questionid + "'",
                    null
                    )
        }
        catch (e: SQLiteException){
            return QuestionModel("", "","","","","","","")
        }
        if (cursor!!.moveToFirst()){
            while (cursor.isAfterLast == false){
                questions.add(QuestionModel(
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_QID)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_LK)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSA)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSB)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSC)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSD)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_STATUS))
                        )
                )
                cursor.moveToNext()
            }
        }
        cursor.close()
        return questions[0]
    }
    //读取所有的问题
    fun readAllQuestion(): ArrayList<QuestionModel>  {
        val questions = ArrayList<QuestionModel>()
        var cursor: Cursor? = null
        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM " +
                    TABLE_NAME + ";",
                    null
            )
        }
        catch (e: SQLiteException){
            return ArrayList<QuestionModel>()
        }
        if (cursor!!.moveToFirst()){
            while (cursor.isAfterLast == false){
                questions.add(QuestionModel(
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_QID)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_LK)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSA)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSB)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSC)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_ANSD)),
                        cursor.getString(cursor.getColumnIndex(QuestionDBContract.Entry.COLUMN_STATUS))
                )
                )
                cursor.moveToNext()
            }
        }
        cursor.close()

        return questions
    }
    //修改一道题目的答题状态(-1 错 1对)
    fun modifyStatus(qid: Int, status: Boolean) {
        if (status) {
            writableDatabase.execSQL("UPDATE " + TABLE_NAME +
                    " SET " + QuestionDBContract.Entry.COLUMN_STATUS + " = '1' " +
                    " WHERE " + QuestionDBContract.Entry.COLUMN_QID + " = " + qid.toString()+";")
        }else{
            writableDatabase.execSQL("UPDATE " + TABLE_NAME +
                    " SET " + QuestionDBContract.Entry.COLUMN_STATUS + " = '-1' " +
                    " WHERE " + QuestionDBContract.Entry.COLUMN_QID + " = " + qid.toString()+";")
        }
    }
    //删除表中所有答题情况
    fun clearAllStatus(){
        writableDatabase.execSQL("UPDATE " + TABLE_NAME +
                " SET " + QuestionDBContract.Entry.COLUMN_STATUS + " = '0' ")
    }
    //返回表中总的条数
    fun getNumOfItems(): Int{
        val cursor = readableDatabase.rawQuery("SELECT count(*) FROM " + TABLE_NAME + ";", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    //定义了数据库名,版本,以及用来删除和新建表的语句
    companion object {
        val DATABASE_NAME = "question.db"
        val DATABASE_VERSION = 1
    }
    
    private val SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" +
                    QuestionDBContract.Entry.COLUMN_QID + " TEXT PRIMARY KEY, " +
                    QuestionDBContract.Entry.COLUMN_LK + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_QUESTION + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_ANSA + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_ANSB + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_ANSC + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_ANSD + " TEXT, " +
                    QuestionDBContract.Entry.COLUMN_STATUS + " TEXT " +
                    ");"

    private val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";"

}