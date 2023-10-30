package com.example.notesapp_roomdatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToAdapter(var datas : List<Datas>,val noteClickListener: NoteClickListener):RecyclerView.Adapter<ToAdapter.viewHolder>()
{

    inner class viewHolder(itemview : View,noteClickListener: NoteClickListener) : RecyclerView.ViewHolder(itemview)
    {
        init {
            itemview.setOnClickListener {
                noteClickListener.itemClick(adapterPosition)
            }
        }
        var title = itemview.findViewById<TextView>(R.id.notetitle)
        var content = itemview.findViewById<TextView>(R.id.notebody)
        var date = itemview.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notelayout,parent,false)
        return viewHolder(view,noteClickListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.apply {
            title.text=datas[position].title_content
            content.text=datas[position].body_content
            date.text=datas[position].date


        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    interface NoteClickListener
    {
        fun itemClick(position: Int)
    }

    fun clearItems()
    {
        datas= emptyList()
        notifyDataSetChanged()
    }
}