package com.zaghy.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.githubuser.databinding.CardUserGithubBinding


class RecyclerViewAdapter<T>(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val bindView: (T, CardUserGithubBinding) -> Unit,
    private val itemClick: (T) -> Unit
) : ListAdapter<T, RecyclerViewAdapter<T>.MyViewHolder>(diffCallback) {

    companion object {
        private const val TAG = "RECYCLER VIEW ADAPTER"
    }

    inner class MyViewHolder(private val binding: CardUserGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            bindView(item, binding)
            itemView.setOnClickListener {
                itemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}