package com.example.grocery.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.model.DummyDataItem

class CartAdapter(
    private val selectedProducts: ArrayList<DummyDataItem>,
    private val counterMap: MutableMap<Int, Int>,
    private val counterMapChangeListener: CounterMapChangeListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvProductQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val tvCount: TextView = itemView.findViewById(R.id.tvcount)
        val tvAdd: TextView = itemView.findViewById(R.id.tvAdd)
        val tvRemove: TextView = itemView.findViewById(R.id.tvRemove)
        val counterbtnslayout: LinearLayout = itemView.findViewById(R.id.layoutCounterbtns)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = selectedProducts[position]
        val count = counterMap[product.id] ?: 0

        if (count > 0) {
            // Load image using Glide (you need to add the Glide dependency)
            Glide.with(holder.itemView.context)
                .load(product.imageUrl)
                .into(holder.ivProductImage)

            holder.tvProductName.text = product.title
            holder.tvProductPrice.text = "\u20B9${product.price}"
            holder.tvProductQuantity.text = product.weight.toString()
            holder.tvCount.text = count.toString()

            holder.tvAdd.setOnClickListener {
                // Increment count
                val currentCount = counterMap.getOrDefault(product.id, 0) + 1
                counterMap[product.id] = currentCount
                holder.tvCount.text = currentCount.toString()
                counterMapChangeListener.onCounterMapChange(counterMap)
            }

            holder.tvRemove.setOnClickListener {
                // Decrement count
                val currentCount = counterMap.getOrDefault(product.id, 0)
                if (currentCount > 0) {
                    counterMap[product.id] = currentCount - 1
                    holder.tvCount.text = (currentCount - 1).toString()
                    counterMapChangeListener.onCounterMapChange(counterMap)
                    // Update visibility when count becomes 0
                    if (currentCount == 1) {
//                        holder.itemView.visibility = View.GONE
//                        selectedProducts.removeIf { it ->
//                            it.id == product.id
//                        }
//                        selectedProducts.removeAt(holder.adapterPosition)
//                        notifyDataSetChanged()
                    }
                }
            }
        } else {
//            // If count is 0, hide the entire card
//            holder.itemView.visibility = View.GONE
        }
    }




    override fun getItemCount(): Int {
        return selectedProducts.size
    }


}
