package com.zaghy.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.databinding.ActivityMainBinding
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var  recyclerView:RecyclerView

    companion object {
        const val USERNAME = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textview, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.findUserGithub(searchView.text.toString().trim())
                false
            }
        }

        recyclerView = binding.recyclerViewuser
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        viewModel.userListItems.observe(this){user->
            setUserList(user)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun setUserList(user: List<ItemsItem>?){
        if(user?.size ?: 0 > 0){
            binding.recyclerViewuser.visibility = View.VISIBLE
            binding.errormsg.visibility = View.GONE
            val adapter = RecyclerViewAdapter()
            adapter.submitList(user)
            adapter.setOnItemCallback(object:RecyclerViewAdapter.OnItemClickCallback{
                override fun onItemClicked(username: String) {
                    val intentDetailUser = Intent(this@MainActivity,DetailUser::class.java)
                    intentDetailUser.putExtra(USERNAME,username)
                    startActivity(intentDetailUser)
                }

            })
            binding.recyclerViewuser.adapter = adapter
        }else{
            binding.recyclerViewuser.visibility = View.GONE
            binding.errormsg.text = "Data Tidak Ditemukan"
            binding.errormsg.visibility = View.VISIBLE

        }

    }
    fun showLoading(isLoading:Boolean){
        Log.d("MainAct","load")
        Log.d("MainAct",isLoading.toString())
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}