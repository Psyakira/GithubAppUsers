package com.submission.fundamental.ui.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.fundamental.data.response.ItemsItem
import com.submission.fundamental.databinding.ItemListBinding
import com.submission.fundamental.ui.detail.DetailUserActivity


class ItemAdapter : ListAdapter<ItemsItem, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserActivity.username, user.login)
            intentDetail.putExtra(DetailUserActivity.avatarURL, user.avatarUrl)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class ItemViewHolder(private val binding:ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvName.text = user.type
            binding.tvUser.text = user.login
            Glide.with(itemView.context).load(user.avatarUrl).into(binding.imgAva)
        }
    }
}
