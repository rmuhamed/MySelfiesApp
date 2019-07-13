package com.rmuhamed.sample.myselfiesapp.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rmuhamed.sample.myselfiesapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login_username_inputtextfield.editText?.doAfterTextChanged {
            viewModel.userName = it.toString()
        }

        viewModel.showProgressLiveData.observe(this, Observer {
            progress.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loginAvailableLiveData.observe(this, Observer {
            login_sign_in_button.isEnabled = it
        })

        login_sign_in_button.setOnClickListener {
            viewModel.verifyAccount()
        }
    }

}