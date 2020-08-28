package com.brm.brmlabkotlin.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.presenter.DoctorAddPresenter
import com.brm.brmlabkotlin.view.DoctorAddView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_doctor_add.view.*
import java.util.*
import kotlin.collections.HashMap


class DoctorAddFragment : MvpAppCompatFragment(), DoctorAddView {

    val args: DoctorAddFragmentArgs by navArgs()

    private lateinit var categorySpinner: Spinner
    private lateinit var stateSpinner: Spinner

    @InjectPresenter
    lateinit var presenter: DoctorAddPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doctor_add, container, false)

        categorySpinner = view.findViewById(R.id.activity_new_task_category)
        stateSpinner = view.findViewById(R.id.activity_new_task_status)

        spinnerAdapter(R.array.categoryOfMission, spinner = categorySpinner)
        spinnerAdapter(R.array.statusOfMission, spinner = stateSpinner)

        view.activity_new_task_btnSaveTask.setOnClickListener {
           singleSelection(args.region.split(", ").toTypedArray(),
               view = view)
       }
        return view
    }


    override fun goBack(error: String) {
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

    private fun hashMapFunction(view:View): HashMap<String, String>{
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val increasingInt = Random().nextInt()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["medName"] = currentUser.toString()
        hashMap["address"] = view.activity_new_task_descdoes.text.toString()
        hashMap["category"] = stateSpinner.selectedItem.toString()
        hashMap["id"] = "Item$increasingInt"
        hashMap["name"] = view.activity_new_task_datedoes.text.toString()
        hashMap["phone"] = view.activity_new_task_phone.text.toString()
        hashMap["product"] = view.activity_new_task_product.text.toString()
        hashMap["specialization"] = view.activity_new_task_specialty.text.toString()
        hashMap["state"] = categorySpinner.selectedItem.toString()
        hashMap["status"] = view.activity_new_task_doctor_type.text.toString()
        return hashMap
    }

    private fun singleSelection(list: Array<String>, view: View) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите")
        builder.setSingleChoiceItems(list, -1){ _, which ->
            val selectedRegion = list[which]
            presenter.load(arrayOf(args.town, selectedRegion),
                hashMapFunction(view = view))
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}