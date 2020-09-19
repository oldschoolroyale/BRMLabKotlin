package com.brm.brmlabkotlin.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.adapter.OrderAdapter
import com.brm.brmlabkotlin.model.OrderModel
import com.brm.brmlabkotlin.presenter.OrderPresenter
import com.brm.brmlabkotlin.view.OrderView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderFragment : MvpAppCompatFragment(), OrderView {


    private val args by navArgs<OrderFragmentArgs>()
    private lateinit var recyclerView: RecyclerView
    private var localArrayList: ArrayList<OrderModel> = ArrayList()
    private lateinit var loader: LazyLoader
    private lateinit var linearLayout: LinearLayout
    private lateinit var adapter: OrderAdapter

    @InjectPresenter
    lateinit var presenter: OrderPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        setHasOptionsMenu(true)
        recyclerView = view.findViewById(R.id.fragment_order_recycler)
        loader = view.findViewById(R.id.fragment_order_dots_loader)
        linearLayout = view.findViewById(R.id.fragment_order_empty_layout)

        adapter = OrderAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val user = FirebaseAuth.getInstance().currentUser?.uid
        presenter.load(user!!)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.order_menu_send){
            presenter.sendInformation(arrayOf(
                args.user,
                SimpleDateFormat("yyyy").format(Calendar.getInstance().time),
                SimpleDateFormat("M").format(Calendar.getInstance().time),
                SimpleDateFormat("d").format(Calendar.getInstance().time),
                args.id
            ),
            localArrayList)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun startLoading() {
        loader.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        linearLayout.visibility = View.INVISIBLE
    }

    override fun endLoading() {
        loader.visibility = View.INVISIBLE
    }

    override fun loadList(list: ArrayList<OrderModel>) {
        recyclerView.visibility = View.VISIBLE
        adapter.setUpUserList(list)
        localArrayList.clear()
        localArrayList.addAll(list)
    }

    override fun loadEmptyList() {
        linearLayout.visibility = View.VISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun goBack() {
        findNavController().navigate(R.id.action_orderFragment_to_noteFragment)
    }

}