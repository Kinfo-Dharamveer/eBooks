package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.DownloadAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.LatestAdapter
import com.kinfoitsolutions.ebooks.ui.model.DownloadModelClass
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import kotlinx.android.synthetic.main.fragment_download.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_latest.view.*


class DownloadFragment : Fragment(), DownloadAdapter.mDownloadListener {


    private lateinit var viewOfLayout: View

    private lateinit var  downloadModelClass: ArrayList<DownloadModelClass>
    private lateinit var bAdapter: DownloadAdapter


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

    private val author_name = arrayOf("Malcolm Gladwell",
        "Jojo Moyes",
        "Dale Carnegie",
        "Malcolm Gladwell",
        "Jojo Moyes")




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout =  inflater.inflate(R.layout.fragment_download, container, false)

        val layoutManager = GridLayoutManager(context,3)
        viewOfLayout.downloadRecyclerView.setLayoutManager(layoutManager)
        viewOfLayout.downloadRecyclerView.setItemAnimator(DefaultItemAnimator())

        downloadModelClass = ArrayList()

        for (i in 0 until image.size) {
            val beanClassForRecyclerView_contacts = DownloadModelClass(image[i], title[i], author_name[i])
            downloadModelClass.add(beanClassForRecyclerView_contacts)
        }
        bAdapter = DownloadAdapter(downloadModelClass,context,this)
        viewOfLayout.downloadRecyclerView.setAdapter(bAdapter)


        return viewOfLayout


    }

    override fun mClick(v: View?, position: Int) {

        Toast.makeText(context,"Downloaded"+position,Toast.LENGTH_SHORT).show()

    }



}
