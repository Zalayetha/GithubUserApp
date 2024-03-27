package com.zaghy.githubuser.ui.detailuser


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.response.UserDetailResponse
import com.zaghy.githubuser.ui.adapter.SectionPagesAdapter
import com.zaghy.githubuser.databinding.ActivityDetailUserBinding
import com.zaghy.githubuser.ui.main.MainActivity

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
        val username = intent.getStringExtra(MainActivity.USERNAME)
        val viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]
        val sectionPagesAdapter = SectionPagesAdapter(this)
        sectionPagesAdapter.username = username ?: ""
        val viewPager:ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPagesAdapter

        val tabs:TabLayout = binding.tabs
        TabLayoutMediator(tabs,viewPager){tabs,position->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()


        if(viewModel.username.value != username){
            Log.e("berubah",viewModel.username.value.toString())
            viewModel.setUsername(username ?: "")
        }

        viewModel.username.observe(this){name->
            viewModel.findDetailUserGithub(name ?: "")

        }

        viewModel.detailUser.observe(this){detailUser->
            setDetailUser(detailUser)
        }
        viewModel.isLoading.observe(this){
            showLoading(it)
        }

    }

    private fun setDetailUser(detailUser:UserDetailResponse) {
        Glide.with(this).load(detailUser.avatarUrl).into(binding.circleImageView)
        binding.name.text = detailUser.name
        binding.username.text = detailUser.login
        binding.bio.text = detailUser.bio
        binding.followers.text = "${detailUser.followers} Followers"
        binding.following.text = "${detailUser.following} Following"
    }
    private fun showLoading(isLoading:Boolean){
        Log.d("DetailAct","load")
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}