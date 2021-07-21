package com.chenxyu.retrofit.adapter

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author:        ChenXingYu
 * @CreateDate:    2021/7/21 1:46
 * @Description:
 * @Version:       1.0
 */
@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.callEnqueueFlow(call: Call<R>) {
    call.enqueue(object : Callback<R> {
        override fun onResponse(call: Call<R>, response: Response<R>) {
            processing(response)
        }

        override fun onFailure(call: Call<R>, throwable: Throwable) {
            cancel(CancellationException(throwable.localizedMessage, throwable))
        }
    })
}

@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.callExecuteFlow(call: Call<R>) {
    try {
        processing(call.execute())
    } catch (throwable: Throwable) {
        cancel(CancellationException(throwable.localizedMessage, throwable))
    }
}

@ExperimentalCoroutinesApi
internal fun <R> ProducerScope<R>.processing(response: Response<R>) {
    if (response.isSuccessful) {
        val body = response.body()
        if (body == null || response.code() == 204) {
            cancel(CancellationException("HTTP status code: ${response.code()}"))
        } else {
            trySendBlocking(body)
                .onSuccess {
                    close()
                }
                .onClosed { throwable ->
                    cancel(
                        CancellationException(
                            throwable?.localizedMessage,
                            throwable
                        )
                    )
                }
                .onFailure { throwable ->
                    cancel(
                        CancellationException(
                            throwable?.localizedMessage,
                            throwable
                        )
                    )
                }
        }
    } else {
        val msg = response.errorBody()?.string()
        cancel(
            CancellationException(
                if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                } ?: "unknown error"
            )
        )
    }
}