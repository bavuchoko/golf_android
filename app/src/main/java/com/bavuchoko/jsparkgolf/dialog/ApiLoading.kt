package com.bavuchoko.jsparkgolf.dialog

import android.app.Dialog
import android.content.Context
import com.bavuchoko.jsparkgolf.R

class ApiLoading(context: Context): Dialog(context) {
    init {
        setContentView(R.layout.api_loading)
        setCancelable(false)
    }

    fun showLoading() {
        show()
    }

    fun disappear() {
        dismiss()
    }
}