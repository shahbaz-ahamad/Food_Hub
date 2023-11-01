package com.example.foodhub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodhub.databinding.CategoryProductBinding
import com.example.foodhub.datamodel.retrofitdatamodel.EachCategoryItem

class CategoryItemAdapter : RecyclerView.Adapter<CategoryItemAdapter.viewholder>() {
    class viewholder(private val binding: CategoryProductBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryItem: EachCategoryItem) {
            binding.apply {
                Glide.
                with(itemView)
                    .load(categoryItem.strMealThumb)
                    .into(imgMeal)

                tvMealName.text=categoryItem.strMeal
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<EachCategoryItem>(){
        override fun areItemsTheSame(oldItem: EachCategoryItem, newItem: EachCategoryItem): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: EachCategoryItem, newItem: EachCategoryItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(CategoryProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val categoryItem = differ.currentList[position]
        holder.bind(categoryItem)

        holder.itemView.setOnClickListener {
            onClick?.invoke(categoryItem)
        }

    }


    var onClick: ((EachCategoryItem) -> Unit)?=null
}