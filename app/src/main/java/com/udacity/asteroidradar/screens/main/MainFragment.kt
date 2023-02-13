package com.udacity.asteroidradar.screens.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.addAsteroidsToDatabase()

        GlobalScope.launch {
            viewModel.getPicOfTheDay()
        }

        setHasOptionsMenu(true)

        recyclerInit()

        adapter.onItemClick={ asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.allAsteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.todayAsteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.weekAsteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

/*        viewModel.showDialog.observe(viewLifecycleOwner, Observer {showDialog ->
            if (showDialog) {
                showDialog()
            }
        })*/

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_all_menu -> viewModel.getAsteroidsOfTheWeek()
            R.id.show_rent_menu -> viewModel.getAsteroidsOfToday()
            R.id.show_buy_menu -> viewModel.getAllAsteroids()
        }
        return true
    }

    private fun recyclerInit(){
        adapter = AsteroidAdapter()
        binding.asteroidRecycler.adapter = adapter
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please turn on internet connection to continue")
        builder.setPositiveButton("Settings") { _, _ ->
            context?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }
}
