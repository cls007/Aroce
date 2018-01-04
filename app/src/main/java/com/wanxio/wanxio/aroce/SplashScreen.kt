package com.wanxio.wanxio.aroce

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.d("SplashScreen","SplashScreen ready to call main")
        //检查是否是第一次启动,是的话加载题库到数据库中
        if (getSharedPreferences(AStatus.PREFS_NAME, 0)
                .getBoolean("isFirstRun", true) == false){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //在UI可见后加载数据库(此时可以显示初始化信息)
    override fun onResume() {
        super.onResume()
        Log.d("SplashScreen","First time to run")
        initDatabase()
        //set isFirstRun to false
        getSharedPreferences(AStatus.PREFS_NAME, 0)
                .edit()
                .putBoolean("isFirstRun", false)
                .apply()
    }
    //初始化数据库
    private fun initDatabase(){
        ParseQuestionFile({onParseQuestionFileFinishTask()},
                QuestionDBHelper(this, QuestionDBContract.Entry.TABLE_NAME_LEVEL_A)
                , resources.openRawResource(R.raw.levela)).execute()
        ParseQuestionFile({onParseQuestionFileFinishTask()},
                QuestionDBHelper(this, QuestionDBContract.Entry.TABLE_NAME_LEVEL_B)
                , resources.openRawResource(R.raw.levelb)).execute()
        ParseQuestionFile({onParseQuestionFileFinishTask()},
                QuestionDBHelper(this, QuestionDBContract.Entry.TABLE_NAME_LEVEL_C)
                , resources.openRawResource(R.raw.levelc)).execute()
    }
    //作为检查是否加载完成的callback,三个数据库表会调用它三次
    var times = 1
    fun onParseQuestionFileFinishTask() {
        if (times != 3){
            times ++
        }else{
            //go to mainacticity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}
