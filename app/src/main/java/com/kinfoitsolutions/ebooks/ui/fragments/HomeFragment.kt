package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinfoitsolutions.ebooks.ui.activities.DashboardActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.RecommandedRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import kotlinx.android.synthetic.main.fragment_home.*

import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), RecommandedRecycleAdapter.mClickListener {



    private lateinit var viewOfLayout: View

    //  recommanded data
    private lateinit var  recommandedModelClasses: ArrayList<RecommandedModelClass>
    private lateinit var bAdapter: RecommandedRecycleAdapter

    private val image = arrayOf(R.drawable.blink_imges,
        R.drawable.me_befor_you,
        R.drawable.how_to_win,
        R.drawable.me_befor_you,
        R.drawable.how_to_win)

    private val title = arrayOf(
        "Blink: The Power\n" + "of Thinking Wi…", "Me Before You",
        "How to Win \n" + "Friends and Inf…","Me Before You","And John Carter"
    )
    private val rating = arrayOf("4.5", "4.0", "3.2","1.2","3.7")
    private val author_name = arrayOf("Malcolm Gladwell", "Jojo Moyes",
        "Dale Carnegie","Malcolm Gladwell", "Jojo Moyes")


    //  top_50 data
    private lateinit var recommandedModelClasses1: ArrayList<RecommandedModelClass>
    private lateinit var bAdapter1: RecommandedRecycleAdapter

    private val image1 = arrayOf(R.drawable.cantbury, R.drawable.the_dreaming, R.drawable.the_beauty_purpose,
        R.drawable.cantbury,R.drawable.the_beauty_purpose)
    private val title1 = arrayOf(
        "The Canterbury\n" + "Tales", "The Dreaming\n" + "Reality",
        "The Beauty of\n" + "Purpose in Life…","The Beauty of","Reality"
    )
    private val rating1 = arrayOf("4.5", "4.0", "3.2","1.2","3.7")
    private val author_name1 = arrayOf("Malcolm Gladwell", "Jojo Moyes",
        "Dale Carnegie","Malcolm Gladwell", "Jojo Moyes")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_home, container, false)


        //recommanded recyclerview code is here

        val layoutManager = GridLayoutManager(context, 3)
        viewOfLayout.recommanded_recyclerview.setLayoutManager(layoutManager)
        viewOfLayout.recommanded_recyclerview.setItemAnimator(DefaultItemAnimator())

        recommandedModelClasses = ArrayList()

        for (i in 0 until image.size) {
            val beanClassForRecyclerView_contacts = RecommandedModelClass(image[i], title[i], rating[i], author_name[i])
            recommandedModelClasses.add(beanClassForRecyclerView_contacts)
        }
        bAdapter = RecommandedRecycleAdapter(context, recommandedModelClasses,this)
        viewOfLayout.recommanded_recyclerview.setAdapter(bAdapter)


        //   recommanded recyclerview code is here
        val layoutManager1 = GridLayoutManager(context, 3)
        viewOfLayout.top50_books_recyclerview.setLayoutManager(layoutManager1)
        viewOfLayout.top50_books_recyclerview.setItemAnimator(DefaultItemAnimator())

        recommandedModelClasses1 = ArrayList()

        for (i in 0 until image.size) {
            val beanClassForRecyclerView_contacts =
                RecommandedModelClass(image1[i], title1[i], rating1[i], author_name1[i])
            recommandedModelClasses1.add(beanClassForRecyclerView_contacts)
        }
        bAdapter1 = RecommandedRecycleAdapter(context, recommandedModelClasses1,this)
        viewOfLayout.top50_books_recyclerview.setAdapter(bAdapter1)

        viewOfLayout.viewAllRecommend.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllFragment);

        }

        viewOfLayout.viewAllToday.setOnClickListener {

            Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_viewAllFragment);

        }

        return viewOfLayout

    }


    override fun mClick(v: View?, position: Int) {

        Navigation.findNavController(homeFragContainer).navigate(R.id.action_homeFragment_to_bookReadingFragment);

    }

}
