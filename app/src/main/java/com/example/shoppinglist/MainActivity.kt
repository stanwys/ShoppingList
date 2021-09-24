package com.example.shoppinglist

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.ui.main.SectionsPagerAdapter
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.model.ShoppingListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var viewModel: ShoppingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java!!)
        viewModel.initializeDBHandler(this)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = binding.container
        viewPager.adapter = mSectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeData()
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeDatabase()
    }
}