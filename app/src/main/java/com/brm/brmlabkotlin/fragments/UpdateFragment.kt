package com.brm.brmlabkotlin.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.databinding.FragmentUpdateBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment :  MvpAppCompatFragment() {


    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)


    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()

        if (checkDate()){
            setHasOptionsMenu(true)
        }
        binding.args = args
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.update_menu_start){

            }
            if (item.itemId == R.id.update_menu_stop){

            }
            if (item.itemId == R.id.update_menu_save){

            }
        return super.onOptionsItemSelected(item)
    }

    private fun checkDate(): Boolean{
        return yearString == args.year && monthString == args.month && dayString == args.day
    }
}