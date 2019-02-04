package com.kinfoitsolutions.ebooks.ui.fragments


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.GetAllBooksSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.kinfoitsolutions.ebooks.ui.adapters.SearchBookAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchBooksResponse.SearchBookSuccess


class HomeFragment : BaseFragment() {

    private lateinit var viewOfLayout: View

    //  recommanded data
    private lateinit var bAdapter: RecommandedRecycleAdapter
    private lateinit var searchAdapter: SearchBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_home, parent, false)


        //recommanded recyclerview code is here
        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.recommanded_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.recommanded_recyclerview.setItemAnimator(DefaultItemAnimator())


        //Recommended Apps Data
        getAllBooksApi()

        //   top 50 recyclerview code is here
        val layoutManager1 = GridLayoutManager(context, 3)
        viewOfLayout.top50_books_recyclerview.setLayoutManager(layoutManager1)
        viewOfLayout.top50_books_recyclerview.setItemAnimator(DefaultItemAnimator())


        // bAdapter1 = RecommandedRecycleAdapter(context, recommandedModelClasses1, this)
        //  viewOfLayout.top50_books_recyclerview.setAdapter(bAdapter1)


        viewOfLayout.viewAllRecommend.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllFragment);

        }

        viewOfLayout.viewAllToday.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllFragment);

        }

        return viewOfLayout

    }

    private fun getAllBooksApi() {


        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("orderby", "0")

            restClient.get_books(stringHashMap).enqueue(object : Callback<GetAllBooksSuccess>,
                RecommandedRecycleAdapter.mClickListener {

                override fun mClick(v: View?, position: Int) {
                    Navigation.findNavController(homeFragContainer)
                        .navigate(R.id.action_homeFragment_to_bookReadingFragment);

                }

                override fun onResponse(call: Call<GetAllBooksSuccess>, response: Response<GetAllBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            val allBooks = response.body()!!.books

                            bAdapter = RecommandedRecycleAdapter(context, allBooks, this)
                            viewOfLayout.recommanded_recyclerview.setAdapter(bAdapter)



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
        menu.findItem(R.id.action_category).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(false)
        menu.findItem(R.id.action_filter).setVisible(false)
        menu.findItem(R.id.action_search).setVisible(true)
        super.onPrepareOptionsMenu(menu)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView

        // MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItemCompat.SHOW_AS_ACTION_IF_ROOM)
        //  MenuItemCompat.setActionView(searchItem, searchView)


        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity()!!.getComponentName()))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    // Toast.makeText(context,"onQueryTextChange"+query,Toast.LENGTH_SHORT).show()
                    searchApiCall(query)

                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    //Toast.makeText(context,"onQueryTextSubmit"+newText,Toast.LENGTH_SHORT).show()

                    searchApiCall(newText)

                    //bAdapter.getFilter().filter(newText)
                    return true
                }
            })

            super.onCreateOptionsMenu(menu, inflater)

        }


    }

    private fun searchApiCall(searchText: String) {

        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))
            stringHashMap.put("term", searchText)

            restClient.searchBook(stringHashMap).enqueue(object : Callback<SearchBookSuccess>, SearchBookAdapter.mSearchClickListener {

                override fun mSearchClick(v: View?, position: Int) {
                    Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_bookReadingFragment);
                }

                override fun onResponse(call: Call<SearchBookSuccess>, response: Response<SearchBookSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {

                            val searchAllBooks = response.body()!!.books

                            searchAdapter = SearchBookAdapter(searchAllBooks, context, this)
                            viewOfLayout.recommanded_recyclerview.setAdapter(searchAdapter)

                            searchAdapter.notifyDataSetChanged()

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


