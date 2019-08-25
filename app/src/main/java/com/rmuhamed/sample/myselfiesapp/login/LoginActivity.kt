package com.rmuhamed.sample.myselfiesapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.gallery.GalleryActivity
import com.rmuhamed.sample.myselfiesapp.getViewModel
import com.rmuhamed.sample.myselfiesapp.repository.RepositoryFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = getViewModel(LoginViewModel::class.java, application, RepositoryFactory.Type.LOGIN)

        login_username_input_textField.editText?.doAfterTextChanged {
            viewModel.userName = it.toString()
        }

        viewModel.loginInProgressLiveData.observe(this, Observer {
            if (it) {
                progress.visibility = View.VISIBLE
                login_sign_in_button.isEnabled = false
            } else {
                progress.visibility = View.GONE
                login_sign_in_button.isEnabled = true
            }
        })

        viewModel.loginAvailableLiveData.observe(this, Observer {
            login_sign_in_button.isEnabled = it
        })

        viewModel.credentialsInvalidLiveData.observe(this, Observer { invalid ->
            if (invalid) {
                showUserNameError()
            }
        })

        viewModel.loginSuccessfulLiveData.observe(this, Observer {
            it?.let {
                this@LoginActivity.startActivity(
                    Intent().apply {
                        setClass(this@LoginActivity, GalleryActivity::class.java)
                    })
                //Should not be part of BackStack
                this.finish()
            }
        })

        login_sign_in_button.setOnClickListener {
            viewModel.verifyAccount()
        }
    }

    private fun showUserNameError() {
        login_username_input_textField.isErrorEnabled = true
        login_username_input_textField.error =
            getString(R.string.login_username_input_textfield_error)
    }
}