package com.brm.brmlabkotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.view.NoteView

class NotesAdapter(var notesView: NoteView): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var notesList: ArrayList<NoteModel> = ArrayList()

    fun setUpUserList(userNewList: ArrayList<NoteModel>){
        notesList.clear()
        notesList.addAll(userNewList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notesList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotesViewHolder){
            holder.bind(notesList[position], notesView = notesView)
        }
    }
    private class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var nameText = view.findViewById(R.id.notes_list_item_name_text) as TextView
        var addressText = view.findViewById(R.id.notes_list_item_address) as TextView
        var priorityCard = view.findViewById(R.id.notes_list_item_priority_indicator) as CardView
        var mainCard = view.findViewById(R.id.notes_List_item_card_view) as CardView

        @SuppressLint("SetTextI18n")
        fun bind(notesModel: NoteModel, notesView: NoteView){
            nameText.text = notesModel.name
            addressText.text = notesModel.address
            mainCard.setOnClickListener{ notesView.itemClick(adapterPosition);}

            priorityCard.setCardBackgroundColor(
             if (notesModel.visit == "Визит окончен"){
                        priorityCard.context.getColor(android.R.color.holo_green_dark)
                    }
             else{
                        priorityCard.context.getColor(android.R.color.holo_red_light)
                    })

            mainCard.setCardBackgroundColor(
                    if (notesModel.type == "Визит в аптеку"){
                        mainCard.context.getColor(R.color.ping_lovando)
                    }
                            else{
                        mainCard.context.getColor(R.color.light_blue)
                    })
        }
    }
}