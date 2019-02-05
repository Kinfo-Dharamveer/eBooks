package com.kinfoitsolutions.ebooks.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_category.view.*
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.widget.*
import androidx.navigation.Navigation
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.responsemodel.CategoryBooksResponse.BooksCatSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.SearchBookAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.SearchCategoryAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchBooksResponse.SearchBookSuccess
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class CategoryFragment : BaseFragment() {

    private lateinit var viewOfLayout: View
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var searchCategoryAdapter: SearchCategoryAdapter

    // private lateinit var cateName: String

    var cateName = arrayOf<String>()

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_category, parent, false)
        setHasOptionsMenu(true)


        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.category_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.category_recyclerview.setItemAnimator(DefaultItemAnimator())

        getBooksByCat()

        return viewOfLayout

    }

    private fun getBooksByCat() {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.getBooksByCat(stringHashMap).enqueue(object : Callback<BooksCatSuccess>,
                CategoryAdapter.mBookCatgoryClickListner {

                override fun mCatBookClick(v: View?, position: Int) {

                    Navigation.findNavController(categry_container)
                        .navigate(R.id.action_categoryFragment_to_allCategoryBooksFragment);

                }

                override fun onResponse(call: Call<BooksCatSuccess>, response: Response<BooksCatSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {

                            val allBooksCat = response.body()!!.categories

                            for (i in 0 until response.body()!!.categories.size) {
                                cateName = arrayOf(response.body()!!.categories.get(i).name)
                            }

                            categoryAdapter = CategoryAdapter(allBooksCat, context, this)
                            viewOfLayout.category_recyclerview.setAdapter(categoryAdapter)

                            categoryAdapter.notifyDataSetChanged()

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

                override fun onFailure(call: Call<BooksCatSuccess>, t: Throwable) {

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
                val dialogView = inflater.inflate(R.layout.cat_search_dialog, null)
                dialogBuilder.setView(dialogView)

                // val spinner = dialogView.findViewById(R.id.cat_spinner) as Spinner
                val cross_dialog = dialogView.findViewById(R.id.cross_dialog) as ImageView
                val submit_dialog = dialogView.findViewById(R.id.submit_dialog) as Button

                /*   val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, cateName)
                   aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                   //Setting the ArrayAdapter data on the Spinner
                   spinner.setAdapter(aa)*/


                val adapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, cateName)
                //Getting the instance of AutoCompleteTextView
                val actv = dialogView.findViewById(R.id.cat_search) as AutoCompleteTextView
                actv.threshold = 1//will start working from first character
                actv.setAdapter<ArrayAdapter<String>>(adapter)//setting the adapter data into the AutoCompleteTextView
                actv.setTextColor(Color.RED)
                Log.e("texttttttttt", "" + actv.text.toString());

                val alertDialog = dialogBuilder.create()
                alertDialog.show()

                submit_dialog.setOnClickListener {
                    searchApiCall(actv.text.toString())
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

    private fun searchApiCall(searchText: String) {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = java.util.HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("term", searchText)

            restClient.searchBook(stringHashMap)
                .enqueue(object : Callback<SearchBookSuccess>, SearchBookAdapter.mSearchClickListener,
                    SearchCategoryAdapter.mSearchCatClickListener {
                    override fun searchBookCat(v: View?, position: Int) {

                        Navigation.findNavController(categry_container)
                            .navigate(R.id.action_categoryFragment_to_allCategoryBooksFragment);

                    }

                    override fun mSearchClick(v: View?, position: Int) {
                        Navigation.findNavController(homeFragContainer)
                            .navigate(R.id.action_homeFragment_to_bookReadingFragment);
                    }

                    override fun onResponse(call: Call<SearchBookSuccess>, response: Response<SearchBookSuccess>) {

                        if (response.isSuccessful) {

                            if (response.body()!!.code.equals("100")) {


                                val searchCategory = response.body()!!.books

                                searchCategoryAdapter = SearchCategoryAdapter(searchCategory, context, this)
                                viewOfLayout.category_recyclerview.setAdapter(searchCategoryAdapter)

                                searchCategoryAdapter.notifyDataSetChanged()

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


                    override fun onFailure(call: Call<SearchBookSuccess>, t: Throwable) {

                        Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                        myDialog.dismiss()

                    }
                })


        } else {
            showSnackBarFrag("Check your internet connection", activity!!.main_container)

        }

    }


}
