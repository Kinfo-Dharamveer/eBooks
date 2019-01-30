package com.kinfoitsolutions.ebooks.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.adapters.BookListRecycleAdapter
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass
import kotlinx.android.synthetic.main.fragment_view_all.view.*
import java.util.ArrayList


class ViewAllFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var recommandedModelClasses: ArrayList<RecommandedModelClass>
    private var bAdapter: BookListRecycleAdapter? = null

    private val image = arrayOf(
        R.drawable.blink_imges,
        R.drawable.me_befor_you,
        R.drawable.how_to_win,
        R.drawable.blink_imges,
        R.drawable.me_befor_you,
        R.drawable.how_to_win
    )
    private val title = arrayOf(
        "Blink: The Power of Thinking Without\n" + "Thinking",
        "Me Before You",
        "How to Win Friends and Influence\n" + "People",
        "Blink: The Power of Thinking Without\n" + "Thinking",
        "Me Before You",
        "How to Win Friends and Influence\n" + "People"
    )
    private val rating = arrayOf("4.5", "4.0", "3.2", "4.5", "4.0", "3.2")
    private val author_name =
        arrayOf("Malcolm Gladwell", "Jojo Moyes", "Dale Carnegie", "Malcolm Gladwell", "Jojo Moyes", "Dale Carnegie")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout  = inflater.inflate(R.layout.fragment_view_all, container, false)
        setHasOptionsMenu(true)


        val layoutManager = LinearLayoutManager(context)
        viewOfLayout.recyclerView.setLayoutManager(layoutManager)
        viewOfLayout.recyclerView.setItemAnimator(DefaultItemAnimator())

        recommandedModelClasses = ArrayList()

        for (i in image.indices) {
            val beanClassForRecyclerView_contacts = RecommandedModelClass(image[i], title[i], rating[i], author_name[i])
            recommandedModelClasses.add(beanClassForRecyclerView_contacts)
        }
        bAdapter = BookListRecycleAdapter(context, recommandedModelClasses)
        viewOfLayout.recyclerView.setAdapter(bAdapter)



        return viewOfLayout
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
