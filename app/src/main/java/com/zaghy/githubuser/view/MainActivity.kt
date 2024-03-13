package com.zaghy.githubuser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.databinding.ActivityMainBinding
import com.zaghy.githubuser.view.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var  recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textview, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()
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
        val adapter = RecyclerViewAdapter()
        adapter.submitList(user)
        binding.recyclerViewuser.adapter = adapter
    }
    fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}