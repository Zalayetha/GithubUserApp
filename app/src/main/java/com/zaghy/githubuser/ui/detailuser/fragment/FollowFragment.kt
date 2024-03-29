package com.zaghy.githubuser.ui.detailuser.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.databinding.FragmentFollowBinding
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUserViewModel
import com.zaghy.githubuser.ui.detailuser.DetailUserViewModelFactory


class FollowFragment : Fragment() {
    private var position: Int? = null
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding
    private lateinit var  viewModel:DetailUserViewModel
    private lateinit var  recyclerView: RecyclerView
    private lateinit var  dataUser : FavoriteUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(layoutInflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel : DetailUserViewModel by viewModels<DetailUserViewModel> {
            DetailUserViewModelFactory.getInstance(requireActivity())
        }
        arguments?.let {
            position = it.getInt(ARG_POSITION,0)
            username = it.getString(ARG_USERNAME)
        }
        Log.d(ARG_POSITION,position.toString())
        Log.d(ARG_USERNAME,username.toString())
        if(position == 1){
            viewModel.getFollowers(username ?: "").observe(viewLifecycleOwner){result->
                if(result!= null){
                    when(result){
                        is Result.Loading->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success->{
                            binding.progressBar.visibility = View.GONE
                            val followerResult = result.data
                            viewModel.setFollowers(followerResult)
                        }
                        is Result.Error->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }else{
            viewModel.getFollowing(username ?: "").observe(viewLifecycleOwner){result->
                if(result!= null){
                    when(result){
                        is Result.Loading->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success->{
                            binding.progressBar.visibility = View.GONE
                            val followingResult = result.data
                            viewModel.setFollowing(followingResult)
                        }
                        is Result.Error->{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }

        viewModel.followers.observe(requireActivity()){data->
            setFollowerOrFollowingData(data)
        }
        viewModel.following.observe(requireActivity()){data->
            setFollowerOrFollowingData(data)
        }
//        recyclerview
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = binding.followRecycle
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
    }

    private fun setFollowerOrFollowingData(data:List<ItemsItem>){
        if (data.isNotEmpty()){
            binding.followRecycle.visibility = View.VISIBLE
            binding.errormsg.visibility = View.GONE
            val adapter =  RecyclerViewAdapter<ItemsItem>(
                diffCallback = object : DiffUtil.ItemCallback<ItemsItem>() {
                    override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                        return oldItem == newItem
                    }
                },
                bindView = { item, binding ->
                    binding.username.text = item.login
                    Glide.with(binding.root).load(item.avatarUrl).into(binding.userPhoto)
                },
                itemClick = { _ ->
                }
            )
            adapter.submitList(data)
            recyclerView.adapter = adapter
        }else{
            binding.followRecycle.visibility = View.GONE
            binding.errormsg.text = "Data Tidak Ditemukan"
            binding.errormsg.visibility = View.VISIBLE
        }



    }

    companion object{
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}