package com.wanxio.wanxio.aroce

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log


class ResetConfirmDialogFragment: DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setMessage(R.string.reset_confirm_dialog_message)
                .setPositiveButton(R.string.reset_confirm_dialog_ok, {_, _ ->
                    Log.d("ResetConfirmDialog", "User click ok")
                    QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_A).clearAllStatus()
                    QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_B).clearAllStatus()
                    QuestionDBHelper(this.context, QuestionDBContract.Entry.TABLE_NAME_LEVEL_C).clearAllStatus()
                    AStatus.PracticeStatus.currentQid = 0
                })
                .setNegativeButton(R.string.reset_confirm_dialog_cancel, {_, _ ->
                    Log.d("ResetConfirmDialog", "User click cancel")
                })
                .create()
    }
}