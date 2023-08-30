package com.example.vertoshome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomOptionAdapter(private val items: List<String>) :
    RecyclerView.Adapter<RoomOptionAdapter.ViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemText)
        val checkBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_room_option, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
        holder.checkBox.isChecked = position == selectedItemPosition

        holder.itemView.setOnClickListener {
            setSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setSelected(position: Int) {
        if (selectedItemPosition != position) {
            val previousSelected = selectedItemPosition
            selectedItemPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedItemPosition)
        }
    }

    fun getSelectedItems(): List<String> {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            listOf(items[selectedItemPosition])
        } else {
            emptyList()
        }
    }

    fun clearSelected() {
        setSelected(RecyclerView.NO_POSITION)
    }
}
