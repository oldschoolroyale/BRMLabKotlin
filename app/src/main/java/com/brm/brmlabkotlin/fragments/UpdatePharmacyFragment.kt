package com.brm.brmlabkotlin.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.databinding.FragmentUpdatePharmacyBinding
import com.brm.brmlabkotlin.presenter.UpdatePharmacyPresenter
import com.brm.brmlabkotlin.view.UpdateDoctor
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class UpdatePharmacyFragment : MvpAppCompatFragment(), UpdateDoctor {

    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)

    private var _binding: FragmentUpdatePharmacyBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdatePharmacyFragmentArgs>()

    @InjectPresenter
    lateinit var presenter: UpdatePharmacyPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    _binding = FragmentUpdatePharmacyBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.show()

        if (UpdateFragmentDoctor().checkDate(
                yearString, monthString, dayString
            )){
            setHasOptionsMenu(true)
        }


        binding.args = args
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_pharmacy_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance().currentUser?.uid
        when(item.itemId){
            R.id.save_update_pharmacy_menu ->
            {
                presenter.send(binding.fragmentUpdatePharmacyEt.text.toString(),
                    arrayOf(user!!, yearString, monthString, dayString,
                    args.id)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}