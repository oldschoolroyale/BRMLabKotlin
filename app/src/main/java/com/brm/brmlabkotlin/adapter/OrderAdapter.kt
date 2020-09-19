package com.brm.brmlabkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.model.OrderModel
import com.brm.brmlabkotlin.model.ViewPagerModel
import com.brm.brmlabkotlin.view.NoteView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class OrderAdapter :
    RecyclerView.Adapter<OrderAdapter.OrdersViewHolder>() {
    private val ordersList: ArrayList<OrderModel> = ArrayList()

    fun setUpUserList(userNewList: ArrayList<OrderModel>){
        ordersList.clear()
        ordersList.addAll(userNewList)
        notifyDataSetChanged()
    }
    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): OrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.order_item, null)
        return OrdersViewHolder(view, OrdersEditListener())
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        @NonNull holder: OrdersViewHolder,
        position: Int
    ) {
        holder.ordersEditListener.updatePosition(holder.adapterPosition)
        holder.medicationsText.text = ordersList[position].name
        holder.editText.setText(ordersList[position].quantity.toString())
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class OrdersEditListener : TextWatcher {
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
    class OrdersViewHolder(
        @param:NonNull var view: View,
        ordersEditListener: OrdersEditListener
    ) :
        RecyclerView.ViewHolder(view) {
        var medicationsText: TextView = view.findViewById(R.id.order_item_name)
        var editText: EditText = view.findViewById(R.id.order_item_quantity)
        var ordersEditListener: OrdersEditListener = ordersEditListener

        init {
            editText.addTextChangedListener(ordersEditListener)
        }
    }

}