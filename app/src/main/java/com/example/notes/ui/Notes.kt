package com.example.notes.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.db.entities.NoteEntity

class Notes(private val context: Context, val listener: NotesItemClickListener) : RecyclerView.Adapter<Notes.NoteViewHolder>() {
    private val NotesList = ArrayList<NoteEntity>()
    private val fullList = ArrayList<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note_tv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setOnClickListener{
            //listener.onItemClicked(NotesList[holder.adapterPosition])
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }
        holder.notes_layout.setOnLongClickListener{
            listener.onLongItemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }
    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList : List<NoteEntity>){
        fullList.clear()
        fullList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface NotesItemClickListener{
        fun onItemClicked(note:NoteEntity)
        fun onLongItemClicked(note:NoteEntity,cardView: CardView)
    }
}