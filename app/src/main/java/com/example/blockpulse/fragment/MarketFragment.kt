package com.example.blockpulse.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.blockpulse.adapter.MarketAdapter
import com.example.blockpulse.api.ApiInterface
import com.example.blockpulse.api.ApiUtilities
import com.example.blockpulse.databinding.FragmentMarket2Binding
import com.example.blockpulse.fragment.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class MarketFragment : Fragment(){
    private lateinit var binding: FragmentMarket2Binding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarket2Binding.inflate(layoutInflater)
        list = listOf()
        adapter =  MarketAdapter(requireContext() , list ,"market")
        binding.currencyRecyclerView.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            if(res.body() != null ){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
                  adapter.UpdateData(list)
                    binding.spinKitView.visibility = GONE

                }
            }
        }
        searchCoin()

        return binding.root
    }
    lateinit var searchText : String
    private fun searchCoin() {
       binding.searchEditText.addTextChangedListener(object : TextWatcher {
           override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {

           }

           override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

           }

           override fun afterTextChanged(p0: Editable?) {
            searchText = p0.toString().toLowerCase()
               updateRecyclerView()
           }
       })
    }

    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()
        for (item in list){
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbols = item.symbol.lowercase(Locale.getDefault())
            if(coinName.contains(searchText) || coinSymbols.contains(searchText)){
                data.add(item)
            }
        }
        adapter.UpdateData(data)
    }


}
