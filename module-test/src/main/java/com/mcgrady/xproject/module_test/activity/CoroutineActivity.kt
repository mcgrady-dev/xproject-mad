package com.mcgrady.xproject.module_test.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mcgrady.xproject.module_test.databinding.ActivityCoroutineBinding
import kotlinx.coroutines.*
import java.lang.NullPointerException

class CoroutineActivity : AppCompatActivity() {

    init {
        lifecycleScope.launchWhenResumed {
            Log.d("init", "在类初始化位置启动协程")
        }
    }

    lateinit var binding: ActivityCoroutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
//            GlobalScope.launch(Dispatchers.Main) {
//                val runBlockingJob = runBlocking {
//                    Log.d("runBlocking", "start a coroutine")
//                }
//
//                Log.d("runBlockingJob", "$runBlockingJob")

//                val launchJob = launch {
//                    Log.d("launch", "start a coroutine")
//                }
//
//                Log.d("launchJob", "$launchJob")
//
//                val asyncJob = async {
//                    Log.d("async","start a coroutine")
//                    "我是返回值"
//                }
//
//                Log.d("asyncJob", "${asyncJob.await()}")
//                Log.d("asyncJob", "$asyncJob")

//                for (index in 1 until 10) {
//                    launch {
//                        Log.d("launch $index", "start a coroutin")
//                    }
//                }

//                val result = withContext(Dispatchers.IO) {
//                    "请求结果"
//                }
//
//                binding.btnStart.text = result
//            }

            testCoroutineException()

            Log.d(":", "---------------------------------------")
        }
    }

    fun testCoroutineContext() {
        val coroutineContext1 = Job() + CoroutineName("this is a first coroutine context")
        Log.d("coroutine context 1", "$coroutineContext1")
        val coroutineContext2 = coroutineContext1 + Dispatchers.Default + CoroutineName("this is a second coroutine context")
        Log.d("coroutine context 2", "$coroutineContext2")
        val coroutineContext3 = coroutineContext2 + Dispatchers.Main + CoroutineName("this is a third coroutine context")
        Log.d("coroutine context 3", "$coroutineContext3")
    }

    fun testCoroutineStard() {
        val defaultJob = GlobalScope.launch {
            Log.d("default Job", "CoroutineStart.DEFAULT")
        }
        defaultJob.cancel()

        val lazyJob = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Log.d("lazy Job", "CoroutineStart.LAZY")
        }
        lazyJob.cancel()

        val atomicJob = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            Log.d("atomic Job", "CoroutineStart.ATOMIC 挂起前")
            delay(100)
            Log.d("atomic Job", "CoroutineStart.ATOMIC 挂起后")
        }
        atomicJob.cancel()

        val undispatchedJob = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            Log.d("undispatched Job", "CoroutineStart.UNDISPATCHED 挂起前")
            delay(100)
            Log.d("undispatched Job", "CoroutineStart.UNDISPATCHED 挂起后")
        }
        undispatchedJob.cancel()
    }

    fun testUndispatched() {
        GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
                Log.d("${Thread.currentThread().name}线程", " -> 挂起前")
                delay(100)
                Log.d("${Thread.currentThread().name}线程", " -> 挂起后")
            }

            Log.d("${Thread.currentThread().name}线程", " -> join 前")
            job.join()
            Log.d("${Thread.currentThread().name}线程", " -> join 后")
        }
    }

    fun testCoroutineSupervisorScope() {

    }

    fun testCoroutineException() {
        GlobalScope.launch {
            val job = launch {
                Log.d("${Thread.currentThread().name}", " 抛出未捕获异常")
                throw NullPointerException("异常测试")
            }

            job.join()
            Log.d("${Thread.currentThread().name}", " END")
        }
    }

    fun start() {
        val job = MainScope().launch {
            launch {
                throw NullPointerException("空指针")
            }

            val result = withContext(Dispatchers.IO) {
                "网络请求"
            }

            launch {
                //网络请求...
            }

            binding.btnStart.text = result

        }
    }
}