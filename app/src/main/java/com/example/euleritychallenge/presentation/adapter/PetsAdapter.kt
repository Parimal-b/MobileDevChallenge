package com.example.euleritychallenge.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.databinding.PetsListItemBinding

class PetsAdapter(
    private val onItemClickListener: (APIResponseItem) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetsViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<APIResponseItem>() {
        override fun areItemsTheSame(oldItem: APIResponseItem, newItem: APIResponseItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: APIResponseItem, newItem: APIResponseItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder {
        val binding = PetsListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PetsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    inner class PetsViewHolder(
        val binding: PetsListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pets: APIResponseItem) {
            binding.titleTextView.text = pets.title
            binding.descriptionTextView.text = pets.description
            binding.createdTextView.text = pets.created


            Glide.with(binding.imageView.context).load(pets.url)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClickListener(pets)
            }

        }
    }
}