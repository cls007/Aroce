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

    var isSelected = false
    var correctAns = "A"
    lateinit var QuestionList: ArrayList<QuestionModel>
    lateinit var dbHelper: QuestionDBHelper
    lateinit var currLevel: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //Log.d("Aroce","onCreate Practice")
        return inflater!!.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = getString(R.string.title_practice_fragment)

        //start my code
        currLevel = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString("list_preference_level", "LevelA")
        //Log.d("Practice", "try to load db" + currLevel)
        dbHelper = QuestionDBHelper(this.context, currLevel)
        //读取所有的问题
        QuestionList = dbHelper.readAllQuestion()
        AStatus.PracticeStatus.maxQid = QuestionList.size
        //Log.d("Practice", "Load DB finish, now List has " + QuestionList.size.toString() + " items")
        //读取题目进度
        AStatus.PracticeStatus.currentQid =
                activity.getSharedPreferences(AStatus.PREFS_NAME, 0)
                .getString(currLevel + "currqid", "0")
                .toInt()
        //Log.d("practice", "currqid = " + AStatus.PracticeStatus.currentQid.toString())
        //set onclick listener
        buttonAnsA.setOnClickListener({v -> ansClick(v)})
        buttonAnsB.setOnClickListener({v -> ansClick(v)})
        buttonAnsC.setOnClickListener({v -> ansClick(v)})
        buttonAnsD.setOnClickListener({v -> ansClick(v)})
        buttonAfterward.setOnClickListener({v -> buttonAfterwardClick(v)})
        buttonForward.setOnClickListener({v -> buttonForwardClick(v)})
        //display
        loadQuestionToUI()
    }

    override fun onDetach() {
        //在离开的时候保存当前等级的题目进度
        super.onDetach()
        //Log.d("practice", "detach now, saving currqid")
        val settings = activity.getSharedPreferences(AStatus.PREFS_NAME, 0)
        val edit = settings.edit()
        edit.putString(currLevel + "currqid", AStatus.PracticeStatus.currentQid.toString())
        edit.apply()
    }

    //显示数据到UI上
    private fun loadQuestionToUI() {
        clearButtonColor()
        isSelected = false
        textViewLK.text = QuestionList[AStatus.PracticeStatus.currentQid].lk
        val idf = resources.getIdentifier(
                QuestionList[AStatus.PracticeStatus.currentQid].lk.toLowerCase(),
                "drawable", activity.packageName )
        //Log.d("Practice", "idf is " + idf.toString())
        //Log.d("Practice", "lk is " + QuestionList[AStatus.PracticeStatus.currentQid].lk.toLowerCase())
        if (idf != 0)
        {
            Log.d("Practice", "load pic idf is " + idf.toString())
            imageViewPic.setImageResource(idf)
        }else{
            imageViewPic.setImageDrawable(null)
        }
        textViewSchedule.text = "${AStatus.PracticeStatus.currentQid + 1} / ${QuestionList.size}"
        textViewQuestion.text = QuestionList[AStatus.PracticeStatus.currentQid].question
        loadOptionsRandom()
    }
    //清除按键的颜色
    private fun clearButtonColor() {
        buttonAnsA.setBackgroundResource(R.color.buttonNormal)
        buttonAnsB.setBackgroundResource(R.color.buttonNormal)
        buttonAnsC.setBackgroundResource(R.color.buttonNormal)
        buttonAnsD.setBackgroundResource(R.color.buttonNormal)
    }
    //检查答案是否正确
    private fun checkAns(press: String, view: View) {
        if (!isSelected) {
            //If answer is Correct
            if (press == correctAns){
                //正确需要设置成正确时的颜色
                view.setBackgroundResource(R.color.answerCorrect)
                //在数据库中标记这道题作对了
                dbHelper.modifyStatus(AStatus.PracticeStatus.currentQid, true)
                //短暂延时后自动跳到下一道题
                object : CountDownTimer(300, 300) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        fetchNextQuestion()
                        loadQuestionToUI()
                    }
                }.start()
            }
            else {
                //在数据库中标记这道题做错了
                dbHelper.modifyStatus(AStatus.PracticeStatus.currentQid, false)
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
    //打乱选项答案
    private fun loadOptionsRandom(){
        val options = mutableListOf<String>()
        options.add(QuestionList[AStatus.PracticeStatus.currentQid].ansA)
        options.add(QuestionList[AStatus.PracticeStatus.currentQid].ansB)
        options.add(QuestionList[AStatus.PracticeStatus.currentQid].ansC)
        options.add(QuestionList[AStatus.PracticeStatus.currentQid].ansD)
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
        for (i in  0 until options.size){
            if (reg.matches(options[i])){
                correctAns = mlist[i]
            }
            //remove prefix
            options[i] = reg2.replace(options[i], "")
        }
        //load to UI
        buttonAnsA.text = "[A] " + options[0]
        buttonAnsB.text = "[B] " + options[1]
        buttonAnsC.text = "[C] " + options[2]
        buttonAnsD.text = "[D] " + options[3]
    }
    //取下一道题或者上一道题,本质是更改当前的Qid(位于AStatus中)
    fun fetchPreviousQuestion() {
        AStatus.PracticeStatus.currentQid --;
        if (AStatus.PracticeStatus.currentQid < AStatus.PracticeStatus.minQid ) AStatus.PracticeStatus.currentQid = 0
    }
    fun fetchNextQuestion(){
        AStatus.PracticeStatus.currentQid ++;
        if (AStatus.PracticeStatus.currentQid > AStatus.PracticeStatus.maxQid) AStatus.PracticeStatus.currentQid = QuestionList.size
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
    fun buttonForwardClick(view: View) {
        fetchPreviousQuestion()
        loadQuestionToUI()
    }
    fun buttonAfterwardClick(view: View) {
        fetchNextQuestion()
        loadQuestionToUI()
    }

}// Required empty public constructor
