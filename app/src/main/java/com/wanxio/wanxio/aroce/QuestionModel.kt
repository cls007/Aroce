package com.wanxio.wanxio.aroce


//qid -> id of question
//lk -> [LK]
//status -> 1: a correct answer have been given
//          0: no answwer have been given
//          -1: an incorrect answer have been given
class QuestionModel(val qid: String,
                    val lk: String,
                    val question: String,
                    val ansA: String,
                    val ansB: String,
                    val ansC: String,
                    val ansD: String,
                    val status: String
                    )