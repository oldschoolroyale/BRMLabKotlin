package com.brm.brmlabkotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.activities.RegistrationActivity
import com.brm.brmlabkotlin.adapter.NotesAdapter
import com.brm.brmlabkotlin.adapter.ViewPagerAdapter
import com.brm.brmlabkotlin.helper.ViewPagerHelper
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.presenter.NotePresenter
import com.brm.brmlabkotlin.view.NoteView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_note.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NoteFragment : MvpAppCompatFragment(), NoteView, DatePickerDialog.OnDateSetListener {

    private lateinit var loader: LazyLoader
    private lateinit var imageView: ImageView
    private lateinit var nameText: TextView
    private lateinit var dateText: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private var localArray: ArrayList<NoteModel> = ArrayList()
    private var viewHelper: ViewPagerHelper = ViewPagerHelper()
    private lateinit var localTypedArray: Array<String>

    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)

    private lateinit var token: String
    private var takenData: String = "$dayString/$monthString/$yearString"

    @InjectPresenter
    lateinit var presenter: NotePresenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()

        loader = view.findViewById(R.id.fragment_notes_dots_loader)
        imageView = view.findViewById(R.id.fragment_note_circle_image)
        nameText = view.findViewById(R.id.fragment_note_name)
        dateText = view.findViewById(R.id.fragment_note_date_text)
        dateText.text = takenData
        dateText.setOnClickListener {
            val now : Calendar = Calendar.getInstance()
            val dpd : DatePickerDialog = DatePickerDialog.newInstance(
                this,
                now[Calendar.YEAR], // Initial year selection
                // Initial year selection
                now[Calendar.MONTH], // Initial month selection
                // Initial month selection
                now[Calendar.DAY_OF_MONTH] // Inital day selection

            )
            fragmentManager?.let { it1 -> dpd.show(it1, "Datepickerdialog") }
        }

        //ViewPagerAdapter
        val viewPagerAdapter = ViewPagerAdapter(
            viewHelper.modelList(),
                this
        )
        val viewPager : ViewPager2 = view.findViewById(R.id.fragment_note_view_pager2)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.notifyDataSetChanged()
        val indicatorLayout: LinearLayout = view.findViewById(R.id.fragment_note_indicator_constraint)
        context?.let { viewHelper.setUpIndicator(viewPagerAdapter, indicatorLayout, it) }
        context?.let { viewHelper.setCurrentIndicator(0, indicatorLayout, it) }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                context?.let { viewHelper.setCurrentIndicator(position, indicatorLayout, it) }
            }
        })

        //Recycler
        adapter = NotesAdapter(this)
        recyclerView = view.findViewById(R.id.fragment_note_recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        val mAuth = FirebaseAuth.getInstance().currentUser
        token = mAuth!!.uid
        presenter.firstLoad(token = token, year = yearString, month = monthString, day = dayString)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main_menu_exit ->{
                FirebaseAuth.getInstance().signOut()
                activity?.finish()
                startActivity(Intent(context, RegistrationActivity::class.java))
            }
            R.id.main_menu_setting -> Toast.makeText(context,
            "Тут пока пусто", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun startLoading() {
        loader.visibility = View.VISIBLE
        fragment_note_ll_empty.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun endLoading() {
        loader.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun loadEmptyList() {
        fragment_note_ll_empty.visibility = View.VISIBLE
    }

    override fun setUpAccount(array: Array<String>) {
        array[0].let { url -> Picasso.with(context).load(url).into(imageView)  }
        nameText.text = array[1]
        localTypedArray = array
    }

    override fun loadList(list: ArrayList<NoteModel>) {
        recyclerView.visibility = View.VISIBLE
        adapter.setUpUserList(list)
        localArray.clear()
        localArray.addAll(list)
    }

    override fun itemClick(position: Int) {
        findNavController().navigate(R.id.action_noteFragment_to_updateFragment)
    }

    override fun viewPagerClick(position: Int) {
        findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToDoctorListFragment(
            town = localTypedArray[2],
            region = localTypedArray[3]
        ))
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        yearString = year.toString()
        dayString = dayOfMonth.toString()
        monthString = (monthOfYear + 1).toString()
        takenData = "$dayString/$monthString/$yearString"
        dateText.text = takenData
        presenter.load(token = token, year = yearString, month = monthString, day = dayString)
    }

   }