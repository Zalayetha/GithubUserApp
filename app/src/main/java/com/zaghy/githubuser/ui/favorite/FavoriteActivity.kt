package com.zaghy.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.databinding.ActivityFavoriteBinding
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUser
import com.zaghy.githubuser.ui.main.MainActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var  binding:ActivityFavoriteBinding
    companion object{
        const val TAG = "FavoriteActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        viewmodel
        val viewModel:FavoriteViewModel by viewModels<FavoriteViewModel> {
            FavoriteViewModelFactory.getInstance(application)
        }
        viewModel.getAllFavoriteUser().observe(this){result->
            if(result != null){
                when(result){
                    is Result.Loading->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val userList = result.data
                        Log.d(TAG,result.data.toString())
                        setUserList(userList)
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
//        recyclerView
        binding.recv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

    }
    private fun setUserList(user: List<FavoriteUser>?) {
        if ((user?.size ?: 0) > 0) {
            binding.recv.visibility = View.VISIBLE
            binding.errormsg.visibility = View.GONE
            val adapter =  RecyclerViewAdapter<FavoriteUser>(
                diffCallback = object : DiffUtil.ItemCallback<FavoriteUser>() {
                    override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                        return oldItem == newItem // Assuming ItemsItem has an id
                    }

                    override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                        return oldItem == newItem
                    }
                },
                bindView = { item, binding ->
                    binding.username.text = item.username
                    Glide.with(binding.root).load(item.avatarUrl).into(binding.userPhoto)
                },
                itemClick = { item ->
                    val intentDetailUser = Intent(this@FavoriteActivity, DetailUser::class.java)
                    intentDetailUser.putExtra(MainActivity.USERNAME, item.username)
                    startActivity(intentDetailUser)
                }
            )
            adapter.submitList(user)
            binding.recv.adapter = adapter
        } else {
            binding.recv.visibility = View.GONE
            binding.errormsg.text = getString(R.string.data_tidak_ditemukan)
            binding.errormsg.visibility = View.VISIBLE

        }

    }
}