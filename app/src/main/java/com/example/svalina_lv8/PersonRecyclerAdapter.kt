package com.example.svalina_lv8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ItemClickType{
    EDIT,
    REMOVE
}

class PersonRecyclerAdapter(val items: ArrayList<Person>, val listener: ContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PersonViewHolder -> {
                holder.bind(position, items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    class PersonViewHolder(val view: View): RecyclerView.ViewHolder(view){
        private val imageView = view.findViewById<ImageView>(R.id.personImage)
        private val name = view.findViewById<EditText>(R.id.personName)
        private val description = view.findViewById<EditText>(R.id.personDescription)
        private val editBtn = view.findViewById<ImageButton>(R.id.editButton)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)

        fun bind(index: Int, person: Person, listener: ContentListener){
            Glide.with(view.context).load(person.imageUrl).into(imageView)
            name.setText(person.name)
            description.setText(person.description)

            editBtn.setOnClickListener{
                person.name = name.text.toString()
                person.description = description.text.toString()
                listener.onItemButtonClick(index, person, ItemClickType.EDIT)
            }

            deleteBtn.setOnClickListener{
                listener.onItemButtonClick(index, person, ItemClickType.REMOVE)
            }
        }
    }

    interface ContentListener{
        fun onItemButtonClick(index: Int, person: Person, clickType: ItemClickType)
    }
}
