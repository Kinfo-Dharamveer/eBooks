package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.kinfoitsolutions.ebooks.ui.activities.DashboardActivity

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.CategoryAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.LatestAdapter
import com.kinfoitsolutions.ebooks.ui.model.CategoryModel
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.fragment_latest.view.*
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.R.attr.country
import android.widget.*


class CategoryFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var  categoryModel: ArrayList<CategoryModel>
    private lateinit var categoryAdapter: CategoryAdapter


    private val image = arrayOf(R.drawable.blink_imges,
        R.drawable.me_befor_you,
        R.drawable.how_to_win,
        R.drawable.blink_imges,
        R.drawable.how_to_win)

    private val title = arrayOf("Blink: The Power",
        "of Thinking Wi…",
        "Me Before You",
        "How to Win " ,
        "Me Before You")

    private val number = arrayOf("3",
        "2",
        "5",
        "12",
        "5")

    var country = arrayOf("India", "USA", "China", "Japan", "Other")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewOfLayout =  inflater.inflate(R.layout.fragment_category, container, false)

        setHasOptionsMenu(true)


        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.category_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.category_recyclerview.setItemAnimator(DefaultItemAnimator())

        categoryModel = ArrayList()

        for (i in 0 until image.size) {
            val categorybeanClass = CategoryModel(image[i], title[i], number[i])
            categoryModel.add(categorybeanClass)
        }
        categoryAdapter = CategoryAdapter(categoryModel,context)
        viewOfLayout.category_recyclerview.setAdapter(categoryAdapter)


        return viewOfLayout
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
