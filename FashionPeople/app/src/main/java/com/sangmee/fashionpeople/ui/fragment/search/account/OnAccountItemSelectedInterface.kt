package com.sangmee.fashionpeople.ui.fragment.search.account

import com.sangmee.fashionpeople.data.model.FUser

interface OnAccountItemSelectedInterface {
    fun onItemSelected(user: FUser)
    fun onClickCancelBtn(user: FUser)
}
