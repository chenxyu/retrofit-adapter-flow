package com.chenxyu.example

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.chenxyu.retrofit.adapter.FlowCallAdapterFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    private val githubService: GithubService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
            .create(GithubService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        lifecycleScope.launch(Dispatchers.Main) {
            githubService.search("kotlinx.coroutines")
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    if (e is CancellationException) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }.collect {
                    textView.text = it
                }
        }
    }
}

interface GithubService {
    @GET("search/repositories")
    fun search(@Query("q") query: String): Flow<String>
}