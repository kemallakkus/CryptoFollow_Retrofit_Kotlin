package com.kemalakkus.retrofitkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.retrofitkotlin.R
import com.kemalakkus.retrofitkotlin.model.CryptoModel
import okhttp3.internal.http2.Http2Connection.Listener

class RecyclerViewAdapter(private val cryptoList : ArrayList<CryptoModel>,private val listener : Listener) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(cryptoModel: CryptoModel)
    }

    private val colors : Array<String> = arrayOf("#c7e7c9","#ebbbbb","#22fb4f","#540c3b","#d7582b","#dc1d1d","#2b889c","#6a9409")


    class RowHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(cryptoModel: CryptoModel, colors: Array<String>,position: Int, listener : Listener){
            itemView.setOnClickListener {
                listener.onItemClick(cryptoModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))
            itemView.findViewById<TextView>(R.id.text_name).text = cryptoModel.currency
            itemView.findViewById<TextView>(R.id.text_price).text = cryptoModel.price
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent,false)
        return RowHolder(view)

    }



    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(cryptoList[position],colors,position,listener)

    }
}