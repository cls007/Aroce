package com.wanxio.wanxio.aroce

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

//将文本形式的题库解析后插入数据库中
class ParseQuestionFile(callback: () -> Unit,
        dbHelper: QuestionDBHelper,
        input: InputStream)
    : AsyncTask<String, String, String>() {

    private val db = dbHelper
    private val inputStream = input
    private val mcallback = callback
    override fun doInBackground(vararg params: String?): String {
        parseToDB(db, inputStream)
        return "NULL"
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        mcallback()
    }

    private fun parseToDB(db: QuestionDBHelper, inputStream: InputStream) {
        db.createDBTable()
        val readBuf = BufferedReader(InputStreamReader(inputStream))
        val allTextFromBufferedReader: List<String> = readBuf.readLines()
        readBuf.close()
        var ind = 0;
        ForLoop@ for (i in allTextFromBufferedReader.indices) {
            //Every question start with [I]LK....
            if (allTextFromBufferedReader[i].length < 2
                    || allTextFromBufferedReader[i][1] != 'I') {
                continue@ForLoop
            }
            //Add a new question to questionList
            val aQuestion = QuestionModel(
                    qid = ind.toString(),
                    lk = allTextFromBufferedReader[i].removePrefix("[I]"),
                    question = allTextFromBufferedReader[i + 1].removePrefix("[Q]"),
                    ansA = allTextFromBufferedReader[i + 2],
                    ansB = allTextFromBufferedReader[i + 3],
                    ansC = allTextFromBufferedReader[i + 4],
                    ansD = allTextFromBufferedReader[i + 5],
                    status = 0.toString()
            )
            db.insertQuestion(aQuestion)
            ind++
        }
    }
}
