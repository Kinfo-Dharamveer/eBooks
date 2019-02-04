package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.drivingschool.android.AppConstants

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.LatestAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.RecommandedModelClass
import com.kinfoitsolutions.ebooks.ui.responsemodel.latestBooks.LatestBooksSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_latest.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LatestFragment : BaseFragment() {



    private lateinit var viewOfLayout: View

    private lateinit var latestAdapter: LatestAdapter


    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_latest, parent, false)

        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.latest_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.latest_recyclerview.setItemAnimator(DefaultItemAnimator())

        getLatestBooks()

        return viewOfLayout

    }

    private fun getLatestBooks() {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.getLatestBooks(stringHashMap).enqueue(object : Callback<LatestBooksSuccess> {

                override fun onResponse(call: Call<LatestBooksSuccess>, response: Response<LatestBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val latestBooksList = response.body()!!.books

                            latestAdapter = LatestAdapter(latestBooksList,context)
                            viewOfLayout.latest_recyclerview.setAdapter(latestAdapter)


                            latestAdapter.notifyDataSetChanged()

                            Utils.showSnackBar(context, "Success", activity!!.main_container)
                            myDialog.dismiss()

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
                        Utils.showSnackBar(context, "Failed", activity!!.main_container)
                        myDialog.dismiss()


                    }
                }


                override fun onFailure(call: Call<LatestBooksSuccess>, t: Throwable) {

                    Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                    myDialog.dismiss()

                }
            })


        } else {
            showSnackBarFrag("Check your internet connection", activity!!.main_container)

        }
    }


}
