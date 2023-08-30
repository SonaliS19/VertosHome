package com.example.vertoshome

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AddRoom : AppCompatActivity() {

    private lateinit var roomTypes: RecyclerView
    private lateinit var roomSeaters: RecyclerView
    private lateinit var roomTypeAdapter: RoomOptionAdapter
    private lateinit var roomSeaterAdapter: RoomOptionAdapter
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val roomTypeList = ArrayList<String>()
    private val roomSeaterList = ArrayList<String>()

    // address input box
    private lateinit var address: String

    // rent input box
    private lateinit var rent: String

    // apartment name input box
    private lateinit var apartmentName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = resources.getColor(R.color.gray, theme)
        setContentView(R.layout.activity_add_room)

        // add back button functionality
        val backButton = findViewById<ImageView>(R.id.back_button)

        backButton.setOnClickListener {
            finish()
        }

        // call function to display the room types
        displayRoomTypes()

        // call function to display the room seater types
        displayRoomSeaters()
    }

    // function to display the room types
    private fun displayRoomTypes() {
        roomTypes = findViewById(R.id.room_types)
        roomTypeList.add("AC")
        roomTypeList.add("Non-AC")

        // display the room types in the recycler view
        roomTypeAdapter = RoomOptionAdapter(roomTypeList)
        roomTypes.adapter = roomTypeAdapter

        // set the layout manager for the recycler view
        roomTypes.layoutManager = GridLayoutManager(this, 2)

        // set the fixed size for the recycler view
        roomTypes.setHasFixedSize(true)
    }

    // function to display the room seater types
    private fun displayRoomSeaters() {

        roomSeaters = findViewById(R.id.room_seater)
        roomSeaterList.add("1 seater")
        roomSeaterList.add("2 seater")
        roomSeaterList.add("3 seater")
        roomSeaterList.add("4 seater")

        // display the room types in the recycler view
        roomSeaterAdapter = RoomOptionAdapter(roomSeaterList)
        roomSeaters.adapter = roomSeaterAdapter

        // set the layout manager for the recycler view
        roomSeaters.layoutManager = GridLayoutManager(this, 2)

        // set the fixed size for the recycler view
        roomSeaters.setHasFixedSize(true)
    }

    fun onCheckBoxClick(view: View) {
        // Find the parent layout (LinearLayout) containing both the TextView and CheckBox
        val parentLayout = view.parent as LinearLayout

        // Find the CheckBox within the parent layout using its ID
        val checkBox = parentLayout.findViewById<CheckBox>(R.id.itemCheckBox)

        // Get the RecyclerView that contains the clicked CheckBox
        val recyclerView = parentLayout.parent as RecyclerView

        // Get the position of the clicked CheckBox in the RecyclerView
        val position = recyclerView.getChildAdapterPosition(parentLayout)

        // if view is textview then do toggle else do nothing
        if (view.id == R.id.itemText) {
            // Toggle the isChecked property of the CheckBox
            checkBox.isChecked = !checkBox.isChecked
        } else {
            checkBox.isChecked = checkBox.isChecked
        }

        // Update the selected status in the adapter
        (recyclerView.adapter as RoomOptionAdapter).setSelected(position)
    }

    fun handleSubmit(view: View) {
        val selectedRoomTypes = roomTypeAdapter.getSelectedItems()
        val selectedRoomSeaters = roomSeaterAdapter.getSelectedItems()

        // get the address input box
        address = findViewById<EditText>(R.id.address_input).text.toString()

        // get the rent input box
        rent = findViewById<EditText>(R.id.room_rent_input).text.toString()

        // get the apartment name input box
        apartmentName = findViewById<EditText>(R.id.apartment_name_input).text.toString()

        // if any of the room type, rent or seater is not selected then show error toast
        if (selectedRoomTypes.isEmpty() || selectedRoomSeaters.isEmpty()
            || address.isEmpty() || rent.isEmpty() || apartmentName.isEmpty()
        ) {
            Toast.makeText(this, "Please select all the options", Toast.LENGTH_SHORT).show()
        } else if (rent.toInt() < 0) {
            Toast.makeText(this, "Please enter valid rent amount", Toast.LENGTH_SHORT).show()
        } else {
            // if all the options are selected then add the room to the database
            val roomObject = Room(
                roomTypeAdapter.getSelectedItems()[0],
                rent,
                roomSeaterAdapter.getSelectedItems()[0],
                address,
                apartmentName
            )

            // add the room to the database
            db.collection("rooms")
                .add(roomObject)
                .addOnSuccessListener {

                    // clear all the input boxes and checkboxes
                    findViewById<EditText>(R.id.address_input).text.clear()
                    findViewById<EditText>(R.id.room_rent_input).text.clear()
                    findViewById<EditText>(R.id.apartment_name_input).text.clear()
                    roomTypeAdapter.clearSelected()
                    roomSeaterAdapter.clearSelected()

                    // show success toast
                    Toast.makeText(this, "Room added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error adding room", Toast.LENGTH_SHORT).show()
                }
        }
    }
    }