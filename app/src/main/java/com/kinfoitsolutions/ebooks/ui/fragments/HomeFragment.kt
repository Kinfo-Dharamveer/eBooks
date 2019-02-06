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
import com.kinfoitsolutions.ebooks.ui.adapters.Top50Adapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess.AllSearchDataSuccess
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchDataResponse.SearchDataSuccess


class HomeFragment : BaseFragment() {

    private lateinit var viewOfLayout: View

    //  recommanded data
    private lateinit var bAdapter: RecommandedRecycleAdapter
    private lateinit var searchAdapter: SearchBookAdapter

    // top50 data
    private lateinit var top50Adapter: Top50Adapter


    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_home, parent, false)
        setHasOptionsMenu(true)


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



        viewOfLayout.viewAllRecommend.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllFragment)

        }

        viewOfLayout.viewAllToday.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllTop50Fragment)

        }

        return viewOfLayout

    }

    private fun getAllBooksApi() {


        if (isNetworkConnected(context!!)) {

            val myDialog = Utils.showProgressDialog(context, "Please wait......")

            val restClient = RestClient.getClient()

            val stringHashMap = HashMap<String, String>()
            stringHashMap.put("token", Hawk.get(AppConstants.TOKEN))

            restClient.get_books(stringHashMap).enqueue(object : Callback<GetAllBooksSuccess>,
                RecommandedRecycleAdapter.mClickListener, Top50Adapter.mTop50ClickListener {
                override fun mTop50Click(c: View?, pos: Int, bookFile: String?) {


                    Hawk.put(AppConstants.PDF_FILE, bookFile)

                    Navigation.findNavController(homeFragContainer)
                        .navigate(R.id.action_homeFragment_to_webViewFragment)

                }

                override fun mClick(v: View?, position: Int, bookFile: String?) {

                    Hawk.put(AppConstants.PDF_FILE, bookFile)

                    Navigation.findNavController(homeFragContainer)
                        .navigate(R.id.action_homeFragment_to_webViewFragment)


                }

                override fun onResponse(call: Call<GetAllBooksSuccess>, response: Response<GetAllBooksSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {


                            //Recommended Data
                            val allRecommededBooks = response.body()!!.recomended
                            bAdapter = RecommandedRecycleAdapter(context, allRecommededBooks, this)
                            viewOfLayout.recommanded_recyclerview.setAdapter(bAdapter)

                            bAdapter.notifyDataSetChanged()

                            //Top 50 Data
                            val allTop50Books = response.body()!!.top50
                            top50Adapter = Top50Adapter(allTop50Books, context, this)
                            viewOfLayout.top50_books_recyclerview.setAdapter(top50Adapter)
                            top50Adapter.notifyDataSetChanged()


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
            stringHashMap.put("type", "1")


            restClient.searchAllData(stringHashMap)
                .enqueue(object : Callback<AllSearchDataSuccess>, SearchBookAdapter.mSearchClickListener {

                    override fun mSearchClick(v: View?, position: Int) {
                        Navigation.findNavController(homeFragContainer)
                            .navigate(R.id.action_homeFragment_to_webViewFragment);
                    }

                    override fun onResponse(
                        call: Call<AllSearchDataSuccess>,
                        response: Response<AllSearchDataSuccess>
                    ) {

                        if (response.isSuccessful) {

                            if (response.body()!!.code.equals("100")) {

                                val searchAllBooks = response.body()!!.data

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


