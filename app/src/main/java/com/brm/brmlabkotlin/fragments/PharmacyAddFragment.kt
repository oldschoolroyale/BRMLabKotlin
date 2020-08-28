package com.brm.brmlabkotlin.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.presenter.PharmacyAddPresenter
import com.brm.brmlabkotlin.view.PharmacyAddView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_pharmacy_add.view.*
import java.util.*
import kotlin.collections.HashMap


class PharmacyAddFragment : MvpAppCompatFragment(), PharmacyAddView {

   val args: PharmacyAddFragmentArgs by navArgs()
    private lateinit var spinner: Spinner

    @InjectPresenter
    lateinit var presenter: PharmacyAddPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pharmacy_add, container, false)

        spinner = view.findViewById(R.id.activity_pharmacy_status)

        spinnerAdapter(R.array.statusOfMission, spinner)

        view.activity_pharmacy_btnSaveTask.setOnClickListener {
            singleSelection(args.region.split(", ").toTypedArray(),
                view = view)
        }

        return view
    }
    private fun singleSelection(list: Array<String>, view: View) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите")
        builder.setSingleChoiceItems(list, -1){ _, which ->
            val selectedRegion = list[which]
            presenter.startLoading(arrayOf(args.town, selectedRegion),
                hashMapFunction(view = view))
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun hashMapFunction(view: View): HashMap<String, String>{

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val increasingInt = Random().nextInt()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["address"] = view.activity_pharmacy_address.text.toString()
        hashMap["comments"] = view.activity_pharmacy_addons.text.toString()
        hashMap["name"] = view.activity_pharmacy_name.text.toString()
        hashMap["owner"] = view.activity_pharmacy_owner_name.text.toString()
        hashMap["phone"] = view.activity_pharmacy_phone.text.toString()
        hashMap["status"] = spinner.selectedItem.toString()
        hashMap["medName"] = currentUser.toString()
        hashMap["id"] = "Item$increasingInt"
        hashMap["employee"] = view.activity_pharmacy_employees_name.text.toString()
        return hashMap
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private fun spinnerAdapter(array: Int, spinner: Spinner){
        val categoryAdapter = context?.let { ArrayAdapter.createFromResource(
            it,
            array,
            android.R.layout.simple_spinner_item
        )
        }
        categoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = categoryAdapter
    }
}