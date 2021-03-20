package com.example.android.firebasestart.readtimedb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.firebasestart.R

class MemoAdapter(val context: Context,
                  val memoItems: ArrayList<MemoItem>,
                  val memoViewListener: MemoViewListener)
    : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView?.text = memoItems[position].memoTitle
        holder.contentView?.text = memoItems[position].memoContents
    }

    override fun getItemCount(): Int = memoItems.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var titleView: TextView? = null
        var contentView: TextView? = null

        init {
            titleView = view.findViewById(R.id.memoTitle)
            contentView = view.findViewById(R.id.memoContents)
        }

        override fun onClick(view: View) {
            memoViewListener.onItemClick(adapterPosition, view)
        }

    }
}