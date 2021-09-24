package com.example.shoppinglist.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.shoppinglist.R
import com.example.shoppinglist.model.ShoppingProject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ShoppingProjectAdapter (private val mContext: Context, private val mResource: Int,
                              objects: ArrayList<ShoppingProject>,
                              private val mListener: ListAdapterListener ) :
    ArrayAdapter<ShoppingProject>(mContext, mResource, objects) {

    interface ListAdapterListener {
        fun onClickRemoveButton(position: Int)
        fun onClickArchiveButton(position: Int)
    }

    override fun getView(position: Int, convView: View?, parent: ViewGroup): View {

        var convertView = convView
        val element = getItem(position)

        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            convertView = inflater.inflate(mResource, parent, false)
        }

        val titleView = convertView?.findViewById<TextView>(R.id.Title)//.Title?.setText(element.name)
        val dateView = convertView?.findViewById<TextView>(R.id.Date)
        titleView?.text = element?.name
        dateView?.text = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(element?.date)


        val removeButton = convertView?.findViewById<ImageButton>(R.id.RemoveButton)

        removeButton?.setOnClickListener { l ->
            //l.visibility = View.GONE
            mListener.onClickRemoveButton(position);
            this.remove(element)
            notifyDataSetChanged()
        }

        val archiveButton = convertView?.findViewById<Button>(R.id.ArchiveButton)
        archiveButton?.setOnClickListener { l ->
            //l.visibility = View.GONE
            mListener.onClickArchiveButton(position);
            this.remove(element)
            notifyDataSetChanged()
        }

        return convertView!!
    }
}