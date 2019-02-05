package com.kinfoitsolutions.ebooks.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.drivingschool.android.AppConstants
import com.drivingschool.android.customviews.CustomTextView

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.AuthorsAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.SearchAuthorsAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.AuthorsBooksResponse.GetAuthorsBooksSuccess
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchAuthors.SearchAuthorsSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_author.*
import kotlinx.android.synthetic.main.fragment_author.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorFragment : BaseFragment() {


    private lateinit var viewOfLayout: View
    private lateinit var authorsAdapter: AuthorsAdapter
    private lateinit var searchAuthorsAdapter: SearchAuthorsAdapter

    private lateinit var authorsNameArray: ArrayList<String>

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View
    {

        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_author, parent, false)
        setHasOptionsMenu(true)

        authorsNameArray = ArrayList<String>()

        val gridLayouManager = GridLayoutManager(context,3)

        viewOfLayout.author_recyclerview.layoutManager = gridLayouManager
        viewOfLayout.author_recyclerview.itemAnimator = DefaultItemAnimator()

        getBookByAuthors()

        return viewOfLayout
    }



    private fun getBookByAuthors() {


        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.getBooksByAuthors(stringHashMap).enqueue(object : Callback<GetAuthorsBooksSuccess>,
                AuthorsAdapter.mAuthorRowClickInterface {

                override fun mAuthorRowClick(c: View?, Pos: Int, id: Int?) {

                 //   var bundle = bundleOf("Dharamveer" to username.text.toString())

                    Navigation.findNavController(author_container).
                        navigate(R.id.action_authorFragment_to_authorBooksFragment)

                }

                override fun onResponse(call: Call<GetAuthorsBooksSuccess>, response: Response<GetAuthorsBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val allAuthors = response.body()!!.authors

                            var authorsName: String

                            for (i in 0 until response.body()!!.authors.size) {
                                 authorsName = response.body()!!.authors.get(i).name
                                authorsNameArray.add(authorsName)

                            }


                            authorsAdapter = AuthorsAdapter(allAuthors,context,this)
                            viewOfLayout.author_recyclerview.adapter = authorsAdapter

                            authorsAdapter.notifyDataSetChanged()

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


                override fun onFailure(call: Call<GetAuthorsBooksSuccess>, t: Throwable) {

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

                searchDialogTitle.setText("Search Authors By Name ")

                val adapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, authorsNameArray)
                //Getting the instance of AutoCompleteTextView
                val actv = dialogView.findViewById(R.id.cat_search) as AutoCompleteTextView
                actv.threshold = 1//will start working from first character
                actv.setAdapter<ArrayAdapter<String>>(adapter)//setting the adapter data into the AutoCompleteTextView
                actv.setTextColor(Color.RED)
                Log.e("texttttttttt", "" + actv.text.toString());

                val alertDialog = dialogBuilder.create()
                alertDialog.show()

                submit_dialog.setOnClickListener {
                    searchAuthors(actv.text.toString())
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

    private fun searchAuthors(searchText: String) {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = java.util.HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("term", searchText)
            stringHashMap.put("type", "3")

            restClient.searchAuthor(stringHashMap).enqueue(object : Callback<SearchAuthorsSuccess>,
                SearchAuthorsAdapter.mSearchAuthorInterface {


                override fun mClickAuthorRow(v: View?, pos: Int, id: Int?) {

                    Navigation.findNavController(author_container).
                        navigate(R.id.action_authorFragment_to_authorBooksFragment)

                }

                override fun onResponse(call: Call<SearchAuthorsSuccess>, response: Response<SearchAuthorsSuccess>) {

                        if (response.isSuccessful) {

                            if (response.body()!!.code.equals("100")) {

                                val searchCategory = response.body()!!.data

                                searchAuthorsAdapter = SearchAuthorsAdapter(searchCategory, context, this)
                                viewOfLayout.author_recyclerview.setAdapter(searchAuthorsAdapter)

                                searchAuthorsAdapter.notifyDataSetChanged()

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


                    override fun onFailure(call: Call<SearchAuthorsSuccess>, t: Throwable) {

                        Utils.showSnackBar(context, t.toString(), activity!!.main_container)
                        myDialog.dismiss()

                    }
                })


        } else {
            showSnackBarFrag("Check your internet connection", activity!!.main_container)

        }

    }


}
