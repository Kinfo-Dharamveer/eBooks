package com.kinfoitsolutions.ebooks.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.drivingschool.android.AppConstants
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.toolbar_main.*
import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseActivity
import com.kinfoitsolutions.ebooks.ui.customviews.CustomTypefaceSpan
import com.orhanobut.hawk.Hawk
import android.widget.ArrayAdapter




class DashboardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var exit = 0
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(toolbar)

        val m = navigationView!!.getMenu()
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)

            //for aapplying a font to subMenu ...
            val subMenu = mi.subMenu
            if (subMenu != null && subMenu.size() > 0) {
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)
                    applyFontToMenuItem(subMenuItem)
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val host = supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment? ?: return
        navController = host.navController

        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(this)


    }

    private fun applyFontToMenuItem(subMenuItem: MenuItem?) {
        val font = Typeface.createFromAsset(assets, "fonts/Montserrat-Regular.ttf")
        val mNewTitle = SpannableString(subMenuItem!!.getTitle())
        mNewTitle.setSpan(CustomTypefaceSpan("", font), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        subMenuItem.setTitle(mNewTitle)

    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {


        menuItem.setChecked(true)

        drawer_layout.closeDrawers()

        val id = menuItem.getItemId()

        when (id) {


            R.id.nav_home -> navController.navigate(R.id.homeFragment)

            R.id.nav_latest -> navController.navigate(R.id.latestFragment)

            R.id.nav_category -> navController.navigate(R.id.categoryFragment)

            R.id.nav_author -> navController.navigate(R.id.authorFragment)

            R.id.nav_download -> navController.navigate(R.id.downloadFragment)

           // R.id.nav_favorite -> navController.navigate(R.id.favoriteFragment)

            R.id.nav_settings -> navController.navigate(R.id.settingsFragment)

            R.id.nav_profile -> navController.navigate(R.id.profileFragment)

            R.id.nav_logout -> {

                createalert()

            }

        }

        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            exit()
        }
    }

    private fun createalert() {
        val builder = AlertDialog.Builder(this@DashboardActivity)
        builder.setMessage("Are you sure want to logout?")

        builder.setPositiveButton("YES") { dialog, which ->
            Hawk.delete(AppConstants.TOKEN)
            val i = Intent(applicationContext,LoginActivity::class.java)
            i.putExtra(AppConstants.OPEN_LOGIN_FRAG,1)
            startActivity(i)
            finish()
        }


        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }


        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.my_fragment),drawer_layout)

    }

    private fun exit() {
        exit++
        if (exit === 2) {
            finish()
        } else {
            Toast.makeText(applicationContext, "Press again to exit", Toast.LENGTH_SHORT).show()
        }
        Handler().postDelayed({ exit = 0 }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)

        val mSearch = menu.findItem(R.id.action_search)

        val mEdit = menu.findItem(R.id.action_edit)
        val mFilter = menu.findItem(R.id.action_filter)
        val mCatSearch = menu.findItem(R.id.action_category)
        mEdit.setVisible(false)
        mFilter.setVisible(false)
        mCatSearch.setVisible(false)


        val mSearchView = mSearch.actionView as SearchView
        mSearchView.setQueryHint("Search")

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
               // mAdapter.getFilter().filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }



}