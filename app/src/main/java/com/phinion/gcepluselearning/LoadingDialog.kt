package com.phinion.gcepluselearning

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.phinion.gcepluselearning.databinding.LoadingDialogLayoutBinding

class LoadingDialog constructor(context: Context) {
    private lateinit var loadingDialog: AlertDialog
    private var loadingDialogBinding: LoadingDialogLayoutBinding = LoadingDialogLayoutBinding.inflate(
        LayoutInflater.from(context),
    )

    init {
        // Loading Dialog
        loadingDialog = AlertDialog.Builder(context)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    }
    fun showLoadingDialog() {
        loadingDialog.show()
    }
    fun dismissLoadingDialog() {
        loadingDialog.dismiss()
    }
}
