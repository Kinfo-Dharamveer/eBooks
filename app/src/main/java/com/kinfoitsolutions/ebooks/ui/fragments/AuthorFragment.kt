package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.AuthorsAdapter
import com.kinfoitsolutions.ebooks.ui.model.AuthorsModel
import kotlinx.android.synthetic.main.fragment_author.view.*

class AuthorFragment : Fragment() {


    private lateinit var viewOfLayout: View
    private lateinit var authorsAdapter: AuthorsAdapter
    private lateinit var authorsModel: ArrayList<AuthorsModel>


    private val authorImages = arrayOf(R.drawable.cantbury,R.drawable.how_to_win,R.drawable.blink_imges,R.drawable.blink_imges)
    private val authorName = arrayOf("Dany Soh","Carton Hime","Larry John","Dude Jakson")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_author, container, false)

        val gridLayouManager = GridLayoutManager(context,3)

        viewOfLayout.author_recyclerview.layoutManager = gridLayouManager
        viewOfLayout.author_recyclerview.itemAnimator = DefaultItemAnimator()

        authorsModel = ArrayList()

        for (i in 0 until authorImages.size){
            val authorsbeanModel = AuthorsModel(authorImages[i],authorName[i])
            authorsModel.add(authorsbeanModel)
        }

        authorsAdapter = AuthorsAdapter(authorsModel,context)
        viewOfLayout.author_recyclerview.adapter = authorsAdapter

        return viewOfLayout
    }


}
