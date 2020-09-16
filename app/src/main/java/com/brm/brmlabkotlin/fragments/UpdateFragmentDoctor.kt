package com.brm.brmlabkotlin.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.databinding.FragmentUpdateBinding
import com.brm.brmlabkotlin.presenter.UpdateDoctorPresenter
import com.brm.brmlabkotlin.view.UpdateDoctor
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragmentDoctor :  MvpAppCompatFragment(), UpdateDoctor {

    companion object{
        private var LOCATION_REQUEST_CODE = 10001
    }
    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)


    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    @InjectPresenter
    lateinit var presenter: UpdateDoctorPresenter

    private val args by navArgs<UpdateFragmentDoctorArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()

        if (checkDate(yearString, monthString, dayString)){
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
        val user = FirebaseAuth.getInstance().currentUser?.uid

            if (item.itemId == R.id.update_menu_save){
                presenter.sendInformation(binding.currentDescriptionEt.text.toString(),
                    arrayOf(user!!, yearString, monthString, dayString,
                        args.id))
            }
        if (item.itemId == R.id.update_menu_delete){
            presenter.startDelete(arrayOf(user!!, yearString, monthString, dayString,
                    args.id))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkDate(year: String,
    month: String, day: String): Boolean{
        return year == args.year && month == args.month && day == args.day
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun goBack(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        findNavController().navigate(UpdateFragmentDoctorDirections.actionUpdateFragmentToNoteFragment())
    }

}