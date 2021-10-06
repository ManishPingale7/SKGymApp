package com.example.skgym.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.skgym.R

class GridAdapter(
    var requireContext: Context,
    var arrayNames: ArrayList<String>,
    var arrayImages: ArrayList<Uri>
) : BaseAdapter() {

    var context: Context? = requireContext

    var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return arrayNames.size
    }

    override fun getItem(position: Int): Any? {
        return arrayImages[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (inflater == null) inflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.grid_item, null)
        }
        val imageView: ImageView = convertView!!.findViewById(R.id.grid_image)
        val textView: TextView = convertView.findViewById(R.id.item_name)
        Glide.with(convertView)
            .load(arrayImages[position])
            .fitCenter()
            .into(imageView)

        textView.text = arrayNames[position]
        return convertView
    }


}