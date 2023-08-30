package com.example.vertoshome

import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class AvailableRooms : AppCompatActivity() {
    private lateinit var adapter: RoomDetailsAdapter
    private lateinit var recyclerView: RecyclerView
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val roomList = ArrayList<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = resources.getColor(R.color.gray, theme)
        setContentView(R.layout.activity_available_rooms)

        // add go back functionality
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.roomList)

        // Fetch data from database and listen for real-time updates
        db.collection("rooms").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle any errors that occurred during fetching the data
                return@addSnapshotListener
            }

            // Clear the existing roomList to avoid duplicates
            roomList.clear()

            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot) {
                    val room = Room(
                        document.data["roomType"].toString(),
                        document.data["roomRent"].toString(),
                        document.data["roomSeater"].toString(),
                        document.data["address"].toString(),
                        document.data["apartmentName"].toString()
                    )
                    roomList.add(room)
                }

                // Update the UI with the fetched data
                setAdapter()
            } else {
                // make the no rooms message visible
                findViewById<TextView>(R.id.noRoomMessage).visibility = TextView.VISIBLE
            }
        }
    }

    private fun setAdapter() {
        if (!this::adapter.isInitialized) {
            adapter = RoomDetailsAdapter(this,roomList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
        } else {
            adapter.notifyDataSetChanged()
        }
    }
}