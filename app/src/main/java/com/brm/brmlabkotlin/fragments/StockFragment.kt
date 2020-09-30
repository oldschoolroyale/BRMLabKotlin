package com.brm.brmlabkotlin.fragments

import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.adapter.StockAdapter
import com.brm.brmlabkotlin.model.StockModel
import com.brm.brmlabkotlin.presenter.StockPresenter
import com.brm.brmlabkotlin.view.StockView
import com.google.android.material.bottomsheet.BottomSheetDialog


class StockFragment : MvpAppCompatFragment(), StockView {

    private lateinit var loading: LazyLoader
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyLayout: LinearLayout

    private var localArray: ArrayList<StockModel> = ArrayList()
    private lateinit var adapter: StockAdapter

    private val args by navArgs<StockFragmentArgs>()

    @InjectPresenter
    lateinit var presenter: StockPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stock, container, false)

        loading = view.findViewById(R.id.fragment_stock_dots_loader)
        recyclerView = view.findViewById(R.id.fragment_stock_recycler)
        emptyLayout = view.findViewById(R.id.fragment_stock_empty_layout)

        setHasOptionsMenu(true)
        adapter = StockAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        presenter.load(args.user)

        return view
    }

    override fun startLoading() {
        loading.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        emptyLayout.visibility = View.INVISIBLE
    }

    override fun endLoading() {
        loading.visibility = View.INVISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun loadEmptyLayout() {
        emptyLayout.visibility = View.VISIBLE
    }

    override fun loadList(list: ArrayList<StockModel>) {
        recyclerView.visibility = View.VISIBLE
        adapter.setUpUserList(list)
        localArray.clear()
        localArray.addAll(list)

    }

    override fun goBack() {
        TODO("ASAP")
    }

    override fun showAlert(message: String) {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(), R.style.BottomSheetDialogTheme
        )
        val bottomSheetView: View = LayoutInflater.from(context)
            .inflate(R.layout.layout_bottom_sheet,
                view?.findViewById(R.id.bottomSheetContainer))
        bottomSheetView.findViewById<TextView>(R.id.layout_bottom_sheet_textView).text = message
        bottomSheetView.findViewById<Button>(R.id.layout_bottom_sheet_button_order).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.stock_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.stock_menu_calculate -> presenter.startCalculation(localArray, "semi")
        }
        return super.onOptionsItemSelected(item)
    }
}