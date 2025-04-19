package com.example.blockpulse.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.blockpulse.R
import com.example.blockpulse.adapter.MarketAdapter
import com.example.blockpulse.api.ApiInterface
import com.example.blockpulse.api.ApiUtilities
import com.example.blockpulse.databinding.FragmentDeatilsBinding
import com.example.blockpulse.databinding.FragmentWatchListBinding
import com.example.blockpulse.fragment.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchListFragment : Fragment() {
    private lateinit var binding: FragmentWatchListBinding
    private lateinit var watchlist: ArrayList<String>
    private lateinit var watchListItem: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readData()  // Ensure watchlist is loaded after the view is created
        fetchMarketData()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>() {}.type
        watchlist = gson.fromJson(json, type) ?: ArrayList()
    }

    private fun fetchMarketData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    watchListItem = ArrayList()

                    for (watchData in watchlist) {
                        for (item in res.body()!!.data.cryptoCurrencyList) {
                            if (watchData == item.symbol) {
                                watchListItem.add(item)
                            }
                        }
                    }

                    // Update the RecyclerView with the new data
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(), watchListItem, "watchFragment")
                    binding.spinKitView.visibility = GONE
                }
            }
        }
    }
}