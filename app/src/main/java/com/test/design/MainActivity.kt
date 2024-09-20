package com.test.design

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.design.api.MemeApi
import com.test.design.model.Meme
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var urll:String
    private lateinit var titlee:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val back = findViewById<ImageView>(R.id.imageBack)
        val next = findViewById<Button>(R.id.button1)
        val share = findViewById<Button>(R.id.button)

        back.setOnClickListener{
            finish()
        }

        loadMeme()
        next.setOnClickListener{
            loadMeme()
        }
        share.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,urll)
            val chooser=Intent.createChooser(intent,"Share with...")
            startActivity(chooser)
        }



    }

    private fun loadMeme() {
         val retrofit = Retrofit.Builder()
            .baseUrl("https://meme-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val memeApi = retrofit.create(MemeApi::class.java)
        val progress = findViewById<ProgressBar>(R.id.progress)
        progress.visibility = View.VISIBLE
        val image = findViewById<ImageView>(R.id.imageView)
        val titleTextView = findViewById<TextView>(R.id.title)

        // Make the API call using Retrofit
        memeApi.getMeme().enqueue(object : retrofit2.Callback<Meme> {
            override fun onResponse(call: Call<Meme>, response: retrofit2.Response<Meme>) {
                if (response.isSuccessful) {
                    val meme = response.body()
                    if (meme != null) {
                        // Extract URL and title
                        urll = meme.url
                        titlee = meme.title

                        // Update the title TextView
                        titleTextView.text = titlee

                        // Load the image using Glide
                        Glide.with(this@MainActivity).load(urll).listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                progress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                progress.visibility = View.GONE
                                return false
                            }
                        }).into(image)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Response error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Meme>, t: Throwable) {
                progress.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }


    /*private fun loadMeme(){
        val progress = findViewById<ProgressBar>(R.id.progress)
        progress.visibility=View.VISIBLE
        val image = findViewById<ImageView>(R.id.imageView)
        val title = findViewById<TextView>(R.id.title)
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                urll = response.getString("url",)
                titlee = response.getString("title")
                title.text = titlee
                Glide.with(this).load(urll).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility=View.GONE
                        return false

                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility=View.GONE
                        return false
                    }
                }).into(image)
            },
            {
                Toast.makeText(this,"Somthing error",Toast.LENGTH_SHORT).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }*/
}
