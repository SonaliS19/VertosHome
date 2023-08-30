package com.example.vertoshome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale


class RoomDetailsAdapter(private val context: Context, private val roomList: ArrayList<Room>) :
    RecyclerView.Adapter<RoomDetailsAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_room,
            parent,
            false
        )
        return RoomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentRoom = roomList[position]

        holder.apartmentName.text = capitalize(currentRoom.apartmentName)
        holder.address.text = capitalize(currentRoom.address)
        holder.price.text = formatRent(currentRoom.roomRent)
        holder.itemView.setOnClickListener {
            showRoomDetailsModal(currentRoom)
        }
    }

    private fun showRoomDetailsModal(room: Room) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Room Details")
        builder.setMessage(
            buildMessage(
                room.roomType,
                room.roomRent,
                room.roomSeater,
                room.address,
                room.apartmentName
            )
        )

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun buildMessage(
        roomType: String,
        roomRent: String,
        roomSeater: String,
        address: String,
        apartmentName: String
    ): CharSequence? {
        var roomDetails = StringBuilder()
        roomDetails.append("Room Type: ")
        roomDetails.append(roomType)
        roomDetails.append("\n")

        roomDetails.append("Rent: ")
        roomDetails.append(formatRent(roomRent))
        roomDetails.append("\n")

        roomDetails.append("Seater: ")
        roomDetails.append(roomSeater)
        roomDetails.append("\n")

        roomDetails.append("Address: ")
        roomDetails.append(capitalize(address))
        roomDetails.append("\n")

        roomDetails.append("Apartment: ")
        roomDetails.append(capitalize(apartmentName))
        roomDetails.append("\n")

        return roomDetails


    }


    private fun capitalize(address: String): CharSequence? {
        // capitalize first letter of address
        return address.substring(0, 1).uppercase(Locale.ROOT) + address.substring(1)
    }

    private fun formatRent(roomRent: String): CharSequence? {
        // return amount in indian rupees format prefixed with ₹ without-decimal
        val indianRupeeFormat = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        indianRupeeFormat.maximumFractionDigits = 0
        return indianRupeeFormat.format(roomRent.toInt())
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val apartmentName: TextView = itemView.findViewById(R.id.apartmentName)
        val address: TextView = itemView.findViewById(R.id.address)
        val price: TextView = itemView.findViewById(R.id.price)
    }
}
