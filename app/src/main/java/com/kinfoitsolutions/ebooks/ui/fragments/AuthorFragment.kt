package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.drivingschool.android.AppConstants

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.AuthorsAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.authorsbooksresponse.GetAuthorsBooksSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_author.*
import kotlinx.android.synthetic.main.fragment_author.view.*
import kotlinx.android.synthetic.main.fragment_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorFragment : BaseFragment() {


    private lateinit var viewOfLayout: View
    private lateinit var authorsAdapter: AuthorsAdapter


    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View
    {

        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_author, parent, false)

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

                    Navigation.findNavController(author_container).navigate(R.id.action_authorFragment_to_authorBooksFragment);

                }

                override fun onResponse(call: Call<GetAuthorsBooksSuccess>, response: Response<GetAuthorsBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val allAuthors = response.body()!!.authors

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


}
