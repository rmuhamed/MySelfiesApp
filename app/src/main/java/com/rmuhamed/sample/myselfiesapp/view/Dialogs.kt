package com.rmuhamed.sample.myselfiesapp.view

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.rmuhamed.sample.myselfiesapp.R

fun dialogForPictureMetadata(@LayoutRes layoutId: Int, context: Context, action: (String, String, String) -> Unit)
        : AppCompatDialog {
    val dialogView = LayoutInflater.from(context).inflate(layoutId, null)

    val dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setCancelable(true)
        .create()

    var name: String = "No name"
    var title: String = "Default title"
    var description: String = "Default description"

    val nameField = dialogView.findViewById<TextInputLayout>(R.id.picture_name_input_textField).editText
    nameField?.doAfterTextChanged {
        name = it.toString()
    }
    val titleField = dialogView.findViewById<TextInputLayout>(R.id.picture_metadata_title_input_textField).editText
    titleField?.doAfterTextChanged {
        title = it.toString()
    }
    val descriptionField =
        dialogView.findViewById<TextInputLayout>(R.id.picture_metadata_description_input_field).editText
    descriptionField?.doAfterTextChanged {
        description = it.toString()
    }

    dialogView.findViewById<MaterialButton>(R.id.picture_metadata_upload_button)
        .setOnClickListener {
            action.invoke(name, title, description)
            dialog.dismiss()
        }

    return dialog
}