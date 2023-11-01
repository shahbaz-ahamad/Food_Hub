package com.example.foodhub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodhub.databinding.CategoryItemBinding
import com.example.foodhub.databinding.PopularItemBinding
import com.example.foodhub.datamodel.retrofitdatamodel.Category

class CategoryAdapter :RecyclerView.Adapter<CategoryAdapter.viewholder>() {


    class viewholder(private val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryItem: Category) {
            binding.apply {
                Glide.
                    with(itemView)
                    .load(categoryItem.strCategoryThumb)
                    .into(categoryImage)

                tvTitle.text=categoryItem.strCategory
            }
        }

    }

   private val diffUtil = object : DiffUtil.ItemCallback<Category>(){
       override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
           return oldItem.idCategory == newItem.idCategory
       }

       override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
           return oldItem == newItem
       }
   }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val categoryItem = differ.currentList[position]
        holder.bind(categoryItem)
    }
}