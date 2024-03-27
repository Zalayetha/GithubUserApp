package com.zaghy.githubuser.ui.detailuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.databinding.FragmentFollowBinding
import com.zaghy.githubuser.ui.adapter.RecyclerViewAdapter
import com.zaghy.githubuser.ui.detailuser.DetailUserViewModel


class FollowFragment : Fragment() {
    private var position: Int? = null
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding
    private lateinit var  viewModel:DetailUserViewModel
    private lateinit var  recyclerView: RecyclerView
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
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POSITION,0)
            username = it.getString(ARG_USERNAME)
        }
        Log.d(ARG_POSITION,position.toString())
        if(position == 1){
            viewModel.getFollowers(username ?: "")
        }else{
            viewModel.getFollowing(username ?: "")
        }

        viewModel.followersData.observe(viewLifecycleOwner){
            Log.e("masuk","masuk")
            setFollowerOrFollowingData(it)
        }
        viewModel.followingData.observe(viewLifecycleOwner){
            setFollowerOrFollowingData(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = binding.followRecycle
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
    }
    fun setFollowerOrFollowingData(data:List<ItemsItem>){
        if (data.isNotEmpty()){
            binding.followRecycle.visibility = View.VISIBLE
            binding.errormsg.visibility = View.GONE
            val adapter = RecyclerViewAdapter()
            adapter.submitList(data)
            adapter.setOnItemCallback(object:RecyclerViewAdapter.OnItemClickCallback{
                override fun onItemClicked(username: String) {
                }

            })
            recyclerView.adapter = adapter
        }else{
            binding.followRecycle.visibility = View.GONE
            binding.errormsg.text = "Data Tidak Ditemukan"
            binding.errormsg.visibility = View.VISIBLE
        }



    }
    fun showLoading(isLoading:Boolean){
        Log.d("MainAct","load")
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}