package com.redprism.catbreeds

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
import com.redprism.catbreeds.adapters.CatsAdapter
import com.redprism.catbreeds.data.Cat
import com.redprism.catbreeds.viewmodel.MainViewModel
import com.redprism.catbreeds.utils.Filtering
import com.redprism.catbreeds.utils.Pref

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
    private lateinit var downloadImg : ImageView

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
        downloadImg = findViewById(R.id.downloading)
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
            errorUI()
        }
        catsRecyclerView.adapter = catsAdapter
        catsRecyclerView.setHasFixedSize(true)
        catsRecyclerView.setItemViewCacheSize(20)
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
        errorUI()
    }

    fun errorUI(){
        if (dBPref.getBoolean(Pref.dataNotDownloads, true)) {
            filtersButton.visibility = View.INVISIBLE
            if(!viewModel.isInternetAvailable(this)){
                internetError.visibility = View.VISIBLE
                downloadImg.visibility = View.GONE
            }
            else{
                internetError.visibility = View.GONE
                downloadImg.visibility = View.VISIBLE
            }
        } else {
            if(dBPref.getBoolean(Pref.uiErrorsVisible, true)) {
                internetError.visibility = View.GONE
                downloadImg.visibility = View.GONE
                filtersButton.visibility = View.VISIBLE
                dBPref.edit().putBoolean(Pref.uiErrorsVisible, false).apply()
            }
        }
    }

    private fun filterUIChange() {
        if (filterPref.getBoolean(Pref.filterOn, false)) {
            filtersButton.background = ContextCompat.getDrawable(this, R.drawable.toggle_on)
            filtersButtonClear.visibility = View.VISIBLE
            val filters = resources.getStringArray(R.array.filters)
            filterStatus.text = Filtering().setFilterStatus(filterPref,filters)
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
