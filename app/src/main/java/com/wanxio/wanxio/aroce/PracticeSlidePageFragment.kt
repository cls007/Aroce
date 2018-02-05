package com.wanxio.wanxio.aroce

import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_practice_slide_page.*
import java.util.*

class PracticeSlidePageFragment : Fragment() {


    var isSelected = false
    var correctAns = "A"
//    lateinit var QuestionList: ArrayList<QuestionModel>
    lateinit var currQuestion: QuestionModel
    lateinit var dbHelper: QuestionDBHelper
    lateinit var currLevel: String
    lateinit var currPracticeMode: String
    var currQid: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_practice_slide_page, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = getString(R.string.title_practice_fragment)

        //start my code
        //读取当前答题的模式
        currPracticeMode = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString("list_preference_mode", "ExamMode")
        currLevel = arguments.getString("currLevel")
        dbHelper = QuestionDBHelper(this.context, currLevel)
        currQid = arguments.getInt("qid")
        //读取对应的问题
        currQuestion = dbHelper.readQuestion(currQid.toString())
        //set onclick listener
        buttonAnsA.setOnClickListener({v -> ansClick(v)})
        buttonAnsB.setOnClickListener({v -> ansClick(v)})
        buttonAnsC.setOnClickListener({v -> ansClick(v)})
        buttonAnsD.setOnClickListener({v -> ansClick(v)})
        // display
        loadQuestionToUI()
    }

    //清除按键的颜色
    private fun clearButtonColor() {
        buttonAnsA.setBackgroundResource(R.color.buttonNormal)
        buttonAnsB.setBackgroundResource(R.color.buttonNormal)
        buttonAnsC.setBackgroundResource(R.color.buttonNormal)
        buttonAnsD.setBackgroundResource(R.color.buttonNormal)
    }

    //显示数据到UI上
    private fun loadQuestionToUI() {
        clearButtonColor()
        isSelected = false
        textViewLK.text = currQuestion.lk
        textViewSchedule.text = "${currQid + 1} / ${((dbHelper.getNumOfItems() + 1)).toString()}"
        val idf = resources.getIdentifier(currQuestion.lk.toLowerCase(), "drawable", activity.packageName )
        if (idf != 0)
        {
            Log.d("Practice", "load pic idf is " + idf.toString())
            imageViewPic.setImageResource(idf)
        }else{
            imageViewPic.setImageDrawable(null)
        }
        textViewQuestion.text = currQuestion.question
        loadOptionsRandom()
    }

    //打乱选项答案
    private fun loadOptionsRandom(){
        val options = mutableListOf<String>()
        if (currPracticeMode == "ExamMode") {
            options.add(currQuestion.ansA)
            options.add(currQuestion.ansB)
            options.add(currQuestion.ansC)
            options.add(currQuestion.ansD)

            for (i in 0 until options.size) {
                val ranInt = Random().nextInt(4)
                val tempS = options[ranInt]
                options[ranInt] = options[0]
                options[0] = tempS
            }
            //find correct answer
            val mlist = listOf<String>("A", "B", "C", "D")
            val reg = Regex("^\\[A\\].*")
            val reg2 = Regex("^\\[[ABCD]\\]")
            for (i in 0 until options.size) {
                if (reg.matches(options[i])) {
                    correctAns = mlist[i]
                }
                //remove prefix
                options[i] = reg2.replace(options[i], "")
            }
            //load to UI
            buttonAnsA.text = "[A]" + options[0]
            buttonAnsB.text = "[B]" + options[1]
            buttonAnsC.text = "[C]" + options[2]
            buttonAnsD.text = "[D]" + options[3]
        }
        else {
            buttonAnsA.text = currQuestion.ansA
            buttonAnsB.text = currQuestion.ansB
            buttonAnsC.text = currQuestion.ansC
            buttonAnsD.text = currQuestion.ansD
        }

    }

    //检查答案是否正确
    private fun checkAns(press: String, view: View) {
        if (!isSelected) {
            //If answer is Correct
            if (press == correctAns){
                //正确需要设置成正确时的颜色
                view.setBackgroundResource(R.color.answerCorrect)
                //在数据库中标记这道题作对了
                dbHelper.modifyStatus(currQid, true)
                //短暂延时后自动跳到下一道题
                object : CountDownTimer(300, 300) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        //TODO: 自动跳转到下一个
                    }
                }.start()
            }
            else {
                //在数据库中标记这道题做错了
                dbHelper.modifyStatus(currQid, false)
                view.setBackgroundResource(R.color.answerError)
                when (correctAns) {
                    "A" -> buttonAnsA.setBackgroundResource(R.color.answerCorrect)
                    "B" -> buttonAnsB.setBackgroundResource(R.color.answerCorrect)
                    "C" -> buttonAnsC.setBackgroundResource(R.color.answerCorrect)
                    "D" -> buttonAnsD.setBackgroundResource(R.color.answerCorrect)
                }
            }
            //达到一道题只能选择一次的目的
            isSelected = true
        }
    }

    //OnClick callback
    fun ansClick(view: View) {
        when (view.id) {
            R.id.buttonAnsA -> checkAns("A", view)
            R.id.buttonAnsB -> checkAns("B", view)
            R.id.buttonAnsC -> checkAns("C", view)
            R.id.buttonAnsD -> checkAns("D", view)
        }
    }
}