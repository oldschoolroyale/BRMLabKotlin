package com.brm.brmlabkotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.model.DataBaseModel
import com.brm.brmlabkotlin.view.DataBaseView

class DataBaseAdapter(var dataBaseView: DataBaseView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataBaseList : ArrayList<DataBaseModel> = ArrayList()

    fun loadList(dataNewList: ArrayList<DataBaseModel>){
        dataBaseList.clear()
        dataBaseList.addAll(dataNewList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataBaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.data_base_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataBaseList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataBaseViewHolder){
            holder.bind(dataBaseList = dataBaseList[position], dataBaseView = dataBaseView)
        }
    }
    private class DataBaseViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var nameText = view.findViewById(R.id.data_base_list_item_name) as TextView
        var addressText = view.findViewById(R.id.data_base_list_item_address) as TextView
        var phoneText = view.findViewById(R.id.data_base_list_item_phone) as TextView
        var statusText = view.findViewById(R.id.data_base_list_item_status) as TextView
        var categoryEmployeeText = view.findViewById(R.id.data_base_list_item_category_employee) as TextView
        var stateOwnerTextView = view.findViewById(R.id.data_base_list_item_state_owner) as TextView

        @SuppressLint("SetTextI18n")
        fun bind(dataBaseList: DataBaseModel, dataBaseView: DataBaseView){
            nameText.text = dataBaseList.name
            addressText.text = dataBaseList.address
            if (dataBaseList.specialization.isNullOrEmpty()){
                phoneText.text = dataBaseList.phone
            }
            else{
                phoneText.text = dataBaseList.specialization
            }
            statusText.text = dataBaseList.status
            if (dataBaseList.category.isNullOrEmpty()){
                categoryEmployeeText.text = "Зав: " + dataBaseList.employee
            }
            else{
                categoryEmployeeText.text = dataBaseList.category
            }
            if (dataBaseList.state.isNullOrEmpty()){
                stateOwnerTextView.text = "Владелец: " + dataBaseList.owner
            }
            else{
                stateOwnerTextView.text = dataBaseList.state
            }
        }
    }
}