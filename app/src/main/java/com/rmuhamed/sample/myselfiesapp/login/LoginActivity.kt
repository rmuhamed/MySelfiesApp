package com.rmuhamed.sample.myselfiesapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.gallery.GalleryActivity
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

        viewModel.loginSuccessfulLiveData.observe(this, Observer { success ->
            if (success) {
                this@LoginActivity.startActivity(
                    Intent(
                        this@LoginActivity,
                        GalleryActivity::class.java
                    )
                )
            } else {
                //Show error
                login_username_inputtextfield.error = "Invalid Username"
            }
        })

        login_sign_in_button.setOnClickListener {
            viewModel.verifyAccount()
        }
    }
}