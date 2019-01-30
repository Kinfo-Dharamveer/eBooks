package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.LatestAdapter
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import kotlinx.android.synthetic.main.fragment_latest.view.*


class LatestFragment : Fragment(), RecommandedRecycleAdapter.mClickListener {


    private lateinit var viewOfLayout: View

    private lateinit var  recommandedModelClasses: ArrayList<RecommandedModelClass>
    private lateinit var bAdapter: LatestAdapter

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

    private val rating = arrayOf("4.5", "4.0", "3.2","2.5","3.6")
    private val author_name = arrayOf("Malcolm Gladwell",
        "Jojo Moyes",
        "Dale Carnegie",
        "Malcolm Gladwell",
        "Jojo Moyes")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        viewOfLayout =  inflater.inflate(R.layout.fragment_latest, container, false)

        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.latest_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.latest_recyclerview.setItemAnimator(DefaultItemAnimator())

        recommandedModelClasses = ArrayList()

        for (i in 0 until image.size) {
            val beanClassForRecyclerView_contacts = RecommandedModelClass(image[i], title[i], rating[i], author_name[i])
            recommandedModelClasses.add(beanClassForRecyclerView_contacts)
        }
        bAdapter = LatestAdapter(recommandedModelClasses,context)
        viewOfLayout.latest_recyclerview.setAdapter(bAdapter)


        return viewOfLayout

    }


    override fun mClick(v: View?, position: Int) {


    }


}
