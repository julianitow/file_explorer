package com.esgi.fileExplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EmptyAdapter : RecyclerView.Adapter<EmptyAdapter.MyViewHolder>() {

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class EmptyViewHolder(itemView: View) : MyViewHolder(itemView)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_dir, parent, false)
        return EmptyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        print("NOTHIN TO DO HERE")
    }

}