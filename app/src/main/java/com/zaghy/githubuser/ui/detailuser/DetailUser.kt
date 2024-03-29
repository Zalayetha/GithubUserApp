package com.zaghy.githubuser.ui.detailuser


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.data.remote.response.UserDetailResponse
import com.zaghy.githubuser.databinding.ActivityDetailUserBinding
import com.zaghy.githubuser.ui.adapter.SectionPagesAdapter
import com.zaghy.githubuser.ui.main.MainActivity

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var dataUser: FavoriteUser
    private lateinit var detailUser: UserDetailResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra(MainActivity.USERNAME)
        val viewModel: DetailUserViewModel by viewModels<DetailUserViewModel> {
            DetailUserViewModelFactory.getInstance(application)
        }
        val sectionPagesAdapter = SectionPagesAdapter(this)
        sectionPagesAdapter.username = username ?: ""
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPagesAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getDetailUser(username ?: "").observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.fabFavBtn.isEnabled = false
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        detailUser = result.data
                        viewModel.setDetailUser(detailUser)
                        binding.fabFavBtn.isEnabled = true
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.fabFavBtn.isEnabled = false
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

        viewModel.detailUser.observe(this) { data ->
            setDetailUser(data)
        }

        //        check if this user is favorite
        viewModel.checkFavoriteUser(username ?: "").observe(this) { result ->
            Log.e("masuk", "masuk")
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        dataUser = result.data
                        binding.fabFavBtn.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.favoritefill24
                            )
                        )
                        viewModel.setActivated(true)
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
            } else {
                binding.fabFavBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.favorite24
                    )
                )
                viewModel.setActivated(false)
            }
        }
//        then change the fab icon
        viewModel.isFavorite.observe(this) { isFavorite ->
            Log.d("isFavorite", isFavorite.toString())
            if (isFavorite) {
                try {
                    val data =
                        FavoriteUser(username = detailUser.login, avatarUrl = detailUser.avatarUrl)
                    viewModel.addFavoriteUser(data)
                    viewModel.setActivated(true)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                try {
                    val data =
                        FavoriteUser(username = detailUser.login, avatarUrl = detailUser.avatarUrl)
                    viewModel.deleteFavoriteUser(data)
                    viewModel.setActivated(false)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

//        listener FAB
        binding.fabFavBtn.setOnClickListener {
            viewModel.isActivated.observe(this) { isActivate ->
                it.isActivated = !isActivate
            }
            Log.d("FAB", it.isActivated.toString())
            viewModel.setFavorite(it.isActivated)

        }

    }


    private fun setDetailUser(detailUser: UserDetailResponse) {
        Glide.with(this).load(detailUser.avatarUrl).into(binding.circleImageView)
        binding.name.text = detailUser.name
        binding.username.text = detailUser.login
        binding.bio.text = detailUser.bio
        binding.followers.text = buildString {
            append(detailUser.followers)
            append(" Followers")
        }
        binding.following.text = buildString {
            append(detailUser.following)
            append(" Following")
        }
    }
}