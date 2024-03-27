package com.zaghy.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.databinding.ActivityMainBinding
import com.zaghy.githubuser.datastore.SettingsPreferences
import com.zaghy.githubuser.datastore.dataStore
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var  recyclerView:RecyclerView
    private lateinit var viewModel:MainViewModel
    companion object {
        const val USERNAME = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        set data store preferences
        val pref = SettingsPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(this,MainViewModelFactory(pref))[MainViewModel::class.java]

//        Searchbar
        viewModel.getThemeSettings().observe(this){isDarkModeActive->
            if(isDarkModeActive) {
                binding.searchBar.inflateMenu(R.menu.menu_main_dark)
            }else{
                binding.searchBar.inflateMenu(R.menu.menu_main_light)
            }
        }

        binding.searchBar.setOnMenuItemClickListener {item->
            when(item.itemId){
                R.id.switchmode->{
                    toggleSwitchMode(viewModel.isCheckedMode.value ?: false)
                    if(viewModel.isCheckedMode.value == true){
                        item.icon = AppCompatResources.getDrawable(this,R.drawable.lightmode24)
                    }else{
                        item.icon = AppCompatResources.getDrawable(this,R.drawable.darkmode24)
                    }

                }
                R.id.favoriteUser->{
                    toggleSwitchFavorite(viewModel.isCheckedFavorite.value ?: false)
                    if(viewModel.isCheckedFavorite.value == true){
                        item.icon = AppCompatResources.getDrawable(this,R.drawable.favorite24)
                    }else{
                        item.icon = AppCompatResources.getDrawable(this,R.drawable.favoritefill24)
                    }
                }
            }
            true
        }
//        Searchview
        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.findUserGithub(searchView.text.toString().trim())
                false
            }
        }


//        recyclerView
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
        viewModel.getThemeSettings().observe(this){isDarkModeActive->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


    }

    private fun toggleSwitchMode(status:Boolean){
        viewModel.toggleSwitchMode(status)
    }

    private fun toggleSwitchFavorite(status:Boolean){
        viewModel.toggleSwitchFavorite(status)
    }


    private fun setUserList(user: List<ItemsItem>?){
        if((user?.size ?: 0) > 0){
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