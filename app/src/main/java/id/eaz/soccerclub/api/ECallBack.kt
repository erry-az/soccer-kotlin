package id.eaz.soccerclub.api

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ECallBack<T>(private var customNetworkState: Boolean = false) : Callback<T> {
    val networkStateMLD = MutableLiveData<NetworkState>()

    init {
        networkStateMLD.value = NetworkState.LOADING
    }

    override fun onResponse(call: Call<T>,
                            response: Response<T>?) {

        when {
            response?.body()==null -> throw Exception("null error")
            response.isSuccessful -> {
                if (!customNetworkState) {
                    networkStateMLD.value = (NetworkState.LOADED)
                }

                success(response, networkStateMLD)
            }
            else -> networkStateMLD.value = (NetworkState.error(response))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        t.printStackTrace()
        networkStateMLD.value = (NetworkState.error(t))
    }

    abstract fun success(response: Response<T>,
                         nsMld: MutableLiveData<NetworkState>)
}