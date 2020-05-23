package com.internshala.foodfam.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.internshala.foodfam.R
import com.internshala.foodfam.database.RestaurantDatabase
import com.internshala.foodfam.database.RestaurantEntity
import com.internshala.foodfam.fragment.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreference:SharedPreferences
    lateinit var drawerLayout: DrawerLayout
    lateinit var  coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.toolbar)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        drawerLayout=findViewById(R.id.drawerLayout)
        navigationView=findViewById(R.id.navigationView)

        defaultHome()

        setUpActionBar()
        val actionBarToggle=ActionBarDrawerToggle(this,
            drawerLayout,
            R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        navigationView.setNavigationItemSelectedListener{
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){
                R.id.Home->{
                    navigationView.setCheckedItem(R.id.Home)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,HomeFragment())
                        .commit()
                    drawerLayout.closeDrawers()

                }
                R.id.MyProfile->{
                    navigationView.setCheckedItem(R.id.MyProfile)
                    supportActionBar?.title="My Profile"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,ProfileFragment()
                    ).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.favourites->{
                    navigationView.setCheckedItem(R.id.favourites)
                    supportActionBar?.title="Favourite Restaurants"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,FavouritesFragment()
                    ).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.OrderHistory->{
                    navigationView.setCheckedItem(R.id.OrderHistory)
                    supportActionBar?.title="Order History"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,HistoryFragment()
                    ).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.Faq->{
                    navigationView.setCheckedItem(R.id.Faq)
                    supportActionBar?.title="FAQs"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,FaqFragment()
                    ).commit()
                    drawerLayout.closeDrawers()

                }
                R.id.Logout->{
                    sharedPreference.edit().putBoolean("isLoggedin",false).apply()
                    val intent= Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpActionBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
        navigationView.setCheckedItem(R.id.Home)
    }

    fun defaultHome(){
        val homeFragment=HomeFragment()
        val manager=supportFragmentManager.beginTransaction()
        manager.replace(
            R.id.frame,homeFragment)
        manager.commit()
        navigationView.setCheckedItem(R.id.Home)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is HomeFragment->defaultHome()
            else->super.onBackPressed()
        }

    }


}
