package com.kinfoitsolutions.ebooks.ui.fragments


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.drivingschool.android.AppConstants
import com.drivingschool.android.customviews.CustomTextView

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.DownloadAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.DownloadSearchAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess.AllSearchDataSuccess
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.GetAllBooksSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_download.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class DownloadFragment : BaseFragment() {



    private lateinit var viewOfLayout: View

    private lateinit var bAdapter: DownloadAdapter
    private lateinit var downloadSearchAdapter: DownloadSearchAdapter

    private lateinit var bookDownloadNameArray: ArrayList<String>


    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_download, parent, false)
        setHasOptionsMenu(true)
        getActivity()!!.onBackPressed()

        bookDownloadNameArray = ArrayList<String>()


        val layoutManager = GridLayoutManager(context,3)
        viewOfLayout.downloadRecyclerView.setLayoutManager(layoutManager)
        viewOfLayout.downloadRecyclerView.setItemAnimator(DefaultItemAnimator())

        getAllBooksApi()



        return viewOfLayout
    }


    private fun getAllBooksApi() {


        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.get_books(stringHashMap).enqueue(object : Callback<GetAllBooksSuccess>,
                DownloadAdapter.mDownloadListener {


                override fun mPdfDownload(v: View?, position: Int, pdfLink: String?) {

                    showSnackBarFrag(pdfLink + position, activity!!.main_container)

                     startActivity( Intent(Intent.ACTION_VIEW, Uri.parse(pdfLink)))

                }

                override fun onResponse(call: Call<GetAllBooksSuccess>, response: Response<GetAllBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val allBooksDownload = response.body()!!.recomended

                            var bookName: String

                            for (i in 0 until response.body()!!.recomended.size) {
                                bookName = response.body()!!.recomended.get(i).name
                                bookDownloadNameArray.add(bookName)

                            }


                            bAdapter = DownloadAdapter(allBooksDownload,context,this)
                            viewOfLayout.downloadRecyclerView.setAdapter(bAdapter)


                            bAdapter.notifyDataSetChanged()


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
        menu.findItem(R.id.action_category).setVisible(true)
        super.onPrepareOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.getItemId()) {

            R.id.action_category -> {

                val dialogBuilder = AlertDialog.Builder(context!!)
                // ...Irrelevant code for customizing the buttons and title
                val inflater = this.layoutInflater
                val dialogView = inflater.inflate(R.layout.search_dialog, null)
                dialogBuilder.setView(dialogView)

                val searchDialogTitle = dialogView.findViewById(R.id.searchDialogTitle) as CustomTextView
                val cross_dialog = dialogView.findViewById(R.id.cross_dialog) as ImageView
                val submit_dialog = dialogView.findViewById(R.id.submit_dialog) as Button

                searchDialogTitle.setText("Search Book")

                val adapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, bookDownloadNameArray)
                //Getting the instance of AutoCompleteTextView
                val actv = dialogView.findViewById(R.id.cat_search) as AutoCompleteTextView
                actv.threshold = 1//will start working from first character
                actv.setAdapter<ArrayAdapter<String>>(adapter)//setting the adapter data into the AutoCompleteTextView
                actv.setTextColor(Color.RED)
                Log.e("texttttttttt", "" + actv.text.toString());

                val alertDialog = dialogBuilder.create()
                alertDialog.show()

                submit_dialog.setOnClickListener {
                    searchDownloadBook(actv.text.toString())
                    alertDialog.dismiss()

                }


                cross_dialog.setOnClickListener {
                    alertDialog.dismiss()
                }

                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun searchDownloadBook(searchText: String) {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("term", searchText)
            stringHashMap.put("type", "1")

            restClient.searchAllData(stringHashMap).enqueue(object : Callback<AllSearchDataSuccess>,
                DownloadSearchAdapter.mDownloadSearchListener {


                override fun mPdfSearchDownload(v: View?, position: Int, pdfLink: String?) {
                    showSnackBarFrag(pdfLink + position, activity!!.main_container)

                    startActivity( Intent(Intent.ACTION_VIEW, Uri.parse(pdfLink)))

                }


                override fun onResponse(call: Call<AllSearchDataSuccess>, response: Response<AllSearchDataSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {

                            val searchAllBooks = response.body()!!.data

                            downloadSearchAdapter = DownloadSearchAdapter(searchAllBooks, context, this)
                            viewOfLayout.downloadRecyclerView.setAdapter(downloadSearchAdapter)

                            downloadSearchAdapter.notifyDataSetChanged()

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


                override fun onFailure(call: Call<AllSearchDataSuccess>, t: Throwable) {

                    Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                    myDialog.dismiss()

                }
            })


        } else {
            showSnackBarFrag("Check your internet connection", activity!!.main_container)

        }

    }



}
