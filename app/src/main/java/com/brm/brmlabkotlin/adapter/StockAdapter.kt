package com.brm.brmlabkotlin.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.model.StockModel
import org.w3c.dom.Text

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private val ordersList: ArrayList<StockModel> = ArrayList()

    fun setUpUserList(userNewList: ArrayList<StockModel>){
        ordersList.clear()
        ordersList.addAll(userNewList)
        notifyDataSetChanged()
    }
    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.stock_item, null)
        return StockViewHolder(view, StocksEditListener())
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        @NonNull holder: StockViewHolder,
        position: Int
    ) {
        holder.ordersEditListener.updatePosition(holder.adapterPosition)
        holder.medicationsText.text = ordersList[position].name

        holder.stockText.text = "В складу: ${ordersList[position].stock.toString()}"

        holder.fullText.text = "100% ${ordersList[position].full.toString()}"
        holder.halfText.text = "50% ${ordersList[position].half.toString()}"
        holder.semiText.text = "25% ${ordersList[position].semi.toString()}"

        holder.editText.setText(ordersList[position].quantity.toString())
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class StocksEditListener : TextWatcher {
        private var position = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            if (s.toString() == "") {
                ordersList[position].quantity = 0
            } else {
                ordersList[position].quantity = s.toString().toInt()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }


    class StockViewHolder(
        @param:NonNull var view: View,
        stocksEditListener: StocksEditListener
    ) :
        RecyclerView.ViewHolder(view) {
        var medicationsText: TextView = view.findViewById(R.id.stock_item_name)
        var editText: EditText = view.findViewById(R.id.stock_item_quantity)
        var stockText: TextView = view.findViewById(R.id.stock_item_stock)
        var fullText: TextView = view.findViewById(R.id.stock_item_full)
        var halfText: TextView = view.findViewById(R.id.stock_item_half)
        var semiText : TextView = view.findViewById(R.id.stock_item_semi)

        var ordersEditListener: StockAdapter.StocksEditListener = stocksEditListener

        init {
            editText.addTextChangedListener(ordersEditListener)
        }
    }

}