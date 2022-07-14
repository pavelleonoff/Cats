package com.redprism.cats

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redprism.cats.adapters.CatsAdapter
import com.redprism.cats.data.Cat
import com.redprism.cats.data.MainViewModel
import com.redprism.cats.data.Operation
import com.redprism.cats.data.Pref

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var catsRecyclerView: RecyclerView
    private lateinit var catsAdapter: CatsAdapter
    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var filterPref: SharedPreferences
    private lateinit var dBPref: SharedPreferences
    private lateinit var filtersButton: TextView
    private lateinit var filterStatus: TextView
    private lateinit var filtersButtonClear: ImageView
    private lateinit var internetError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filterPref = getSharedPreferences(Pref.prefFilterName, MODE_PRIVATE)
        dBPref = getSharedPreferences(Pref.prefDBDownloadName, MODE_PRIVATE)
        filtersButton = findViewById(R.id.filtersButton)
        filterStatus = findViewById(R.id.filterStatus)
        filtersButtonClear = findViewById(R.id.filtersButtonClear)
        catsRecyclerView = findViewById(R.id.catsRecyclerView)
        internetError = findViewById(R.id.internetError)
        catsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        catsAdapter = CatsAdapter(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels
        )
        viewModel.filteredCats.observe(this) { actualCats ->
            catsAdapter.setCats(actualCats as ArrayList<Cat>)
            filterUIChange()
        }
        catsRecyclerView.adapter = catsAdapter
        catsRecyclerView.setHasFixedSize(true);
        catsRecyclerView.setItemViewCacheSize(20);
        catsAdapter.setOnCatClickListener(object : CatsAdapter.OnCatClickListener {
            override fun onCatClick(id: Int) {
                val catId: String = catsAdapter.getAdapterCats()[id].id
                val image: String = catsAdapter.getAdapterCats()[id].image
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", catId)
                intent.putExtra("image", image)
                startActivity(intent)
            }
        })
        filtersButton.setOnClickListener(this)
        filtersButtonClear.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getActualCatsFromDB()
    }

    private fun filterUIChange() {
        if (!dBPref.getBoolean(Pref.dataDownloads, false)) {
            internetError.visibility = View.VISIBLE
        } else {
            internetError.visibility = View.GONE
            filtersButton.visibility = View.VISIBLE
        }
        if (filterPref.getBoolean(Pref.filterOn, false)) {
            filtersButton.background = ContextCompat.getDrawable(this, R.drawable.toggle_on)
            filtersButtonClear.visibility = View.VISIBLE
            filterStatus.text = Operation().setFilterStatus(filterPref)
        } else {
            filtersButton.background = ContextCompat.getDrawable(this, R.drawable.toggle_off)
            filtersButtonClear.visibility = View.INVISIBLE
            filterStatus.text = ""
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filtersButton -> {
                val intent = Intent(this, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.filtersButtonClear -> {
                filterPref.edit().clear().apply()
                viewModel.getActualCatsFromDB()
            }
        }
    }
}
