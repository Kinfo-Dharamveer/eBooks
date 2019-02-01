package com.kinfoitsolutions.ebooks.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.model.LoginResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {


            email = edEmail.text.toString()
            password = edPsw.text.toString()

            when {

                email == "" -> {
                    edEmail.setError("Enter your email")

                }
                password == "" -> {
                    edPsw.setError("Enter your password")

                }
                else -> {

                    val myDialog = Utils.showProgressDialog(this, "Progressing......")

                    val stringHashMap = HashMap<String, String>()
                    stringHashMap.put("email", email)
                    stringHashMap.put("password", password)

                    val restClient = RestClient.getClient()

                    restClient.login(stringHashMap).enqueue(object : Callback<LoginResponse> {

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                            if (response.isSuccessful) {

                                if (response.body()!!.code.equals(100)) {
                                    Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                    Hawk.put(AppConstants.TOKEN, response.body()!!.token)
                                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                                    finish()
                                    myDialog.dismiss()

                                }

                                else if (response.code() == 401) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()

                                }
                                else if (response.code() == 500) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()

                                }

                                else {
                                    //code 101 invalid credentials
                                    Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                    myDialog.dismiss()

                                }
                            }
                            else if (response.code() == 401) {
                                // Handle unauthorized
                                Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()

                            }
                            else if (response.code() == 501) {
                                // Handle unauthorized
                                Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()

                            }

                            else {
                                //response is failed
                                Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                myDialog.dismiss()
                                edEmail!!.text.clear()
                                edPsw!!.text.clear()

                            }

                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                            Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                            myDialog.dismiss()
                            edEmail!!.text.clear()
                            edPsw!!.text.clear()

                        }

                    })

                }
            }
        }

        txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))

        }

        forgotPsw.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))

        }

        btnSkip.setOnClickListener {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }

    }
}
