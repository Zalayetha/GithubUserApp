package com.zaghy.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.databinding.CardUserGithubBinding


class RecyclerViewAdapter:ListAdapter<ItemsItem,RecyclerViewAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object{
        val DIFF_CALLBACK = object:DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
               return oldItem == newItem
            }

        }
    }


    class MyViewHolder(val binding: CardUserGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user:ItemsItem){
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
    }
}