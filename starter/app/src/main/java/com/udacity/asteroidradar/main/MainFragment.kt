package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidsAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var asteroidsAdapter: AsteroidsAdapter? = null
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        asteroidsAdapter = AsteroidsAdapter(AsteroidsAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.apply {
            adapter = asteroidsAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        }

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsCompleted()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_saved_menu -> viewModel.setFilterAll()
            R.id.show_next_week_menu -> viewModel.setFilterNextWeek()
            R.id.show_today_menu -> viewModel.setFilterToday()
        }
        return true
    }
}
