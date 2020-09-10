package com.brm.brmlabkotlin.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.activities.MainActivity
import com.brm.brmlabkotlin.databinding.FragmentUpdateBinding
import com.brm.brmlabkotlin.presenter.UpdateDoctorPresenter
import com.brm.brmlabkotlin.view.UpdateDoctor
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class UpdateFragmentDoctor :  MvpAppCompatFragment(), UpdateDoctor {

    private var LOCATION_REQUEST_CODE = 10001
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
        val user = FirebaseAuth.getInstance().currentUser?.uid
            if (item.itemId == R.id.update_menu_start){
                if (check()){
                    presenter.startVisit(arrayOf(user!!, yearString, monthString, dayString,
                    args.id), requireContext())}
                else{
                    askPermission()
                }

            }
            if (item.itemId == R.id.update_menu_stop){

            }
            if (item.itemId == R.id.update_menu_save){
                presenter.sendInformation(binding.currentDescriptionEt.text.toString(),
                    arrayOf(user!!, yearString, monthString, dayString,
                        args.id), requireContext())
            }
        return super.onOptionsItemSelected(item)
    }

    private fun askPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            ActivityCompat.requestPermissions(MainActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE)
        }
        else{
            ActivityCompat.requestPermissions(MainActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
    }

    private fun checkDate(): Boolean{
        return yearString == args.year && monthString == args.month && dayString == args.day
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private fun check(): Boolean{
        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showError("Права выданы!")
            }
        }
    }
}