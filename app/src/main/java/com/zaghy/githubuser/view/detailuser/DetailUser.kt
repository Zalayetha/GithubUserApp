package com.zaghy.githubuser.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zaghy.githubuser.R
import com.zaghy.githubuser.adapter.SectionPagesAdapter
import com.zaghy.githubuser.databinding.ActivityDetailUserBinding

class DetailUser : AppCompatActivity() {
    private lateinit var binding:ActivityDetailUserBinding
    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagesAdapter = SectionPagesAdapter(this)
        val viewPager:ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPagesAdapter
        val tabs:TabLayout = binding.tabs
        TabLayoutMediator(tabs,viewPager){tabs,position->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}