package com.mcgrady.xproject.testing.samples.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.testing.samples.databinding.ActivitySampleServiceBinding

class SampleServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleServiceBinding
    private lateinit var connection: ServiceConnection
    private var service: SampleService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val a: Int = 1
        val b: Int = 2
        a.equals(2)

        binding = ActivitySampleServiceBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(binding.root)

            binding.btnStartService.setOnClickListener {
                Intent(this@SampleServiceActivity, SampleService::class.java).also {
                    startService(it)
                }
            }

            binding.btnStopService.setOnClickListener {
                Intent(this@SampleServiceActivity, SampleService::class.java).also {
                    stopService(it)
                }
            }

            binding.btnBindService.setOnClickListener {
                Intent(this@SampleServiceActivity, SampleService::class.java).also {
                    bindService(it, connection, Context.BIND_AUTO_CREATE)
                }
            }

            binding.btnUnbindService.setOnClickListener {
                unbindService(connection)
            }

            binding.btnRandownNum.setOnClickListener { view ->
                service?.let {
                    Snackbar.make(this@SampleServiceActivity, view, it.randomNumber.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        connection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d("${SampleService.TAG} onServiceConnected: $name $service")
                val binder = service as SampleService.SampleBinder
                this@SampleServiceActivity.service = binder.getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("${SampleService.TAG} onServiceDisconnected: $name")
                this@SampleServiceActivity.service = null
            }
        }
    }
}