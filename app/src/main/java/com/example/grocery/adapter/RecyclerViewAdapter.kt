package com.example.grocery.adapter

// RecyclerViewAdapter.kt
import android.content.Context
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

class RecyclerViewAdapter(
    private val context: Context,
    private val data: ArrayList<DummyDataItem>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val counterMap = mutableMapOf<Int, Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardId = data[position].id
        holder.tvCount.text = counterMap.getOrDefault(cardId, 0).toString()

        holder.btInitialAdd.setOnClickListener {
            // Increment count and update visibility
            val currentCount = counterMap.getOrDefault(cardId, 0) + 1
            counterMap[cardId] = currentCount
            updateVisibility(holder)
//            notifyItemChanged(position)
        }
        holder.tvAdd.setOnClickListener {
            // Increment count
            val currentCount = counterMap.getOrDefault(cardId, 0) + 1
            counterMap[cardId] = currentCount
            holder.tvCount.text = currentCount.toString()
            updateVisibility(holder)
        }

        holder.tvRemove.setOnClickListener {
            // Decrement count
            val currentCount = counterMap.getOrDefault(cardId, 0)
            if (currentCount > 0) {
                counterMap[cardId] = currentCount - 1
                holder.tvCount.text = (currentCount - 1).toString()
                updateVisibility(holder)
            }
        }
        val item = data[position]

        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.priceTextView.text = "\u20B9" + item.price.toString()
        holder.quantityTextView.text = item.weight.toString()


        // Use Glide to load the image from the URL
        Glide.with(context)
            .load(item.imageUrl)
            .into(holder.imageView)
    }


    private fun updateVisibility(holder: ViewHolder) {
        val cardId = data[holder.adapterPosition].id
        val currentCount = counterMap.getOrDefault(cardId, 0)

        if (currentCount == 0) {
            holder.btInitialAdd.visibility = View.VISIBLE
            holder.counterbtnslayout.visibility = View.GONE
        } else {
            holder.btInitialAdd.visibility = View.GONE
            holder.counterbtnslayout.visibility = View.VISIBLE
            holder.tvCount.text = currentCount.toString()
        }
    }

    fun getCounterMap(): Map<Int, Int> {
        return counterMap
    }
    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(products: List<DummyDataItem>) {
        data.clear()
        data.addAll(products)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val tvCount: TextView = itemView.findViewById(R.id.tvcount)
        val tvAdd: TextView = itemView.findViewById(R.id.tvAdd)
        val tvRemove: TextView = itemView.findViewById(R.id.tvRemove)
        val btInitialAdd: Button = itemView.findViewById(R.id.btInitialAdd);
        val counterbtnslayout: LinearLayout = itemView.findViewById(R.id.layoutCounterbtns)
    }
}
