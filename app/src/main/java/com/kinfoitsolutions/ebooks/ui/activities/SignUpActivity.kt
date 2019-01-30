package com.kinfoitsolutions.ebooks.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var phoneNo: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userName = edNameSignUp.text.toString()
        email = edEmailSignUp.text.toString()
        password = edPswSignUp.text.toString()
        phoneNo = edPhoneSignUp.text.toString()

        btnRegister.setOnClickListener {


            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

        }
        btnLoginSignUp.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

        }

    }
}
