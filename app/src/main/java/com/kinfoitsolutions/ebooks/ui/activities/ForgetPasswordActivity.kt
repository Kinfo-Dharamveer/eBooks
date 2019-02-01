package com.kinfoitsolutions.ebooks.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.Utils.showSnackBar
import com.kinfoitsolutions.ebooks.ui.data.MessageEvent
import com.kinfoitsolutions.ebooks.ui.model.ForgetResponse.ForgetResponse
import com.kinfoitsolutions.ebooks.ui.model.VerifyOtp.VerifyOtpResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import kotlinx.android.synthetic.main.activity_forget_password.*
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : BaseActivity() {


    private lateinit var emailId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)


        btnSubmit.setOnClickListener {

            if (isNetworkConnected()){
                emailId = edEmailFPsw.text.toString()

                when {

                    emailId == "" -> {
                        edEmailFPsw.setError("Enter your email")

                    }
                    else -> {

                        val myDialog = Utils.showProgressDialog(this, "Email Sending...")

                        val stringHashMap = HashMap<String, String>()
                        stringHashMap.put("email", emailId)

                        val restClient = RestClient.getClient()

                        restClient.forget_password(stringHashMap).enqueue(object : Callback<ForgetResponse> {

                            override fun onResponse(call: Call<ForgetResponse>, response: Response<ForgetResponse>) {

                                if (response.isSuccessful) {


                                    if (response.body()!!.code.equals(100)) {

                                        myDialog.dismiss()

                                        val resOTP = response.body()!!.otp

                                        Toast.makeText(applicationContext, resOTP.toString(), Toast.LENGTH_SHORT).show()

                                        alertDialog(emailId,resOTP)


                                    } else if (response.code() == 401) {
                                        // Handle unauthorized
                                        Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()
                                        myDialog.dismiss()

                                    } else if (response.code() == 500) {
                                        // Handle unauthorized
                                        Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
                                        myDialog.dismiss()

                                    } else {
                                        //code 101 invalid credentials
                                        //  Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                        myDialog.dismiss()

                                    }

                                } else if (response.code() == 401) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Unauthorized", Toast.LENGTH_SHORT).show()

                                } else if (response.code() == 501) {
                                    // Handle unauthorized
                                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()

                                } else {
                                    //response is failed
                                    // Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                    myDialog.dismiss()
                                    edEmailFPsw!!.text.clear()

                                }


                            }

                            override fun onFailure(call: Call<ForgetResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                                myDialog.dismiss()
                                edEmailFPsw!!.text.clear()

                            }


                        })

                    }


                }
            }
            else{
                showSnackBar(this,"Check your internet connection",forgetPswRoot)

            }





        }
    }


    fun alertDialog(email: String,resOTP: Int) {

        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.forget_psw_popup, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)

        val close_popup = dialogView.findViewById(R.id.close_popup) as ImageView
        val enter_otp = dialogView.findViewById(R.id.enter_otp) as EditText
        val submitPsw = dialogView.findViewById(R.id.submitPsw) as Button

        enter_otp.isEnabled = false
        enter_otp.setText(resOTP.toString())

        submitPsw.setOnClickListener {


            if (isNetworkConnected()){

                val otp = enter_otp.text.toString()
                when {
                    otp == "" -> {
                        enter_otp.setError("Enter your OTP")

                    }
                    else -> {

                        val myDialogOTP = Utils.showProgressDialog(this, "OTP Verifing...")

                        val restClient = RestClient.getClient()

                        val stringHashMapOTP = HashMap<String, String>()
                        stringHashMapOTP.put("email", email)
                        stringHashMapOTP.put("otp", otp)

                        restClient.verifyOTp(stringHashMapOTP).enqueue(object : Callback<VerifyOtpResponse>{

                            override fun onResponse(call: Call<VerifyOtpResponse>, response: Response<VerifyOtpResponse>) {

                                if (response.isSuccessful){

                                    if (response.body()!!.code.equals(100)) {


                                        Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                        Log.e("Otpppppp","Dharamveer"+response.body()!!.msg)
                                        startActivity(Intent(this@ForgetPasswordActivity, ResetPasswordActivity::class.java))
                                        finish()
                                        myDialogOTP.dismiss()

                                    } else if (response.code() == 101) {
                                        // Handle unauthorized
                                        Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                        myDialogOTP.dismiss()

                                    } else if (response.code() == 500) {
                                        // Handle unauthorized
                                        Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
                                        myDialogOTP.dismiss()

                                    } else {
                                        //code 101 invalid credentials
                                        Toast.makeText(applicationContext, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                                        myDialogOTP.dismiss()

                                    }
                                }
                                else {
                                    myDialogOTP.dismiss()

                                }

                            }
                            override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {

                                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()

                            }


                        })

                    }

                }
            }
            else {
                showSnackBar(this,"Check your internet connection",forgetPswRoot)

            }



        }



        val alertDialog = dialogBuilder.create()
        alertDialog.show()


        close_popup.setOnClickListener {
            alertDialog.dismiss()
        }


    }




}