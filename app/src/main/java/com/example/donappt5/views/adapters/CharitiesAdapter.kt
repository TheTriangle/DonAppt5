package com.example.donappt5.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.donappt5.R
import com.example.donappt5.data.model.Charity
import com.squareup.picasso.Picasso

class CharitiesAdapter(var ctx: Context?, var objects: ArrayList<Charity>) :
    BaseAdapter() {
    var lInflater: LayoutInflater = ctx
        ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // кол-во элементов
    override fun getCount(): Int {
        return objects.size
    }

    // элемент по позиции
    override fun getItem(position: Int): Any {
        return objects[position]
    }

    // id по позиции
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // пункт списка
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // используем созданные, но не используемые view
        var view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item_charity, parent, false)
        }
        val c = getCharity(position)

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        (view?.findViewById<View>(R.id.tvDescr) as TextView).text = c.briefDescription
        (view.findViewById<View>(R.id.tvName) as TextView).text = c.name

        return view
    }

    fun getCharity(position: Int): Charity {
        return getItem(position) as Charity
    }

    fun addData(list: List<Charity>?) {
        objects.addAll(list!!)
    }

    fun clear() {
        objects.clear()
    }
}