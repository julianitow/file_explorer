package com.esgi.fileExplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import kotlinx.android.synthetic.main.dir_item.view.*
import java.io.File

class RecyclerAdapter(private val dataset: Array<String>, private val onClickListener: (View, String) -> Unit) : Adapter<RecyclerAdapter.MyViewHolder>() {

    private val TYPE_FILE = 1
    private val TYPE_DIRECTORY = 2

    open class MyViewHolder(itemView: View) : ViewHolder(itemView)

    class FileViewHolder(itemView: View) : MyViewHolder(itemView)

    class DirectoryViewHolder(itemView: View) : MyViewHolder(itemView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        if(viewType == TYPE_FILE){
            view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
            return FileViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.dir_item, parent, false)
            return DirectoryViewHolder(view)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val text = dataset[position].split("/")
        holder.itemView.fileTextView.text = text[text.size - 1]
        holder.itemView.setOnClickListener{ view ->
            onClickListener.invoke(view, dataset[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(File(dataset[position]).isFile){
            return TYPE_FILE
        } else {
            return TYPE_DIRECTORY
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}