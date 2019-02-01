package com.kinfoitsolutions.ebooks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinfoitsolutions.ebooks.R
import kotlinx.android.synthetic.main.fragment_settings.view.*
import android.content.Intent
import com.kinfoitsolutions.ebooks.BuildConfig
import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.drivingschool.android.customviews.CustomTextView
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.model.ResetPassword.ResetPasswordResponse
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsFragment : BaseFragment() {


    private lateinit var viewOfLayout: View
    private lateinit var alertDialog: AlertDialog

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_settings, parent, false)
        setHasOptionsMenu(true)

        viewOfLayout.shareApp.setOnClickListener {
            shareAppCode()
        }

        viewOfLayout.rateApp.setOnClickListener {
            rateAppCode()
        }

        viewOfLayout.aboutUs.setOnClickListener {
            aboutUsDialog()
        }

        viewOfLayout.privacyPolicy.setOnClickListener {
            privacyDialog()
        }

        viewOfLayout.changePassword.setOnClickListener {
            changePswDialog()
        }


        return viewOfLayout

    }



    private fun changePswDialog() {

        val dialogBuilder = AlertDialog.Builder(context!!)
        // ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.change_psw, null)
        dialogBuilder.setView(dialogView)

        val cross_aboutus = dialogView.findViewById(R.id.cross_dialog) as ImageView

        val edEmail = dialogView.findViewById(R.id.edEmail) as EditText
        val new_psw = dialogView.findViewById(R.id.new_psw) as EditText
        val confirm_psw = dialogView.findViewById(R.id.confirm_psw) as EditText
        val submitPsw = dialogView.findViewById(R.id.submitPsw) as Button

        submitPsw.setOnClickListener {

            if (isNetworkConnected(context!!)){

                val email = edEmail.text.toString()
                val newPass = new_psw.text.toString()
                val confirmPass = confirm_psw.text.toString()

                when {
                    email == "" -> {
                        edEmail.setError("Enter your email")

                    }
                    newPass == "" -> {
                        new_psw.setError("Enter your new password")

                    }
                    confirmPass == "" -> {
                        confirm_psw.setError("Enter your confirm password")
                    }
                    !newPass.equals(confirmPass) -> {
                        Utils.showSnackBar(context, "Password doesn't match", activity!!.main_container)
                    }

                    else -> {

                        resetPasswordApi(email, newPass)

                    }

                }
            }
            else {

                showSnackBarFrag("Check your internet connection",activity!!.main_container)
            }



        }

        alertDialog = dialogBuilder.create()
        alertDialog.show()

        cross_aboutus.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun resetPasswordApi(email: String, password: String) {

        val myDialog = Utils.showProgressDialog(context, "Please wait......")

        val stringHashMap = HashMap<String, String>()
        stringHashMap.put("email", email)
        stringHashMap.put("password", password)

        val restClient = RestClient.getClient()

        restClient.reset_password(stringHashMap).enqueue(object : Callback<ResetPasswordResponse> {

            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {

                if (response.isSuccessful) {

                    if (response.body()!!.code.equals(100)) {
                        Utils.showSnackBar(context, response.body()!!.msg, activity!!.main_container)
                        myDialog.dismiss()
                        alertDialog.dismiss()


                    } else {
                        Utils.showSnackBar(context, response.body()!!.msg, activity!!.main_container)
                        myDialog.dismiss()

                    }


                } else if (response.code() == 401) {
                    // Handle unauthorized
                    Utils.showSnackBar(context, "Unauthorized", activity!!.main_container)
                    myDialog.dismiss()


                } else if (response.code() == 500) {
                    // Handle unauthorized
                    Utils.showSnackBar(context, "Server Error", activity!!.main_container)
                    myDialog.dismiss()

                } else {
                    //response is failed
                    Utils.showSnackBar(context, response.body()!!.msg, activity!!.main_container)

                    myDialog.dismiss()


                }


            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {

                Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                myDialog.dismiss()


            }


        })


    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(false)
        menu.findItem(R.id.action_filter).setVisible(false)
        super.onPrepareOptionsMenu(menu)

    }

    private fun privacyDialog() {


    }

    private fun aboutUsDialog() {


        val dialogBuilder = AlertDialog.Builder(context!!)
        // ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.about_us_layout, null)
        dialogBuilder.setView(dialogView)

        val cross_aboutus = dialogView.findViewById(R.id.cross_aboutus) as ImageView
        val about_ustext = dialogView.findViewById(R.id.about_ustext) as CustomTextView

        about_ustext.setText(
            "eBooks.com's Ebook Reader lets you read your favorite books on the go." +
                    " Choose from a massive collection of popular books that you can download in a jiffy."
        )

        alertDialog = dialogBuilder.create()
        alertDialog.show()

        cross_aboutus.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun shareAppCode() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Books")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }

    }

    private fun rateAppCode() {

        val uri = Uri.parse("market://details?id=" + context!!.getPackageName())
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.getPackageName())
                )
            )
        }


    }


}
