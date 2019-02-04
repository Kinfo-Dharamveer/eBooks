package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.CategoryAdapter
import com.kinfoitsolutions.ebooks.ui.responsemodel.CategoryModel
import kotlinx.android.synthetic.main.fragment_category.view.*
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.widget.*
import androidx.navigation.Navigation
import com.drivingschool.android.AppConstants
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import com.kinfoitsolutions.ebooks.ui.Utils
import com.kinfoitsolutions.ebooks.ui.responsemodel.categorybooksresponse.BooksCatSuccess
import com.kinfoitsolutions.ebooks.ui.restclient.RestClient
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : BaseFragment() {

    private lateinit var viewOfLayout: View
    private lateinit var categoryAdapter: CategoryAdapter



    var country = arrayOf("India", "USA", "China", "Japan", "Other")


    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_category, parent, false)
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

                    Navigation.findNavController(categry_container).navigate(R.id.action_categoryFragment_to_allCategoryBooksFragment);

                }

                override fun onResponse(call: Call<BooksCatSuccess>, response: Response<BooksCatSuccess>) {

                    if (response.isSuccessful) {

                        if (response.body()!!.code.equals("100")) {

                            val allBooksCat = response.body()!!.categories

                            categoryAdapter = CategoryAdapter(allBooksCat,context,this)
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

                val spinner = dialogView.findViewById(R.id.cat_spinner) as Spinner
                val cross_dialog = dialogView.findViewById(R.id.cross_dialog) as ImageView

                val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, country)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                //Setting the ArrayAdapter data on the Spinner
                spinner.setAdapter(aa)

                val alertDialog = dialogBuilder.create()
                alertDialog.show()

                cross_dialog.setOnClickListener {
                    alertDialog.dismiss()
                }

                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }



}
