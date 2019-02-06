package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.drivingschool.android.AppConstants

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.Top50Adapter
import com.kinfoitsolutions.ebooks.ui.adapters.Top50BookListAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.GetAllBooksSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_view_all.view.*
import kotlinx.android.synthetic.main.fragment_view_all_top50.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class ViewAllTop50Fragment : BaseFragment() {

    private lateinit var viewOfLayout: View
    // top50 data
    private lateinit var top50BookListAdapter: Top50BookListAdapter

    override fun provideYourFragmentView(
        inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        viewOfLayout = inflater.inflate(R.layout.fragment_view_all_top50, parent, false)
        setHasOptionsMenu(true)


        val layoutManager = LinearLayoutManager(context)
        viewOfLayout.recyclerViewTop50View.setLayoutManager(layoutManager)
        viewOfLayout.recyclerViewTop50View.setItemAnimator(DefaultItemAnimator())


        getAllTop50Books()

        return viewOfLayout
    }

    private fun getAllTop50Books() {


        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.get_books(stringHashMap).enqueue(object : Callback<GetAllBooksSuccess>,
                Top50BookListAdapter.mAllTop50BooksClickListener {


                override fun mClickTop50List(v: View?, pos: Int, bookFile: String?) {
                    Hawk.put(AppConstants.PDF_FILE, bookFile)

                    Navigation.findNavController(homeFragContainer)
                        .navigate(R.id.action_viewAllTop50Fragment_to_webViewFragment)
                }


                override fun onResponse(call: Call<GetAllBooksSuccess>, response: Response<GetAllBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {



                            //Top 50 Data
                            val allTop50Books = response.body()!!.top50
                            top50BookListAdapter = Top50BookListAdapter(context,allTop50Books,  this)
                            viewOfLayout.recyclerViewTop50View.setAdapter(top50BookListAdapter)
                            top50BookListAdapter.notifyDataSetChanged()


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


                override fun onFailure(call: Call<GetAllBooksSuccess>, t: Throwable) {

                    Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                    myDialog.dismiss()

                }
            })


        } else {
            showSnackBarFrag("Check your internet connection", activity!!.main_container)

        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(false)
        menu.findItem(R.id.action_filter).setVisible(false)
        super.onPrepareOptionsMenu(menu)

    }

}
