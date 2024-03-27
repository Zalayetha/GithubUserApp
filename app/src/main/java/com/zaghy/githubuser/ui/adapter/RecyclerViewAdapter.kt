package com.zaghy.githubuser.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.databinding.CardUserGithubBinding


class RecyclerViewAdapter:ListAdapter<ItemsItem,RecyclerViewAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var  onItemCallback:OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(username:String)
    }


    companion object{
        private const val TAG = "RECYCLER VIEW ADAPTER"
        val DIFF_CALLBACK = object:DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
               return oldItem == newItem
            }

        }
    }

    fun setOnItemCallback(callback:OnItemClickCallback){
        this.onItemCallback = callback
    }
    class MyViewHolder(val binding: CardUserGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.username.text = user.login
            Glide
                .with(binding.root)
                .load(user.avatarUrl)
                .into(binding.userPhoto)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.MyViewHolder {
        val binding = CardUserGithubBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            Log.d(TAG,user.login)
            onItemCallback.onItemClicked(user.login)
        }
    }
}