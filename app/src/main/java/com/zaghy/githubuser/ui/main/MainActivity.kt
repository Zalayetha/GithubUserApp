package com.zaghy.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.databinding.ActivityMainBinding
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUser
import com.zaghy.githubuser.ui.favorite.FavoriteActivity
import com.zaghy.githubuser.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application)
    }

    companion object {
        const val USERNAME = "username"
        const val TAG = "mainAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        Searchbar
        binding.searchBar.inflateMenu(R.menu.menu_main)

        binding.searchBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    val intentSettings = Intent(this, SettingsActivity::class.java)
                    startActivity(intentSettings)
                }

                R.id.favoriteUser -> {
                    val intentFavorite = Intent(this,FavoriteActivity::class.java)
                    startActivity(intentFavorite)
                }
            }
            true
        }
//        Searchview
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            viewModel.findUserGithub(binding.searchView.text.toString().trim()).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val userList = result.data
                            viewModel.setUserListItems(userList)

                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            false
        }

        viewModel.userListItems.observe(this){userList->
            setUserList(userList)
        }

        viewModel.getThemeSettings().observe(this){isDarkActivated->
            if (isDarkActivated) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

//        recyclerView
        binding.recyclerViewuser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.userListItems.observe(this) { user ->
            setUserList(user)
        }


    }

    private fun setUserList(user: List<ItemsItem>?) {
        if ((user?.size ?: 0) > 0) {
            binding.recyclerViewuser.visibility = View.VISIBLE
            binding.errormsg.visibility = View.GONE
            val adapter = RecyclerViewAdapter<ItemsItem>(
                diffCallback = object : DiffUtil.ItemCallback<ItemsItem>() {
                    override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                        return oldItem == newItem // Assuming ItemsItem has an id
                    }

                    override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                        return oldItem == newItem
                    }
                },
                bindView = { item, binding ->
                    binding.username.text = item.login
                    Glide.with(binding.root).load(item.avatarUrl).into(binding.userPhoto)
                },
                itemClick = { item ->
                    val intentDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                    intentDetailUser.putExtra(USERNAME, item.login)
                    startActivity(intentDetailUser)
                }
            )
            adapter.submitList(user)
            binding.recyclerViewuser.adapter = adapter
        } else {
            binding.recyclerViewuser.visibility = View.GONE
            binding.errormsg.text = "Data Tidak Ditemukan"
            binding.errormsg.visibility = View.VISIBLE

        }

    }
}