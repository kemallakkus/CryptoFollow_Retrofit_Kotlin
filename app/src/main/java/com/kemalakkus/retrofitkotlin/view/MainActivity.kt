package com.kemalakkus.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.retrofitkotlin.R
import com.kemalakkus.retrofitkotlin.adapter.RecyclerViewAdapter
import com.kemalakkus.retrofitkotlin.databinding.ActivityMainBinding
import com.kemalakkus.retrofitkotlin.model.CryptoModel
import com.kemalakkus.retrofitkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() , RecyclerViewAdapter.Listener {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel>? = null
    private var recyclerViewAdapter : RecyclerViewAdapter? = null
    private var job : Job? = null

    //Disposable : Tek kullanımlık gibidir.yapılan calları istekleri, aktivitenin yaşam döngüsünden çıkınca bunları da disposable ettiği bir obje

    private var compositeDisposable : CompositeDisposable? = null

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_main)

        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

        compositeDisposable = CompositeDisposable()
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadData()


    }

    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Retrofitimize rxjava kullanacağımmızı belirttik
            .build().create(CryptoAPI::class.java)

        //Coroutine ile yapıyoruz şimdi

        /*job = CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getData()
            withContext(Dispatchers.Main){
                if (response.isSuccessful){

                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }

                }
            }
        }*/


        compositeDisposable?.add(retrofit.getData() //compositeDisposible diye bir obje kullandık. Farklı farklı birden fazla call'u isteği .add diyerek ekleyebiliyoruz.
                                                    //burda sadece getData() adında 1 adet call vardı biz de onu ekledik
            .subscribeOn(Schedulers.io()) //gelen veriyi arkaplanda dinliyor
            .observeOn(AndroidSchedulers.mainThread())// Main thread de işliyor.
            .subscribe(this::handleResponse)) // handleResponse tarafına aktarıyor




        //val service = retrofit.create(CryptoAPI::class.java)  //retrofit ile API'yi birbirine bağlıyoruz
/*
        val call = service.getData()

        call.enqueue(object : Callback<List<CryptoModel>> {  //hazırlanan isteği yollar asenkron şekilde


            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        //eğer ki boş gelmediyse cevap bu if in içinden devam et demek
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }



                    }
                }
            }



        }) */


    }

    private fun handleResponse(cryptoList: List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)
        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked: ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        job?.cancel()

       compositeDisposable?.clear()
    }
}