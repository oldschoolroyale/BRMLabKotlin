package com.brm.brmlabkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.model.ViewPagerModel
import com.brm.brmlabkotlin.view.NoteView
import com.squareup.picasso.Picasso

class ViewPagerAdapter(var noteView: NoteView) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    private var viewPagerModel: ArrayList<ViewPagerModel> = ArrayList()

    inner class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val imageView = view.findViewById(R.id.view_pager_image) as ImageView
        private val textView = view.findViewById(R.id.view_pager_title) as TextView

        fun bind(viewPagerModel: ViewPagerModel, noteView: NoteView){
            textView.text = viewPagerModel.title
            viewPagerModel.image.let { url -> Picasso.with(itemView.context).load(url).into(imageView)  }
            itemView.setOnClickListener {
                noteView.viewPagerClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_pager_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return viewPagerModel.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(viewPagerModel[position], noteView = noteView)
    }

    fun setUpUserList(userNewList: ArrayList<ViewPagerModel>){
        viewPagerModel.clear()
        viewPagerModel.addAll(userNewList)
        notifyDataSetChanged()
    }
}