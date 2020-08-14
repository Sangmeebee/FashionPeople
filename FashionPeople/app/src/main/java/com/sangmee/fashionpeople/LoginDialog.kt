package com.sangmee.fashionpeople

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.my_dialog.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class LoginDialog(context : Context, title: String, kakaoBtnListener: View.OnClickListener) : Dialog(context) {
    val customTitle = title
    val mKakaoBtnListener = kakaoBtnListener
    init {
        setCancelable(false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.my_dialog)
        //dialog 제목
        dialog_title.text = customTitle
        //카카오버튼 클릭 시
        btn_kakao_login.setOnClickListener(mKakaoBtnListener)
            //btn_kakao_login.performClick()

        //안할래요 버튼 클릭시
        btn_no.onClick {
            dismiss()
        }


    }


}
