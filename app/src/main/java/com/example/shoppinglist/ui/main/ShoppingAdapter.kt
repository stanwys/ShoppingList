package com.example.shoppinglist.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.shoppinglist.R
import com.example.shoppinglist.model.Item
import com.example.shoppinglist.model.ShoppingProject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ShoppingAdapter<T> (private val mContext: Context, private val mResource: Int,
                          objects: ArrayList<T>,
                          private val mListener: ListAdapterListener ) :
    ArrayAdapter<T>(mContext, mResource, objects) {

    interface ListAdapterListener {
        fun onClickRemoveButton(position: Int)
        fun onClickArchiveButton(position: Int)
        fun onClickCompleteButton(position: Int)
        fun onClickNewActivityButton(position: Int)
    }

    override fun getView(position: Int, convView: View?, parent: ViewGroup): View {

        var convertView = convView
        val element = getItem(position)

        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            convertView = inflater.inflate(mResource, parent, false)
        }

        val titleView = convertView?.findViewById<TextView>(R.id.Title)
        val dateView = convertView?.findViewById<TextView>(R.id.Date)
        val checkBox = convertView?.findViewById<CheckBox>(R.id.CompletedBox)
        if (element is Item){
            titleView?.text = element.name
            var checked = false
            if (element.completed == 1){
                checked = true
            }
            checkBox?.isChecked = checked
        }
        else if (element is ShoppingProject){
            titleView?.text = element.name
            dateView?.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(element.date)
        }

        val removeButton = convertView?.findViewById<ImageButton>(R.id.RemoveButton)

        removeButton?.setOnClickListener { l ->
            mListener.onClickRemoveButton(position);
            this.remove(element)
            notifyDataSetChanged()
        }

        val archiveButton = convertView?.findViewById<Button>(R.id.ArchiveButton)
        archiveButton?.setOnClickListener { l ->
            mListener.onClickArchiveButton(position);
            this.remove(element)
            notifyDataSetChanged()
        }

        val completedBox = convertView?.findViewById<CheckBox>(R.id.CompletedBox)
        completedBox?.setOnClickListener { l ->
            mListener.onClickCompleteButton(position);
        }

        titleView?.setOnClickListener { l ->
            mListener.onClickNewActivityButton(position)
        }

        return convertView!!
    }
}