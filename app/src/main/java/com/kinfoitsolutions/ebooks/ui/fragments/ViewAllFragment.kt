package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.drivingschool.android.AppConstants

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.BookListRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.model.GetAllBooksResponse.GetAllBooksSuccess
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_view_all.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap


class ViewAllFragment : BaseFragment() {




    private lateinit var viewOfLayout: View
    private lateinit var bAdapter: BookListRecycleAdapter



    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        viewOfLayout  = inflater.inflate(R.layout.fragment_view_all, parent, false)
        setHasOptionsMenu(true)


        val layoutManager = LinearLayoutManager(context)
        viewOfLayout.recyclerView.setLayoutManager(layoutManager)
        viewOfLayout.recyclerView.setItemAnimator(DefaultItemAnimator())



        viewAllBooksApi()



        return viewOfLayout

    }


    private fun viewAllBooksApi() {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("orderby", "0")

            restClient.get_books(stringHashMap).enqueue(object : Callback<GetAllBooksSuccess>,
                RecommandedRecycleAdapter.mClickListener {

                override fun mClick(v: View?, position: Int) {
                    Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_bookReadingFragment);

                }

                override fun onResponse(call: Call<GetAllBooksSuccess>, response: Response<GetAllBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val allBooks = response.body()!!.books

                            bAdapter = BookListRecycleAdapter(context, allBooks)
                            viewOfLayout.recyclerView.setAdapter(bAdapter)

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
        menu.findItem(R.id.action_filter).setVisible(true)
        super.onPrepareOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_filter -> {
                Toast.makeText(context,"Filtered", Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }



}
