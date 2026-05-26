package com.example.mall_navigation.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mall_navigation.databinding.ItemShopBinding
import com.example.mall_navigation.models.Shop

class ShopAdapter(private val onItemClick: (Shop) -> Unit) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private var items = listOf<Shop>()

    fun submitList(list: List<Shop>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemShopBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: Shop) {
            binding.shopName.text = shop.name
            binding.shopInfo.text = "Этаж ${shop.floor} | ${shop.category}"
            binding.root.setOnClickListener { onItemClick(shop) }
        }
    }
}
