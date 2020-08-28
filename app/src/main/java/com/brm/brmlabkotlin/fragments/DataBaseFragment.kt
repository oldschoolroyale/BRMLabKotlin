package com.brm.brmlabkotlin.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.adapter.DataBaseAdapter
import com.brm.brmlabkotlin.model.DataBaseModel
import com.brm.brmlabkotlin.presenter.DataBasePresenter
import com.brm.brmlabkotlin.view.DataBaseView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_data_base.*


class DataBaseFragment : MvpAppCompatFragment(), DataBaseView{

    val args: DataBaseFragmentArgs by navArgs()

    @InjectPresenter
    lateinit var presenter: DataBasePresenter

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DataBaseAdapter
    private val localArray: ArrayList<DataBaseModel> = ArrayList()
    private lateinit var regionArray: List<String>
    private lateinit var spinner: Spinner

    private lateinit var town: String
    private lateinit var region: String

    private lateinit var loader: LazyLoader
    private lateinit var emptyLayout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_data_base, container, false)

        loader = view.findViewById(R.id.activity_data_base_dots_loader)
        emptyLayout = view.findViewById(R.id.activity_data_base_no_data_constraint)
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        //getIntent
        town = args.town
        regionArray = args.region.split(", ")
        region = regionArray[0]

        //RecyclerView
        adapter = DataBaseAdapter(this)
        recyclerView = view.findViewById(R.id.activity_data_base_recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)


        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.activity_data_base_fb)
            floatingActionButton.setOnClickListener{
            singleSelection(regionArray.toTypedArray())
        }

        return view
    }

    private fun singleSelection(list: Array<String>) {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите")
        builder.setSingleChoiceItems(list, -1){ _, which ->
            region = list[which]
            if (spinner.selectedItemPosition == 0){
                presenter.startLoading("Doctor", town = town, region = region)
            }
            else{
                spinner.setSelection(0)
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    override fun startLoading() {
        loader.visibility = View.VISIBLE
        emptyLayout.visibility = View.INVISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    override fun endLoading() {
        loader.visibility = View.INVISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun loadList(list: ArrayList<DataBaseModel>) {
        recyclerView.visibility = View.VISIBLE
        adapter.loadList(list)
        localArray.clear()
        localArray.addAll(list)
    }

    override fun loadEmptyList() {
        activity_data_base_no_data_constraint.visibility = View.VISIBLE
    }

    override fun itemClick(position: Int) {
        findNavController().navigate(DataBaseFragmentDirections.actionDoctorListFragmentToNoteAddFragment(
            arrayOf()
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data_base_menu, menu)
        val item = menu.findItem(R.id.data_base_menu_spinner)
        spinner = item.actionView as Spinner

        //Spinner Adapter
        val categoryAdapter : ArrayAdapter<CharSequence> =
            context?.let { ArrayAdapter.createFromResource(it, R.array.dataCategory, R.layout.custom_spinner) } as ArrayAdapter<CharSequence>
        categoryAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner.adapter = categoryAdapter

        //Spinner Click
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context, "Ничего не выбранно!", Toast.LENGTH_LONG).show()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(parent?.id){
                    R.id.data_base_menu_spinner -> {
                        presenter.startLoading(type = parent.getItemAtPosition(position).toString(), town = town, region = region)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.data_base_menu_doctor){
            findNavController().navigate(DataBaseFragmentDirections.actionDoctorListFragmentToDoctorAddFragment(
                town, region
            ))
        }
        else{
            findNavController().navigate(DataBaseFragmentDirections.actionDoctorListFragmentToPharmacyAddFragment(
                town, region
            ))
        }
        return super.onOptionsItemSelected(item)
    }
}