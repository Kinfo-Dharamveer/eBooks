package com.kinfoitsolutions.ebooks.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.data.MessageEvent
import com.kinfoitsolutions.ebooks.ui.model.RegisterResponse.RegisterResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : BaseActivity() {

    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var phoneNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnRegister.setOnClickListener {

            userName = edNameSignUp.text.toString().trim()
            email = edEmailSignUp.text.toString().trim()
            password = edPswSignUp.text.toString().trim()
            phoneNo = edPhoneSignUp.text.toString().trim()

            when {

                userName == "" -> {
                    edNameSignUp.setError("Enter your Name")

                }
                email == "" -> {
                    edEmailSignUp.setError("Enter your email")

                }
                password == "" -> {
                    edPswSignUp.setError("Enter your password")

                }

                phoneNo == "" -> {
                    edPhoneSignUp.setError("Enter your phone no")

                }

                else -> {

                    val myDialog = Utils.showProgressDialog(this, "Progressing......")

                    val stringHashMap  = HashMap<String, String>()
                    stringHashMap.put("email", email)
                    stringHashMap.put("password", password)
                    stringHashMap.put("phone", phoneNo)
                    stringHashMap.put("name", userName)

                    val restClient = RestClient.getClient()

                    restClient.register(stringHashMap).enqueue(object : Callback<RegisterResponse>{

                        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                            if (response.isSuccessful){

                                if (response.body()!!.code.equals(100)) {
                                    myDialog.dismiss()
                                    Toast.makeText(applicationContext,response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                    Hawk.put(AppConstants.TOKEN, response.body()!!.token)
                                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                }

                                else if (response.code() == 401) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()

                                }
                                else if (response.code() == 500) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()

                                }

                                else
                                {
                                    Toast.makeText(applicationContext,response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                    myDialog.dismiss()

                                }
                            }
                            else {

                                Toast.makeText(applicationContext,response.body()!!.msg, Toast.LENGTH_SHORT).show()

                                myDialog.dismiss()
                                edNameSignUp!!.text.clear()
                                edEmailSignUp!!.text.clear()
                                edPswSignUp!!.text.clear()
                                edPhoneSignUp!!.text.clear()


                            }

                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

                            Toast.makeText(applicationContext,t.toString(), Toast.LENGTH_SHORT).show()

                            myDialog.dismiss()
                            edNameSignUp!!.text.clear()
                            edEmailSignUp!!.text.clear()
                            edPswSignUp!!.text.clear()
                            edPhoneSignUp!!.text.clear()

                        }


                    })


                }
            }




        }
        btnLoginSignUp.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

        }

    }

    @Subscribe
    fun onEvent(status: MessageEvent) {

        if (status.status.contains("NOT_CONNECT")) {

            Utils.showNoInternetSnackbar("You are offline", signUpLayout, "offline")

        } else {

            Utils.showNoInternetSnackbar("You are online", signUpLayout, "online")

        }
    }




}
