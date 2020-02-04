package com.soshi.todo

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Adapter(private val itemArrayList: ArrayList<Item>, private val context: Context): RecyclerView.Adapter<Adapter.ViewHolder>()  {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        setOnItemClickListener(listener)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content.text = itemArrayList[position].content
        val paint = holder.content.paint
        if (itemArrayList[position].isActive) {
            //Activeの場合
            holder.selectBtn.isChecked = false
            holder.content.setTextColor(context.resources.getColor(R.color.primaryTextColor))
            paint.flags = 0
        } else {
            //Completedの場合
            holder.selectBtn.isChecked = true
            holder.content.setTextColor(context.resources.getColor(R.color.secondaryTextColor))
            paint.flags = holder.content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            paint.isAntiAlias = true
        }

        //チェック処理
        holder.selectBtn.setOnClickListener {
            if (holder.selectBtn.isChecked) {
                //チェックされた場合
                listener.onClick(it, position, true)
            } else {
                //チェック解除された場合
                listener.onClick(it, position, false)
            }
        }

        //削除ボタン処理
        holder.deleteBtn.setOnClickListener {
            listener.onClick(it, position, null)
        }
    }

    override fun getItemCount(): Int = itemArrayList.size

    interface OnItemClickListener {
        fun onClick(view: View, position: Int, isChecked: Boolean?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectBtn: CheckBox = view.findViewById(R.id.select_btn)
        val content: TextView = view.findViewById(R.id.content)
        val deleteBtn: ImageButton = view.findViewById(R.id.delete_btn)
    }

    internal fun updateAdapter(itemArrayList: ArrayList<Item>) {
        this.itemArrayList.clear()
        this.itemArrayList.addAll(itemArrayList)
        notifyDataSetChanged()
    }
}