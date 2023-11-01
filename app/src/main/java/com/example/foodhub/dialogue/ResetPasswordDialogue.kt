package com.example.foodhub.dialogue

import android.app.Activity
import com.example.foodhub.databinding.ResetPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Activity.setUpBottomNavigationDialog(
    onSendClick :(String) -> Unit
){


    val dialog = BottomSheetDialog(this)
    var bottomSheetBinding : ResetPasswordBinding = ResetPasswordBinding.inflate(layoutInflater)
    dialog.setContentView(bottomSheetBinding.root)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    bottomSheetBinding.sendButton.setOnClickListener {
        val email=bottomSheetBinding.etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    bottomSheetBinding.cancelButton.setOnClickListener {
        dialog.dismiss()
    }

}