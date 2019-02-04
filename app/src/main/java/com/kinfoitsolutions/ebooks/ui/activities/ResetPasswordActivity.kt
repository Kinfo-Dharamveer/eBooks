package com.kinfoitsolutions.ebooks.ui.activities

import android.os.Bundle
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.Utils.showSnackBar
import com.kinfoitsolutions.ebooks.ui.responsemodel.ResetPassword.ResetPasswordResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_reset_password.*


class ResetPasswordActivity : BaseActivity() {


    private lateinit var emailId: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)


        btnSubmit.setOnClickListener {

            if (isNetworkConnected()){
                emailId = edEmail.text.toString().trim()
                newPassword = edNewPsw.text.toString().trim()
                confirmPassword = edConfirmPsw.text.toString().trim()


                when {


                    emailId == "" -> {
                        edEmail.setError("Enter email")
                    }
                    newPassword == "" -> {
                        edNewPsw.setError("Enter email")
                    }
                    !newPassword.equals(confirmPassword) -> {
                        Utils.showSnackBar(this,"Password doesn't match",resetPswLayout)
                    }

                    else -> {
                        val myDialog = Utils.showProgressDialog(this, "Please wait......")

                        val stringHashMap = HashMap<String, String>()
                        stringHashMap.put("email", emailId)
                        stringHashMap.put("password", newPassword)

                        val restClient = RestClient.getClient()

                        restClient.reset_password(stringHashMap).enqueue(object : Callback<ResetPasswordResponse> {

                            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {

                                if (response.isSuccessful) {

                                    if (response.body()!!.code.equals(100)){
                                        Utils.showSnackBar(this@ResetPasswordActivity,response.body()!!.msg,resetPswLayout)
                                        myDialog.dismiss()

                                    }
                                    else {
                                        Utils.showSnackBar(this@ResetPasswordActivity,response.body()!!.msg,resetPswLayout)
                                        myDialog.dismiss()

                                    }


                                } else if (response.code() == 401) {
                                    // Handle unauthorized
                                    Utils.showSnackBar(this@ResetPasswordActivity,"Unauthorized",resetPswLayout)
                                    myDialog.dismiss()


                                } else if (response.code() == 500) {
                                    // Handle unauthorized
                                    Utils.showSnackBar(this@ResetPasswordActivity,"Server Error",resetPswLayout)
                                    myDialog.dismiss()

                                } else {
                                    //response is failed
                                    Utils.showSnackBar(this@ResetPasswordActivity,response.body()!!.msg,resetPswLayout)

                                    myDialog.dismiss()
                                    edEmail!!.text.clear()
                                    edNewPsw!!.text.clear()

                                }


                            }

                            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {

                                Utils.showSnackBar(this@ResetPasswordActivity,t.toString(),resetPswLayout)

                                myDialog.dismiss()
                                edEmail.text.clear()
                                edNewPsw.text.clear()
                                edConfirmPsw.text.clear()

                            }


                        })

                    }
                }
            }
            else {
                showSnackBar(this,"Check your internet connection",resetPswLayout)

            }




        }

    }







}
