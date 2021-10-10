package com.example.skgym.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.skgym.R
import com.example.skgym.data.ProductCategory

class GridAdapter(
    requireContext: Context,
    var obj: ArrayList<ProductCategory>,
) : BaseAdapter() {

    var context: Context = requireContext

    var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return obj.size
    }

    override fun getItem(position: Int): Any {
        return obj[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (inflater == null)
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.grid_item, null)
        }
        val imageView: ImageView = convertView!!.findViewById(R.id.grid_image)
        val textView: TextView = convertView.findViewById(R.id.item_name)


        Glide.with(convertView)
            .load(obj[position].image)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .fitCenter()
            .into(imageView)

        imageView.visibility = View.VISIBLE
        textView.text = obj[position].name
        return convertView
    }


}