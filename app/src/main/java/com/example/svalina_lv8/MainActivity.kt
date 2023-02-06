package com.example.svalina_lv8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PersonRecyclerAdapter.ContentListener {
    private val database = Firebase.firestore
    private lateinit var recyclerAdapter: PersonRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val imgEdit = findViewById<EditText>(R.id.editTextImageUrl)
        val nameEdit = findViewById<EditText>(R.id.editTextName)
        val descEdit = findViewById<EditText>(R.id.editTextDesc)

        val recyclerView = findViewById<RecyclerView>(R.id.personList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PersonRecyclerAdapter(ArrayList(), this@MainActivity)
        }

        database.collection("persons")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<Person> = ArrayList()
                for (data in it.documents) {
                    val person = data.toObject(Person::class.java)

                    if (person != null)
                    {
                        person.id = data.id
                        list.add(person)
                    }
                }
                recyclerAdapter = PersonRecyclerAdapter(list, this@MainActivity)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.",
                    exception)
            }

        saveButton.setOnClickListener{
            var personToAdd = Person(imageUrl = imgEdit.text.toString(), name = nameEdit.text.toString(), description = descEdit.text.toString())
            database.collection("persons").add(personToAdd)
        }
    }

    override fun onItemButtonClick(index: Int, person: Person, clickType: ItemClickType) {
        if (clickType == ItemClickType.EDIT) {
            person.id?.let {
                database.collection("persons")
                    .document(it)
                    .set(person)
            }
        }
        else if (clickType == ItemClickType.REMOVE) {
            recyclerAdapter.removeItem(index)
            person.id?.let {
                database.collection("persons")
                    .document(it)
                    .delete()
            }
        }

    }
}
