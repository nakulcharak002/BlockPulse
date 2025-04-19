package com.example.blockpulse.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blockpulse.R
import com.example.blockpulse.databinding.FragmentDeatilsBinding
import com.example.blockpulse.fragment.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailsFragment : Fragment() {
    lateinit var binding : FragmentDeatilsBinding
    private val item : DetailsFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDeatilsBinding.inflate(layoutInflater)
        val data : CryptoCurrency = item.data!!
        setUpDetails(data)
        loadChart(data)
        setButtonONClick(data)
      handleWatchlistButton(data)
        addToWatchlist(data)

      return binding.root
  }
    var watchlist : ArrayList<String>? = null
    var watchListIsChecked = false
    private fun addToWatchlist(data: CryptoCurrency) {
      readData()
        watchListIsChecked = if(watchlist!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }
        binding.addWatchlistButton.setOnClickListener{
            watchListIsChecked = if(!watchListIsChecked){
                if (!watchlist!!.contains(data.symbol)){
                    watchlist!!.add(data.symbol)
                }
                storeData()
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                true
            }else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                watchlist!!.remove(data.symbol)
                storeData()
                false

            }
        }


    }
    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist" ,  Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchlist)
        editor.putString("watchlist" , json )
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist" , Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist" , ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json , type)

    }

    private fun handleWatchlistButton(data: CryptoCurrency) {

    }

    private fun setButtonONClick(item: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5
        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinute.id -> laodChartData(it , "15" , item , oneDay , oneMonth , oneWeek , fourHour , oneHour)
                oneHour.id -> laodChartData(it , "1H" , item , oneDay , oneMonth , oneWeek , fourHour , fifteenMinute)
                fourHour.id -> laodChartData(it , "4H" , item , oneDay , oneMonth , oneWeek , fifteenMinute , oneHour)
                oneDay.id -> laodChartData(it , "D" , item , fifteenMinute , oneMonth , oneWeek , fourHour , oneHour)
                oneWeek.id -> laodChartData(it , "W" , item , oneDay , oneMonth , fifteenMinute , fourHour , oneHour)
                oneMonth.id -> laodChartData(it , "M" , item , oneDay , fifteenMinute , oneWeek , fourHour , oneHour)
            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)

    }

    private fun laodChartData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneDay , oneMonth , oneWeek , fourHour , oneHour)
        it!!.setBackgroundColor(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun disableButton(
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
       oneDay.background = null
       oneMonth.background = null
       oneWeek.background = null
       fourHour.background = null
       oneHour.background = null

    }

    private fun loadChart(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol
        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+ data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text =
            " ${String.format("$%.4f ",data.quotes[0].price)} "

        if(data.quotes[0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.02f" , data.quotes[0].percentChange24h)} %"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = " ${String.format("%.02f ", data.quotes[0].percentChange24h)} %"


        }
    }


}