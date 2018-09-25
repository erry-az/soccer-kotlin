package id.eaz.soccerclub.api

import retrofit2.Response
import java.io.IOException

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        val status: Status,
        val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)

        fun error(msg: String?) = NetworkState(Status.FAILED, msg)

        fun error(t: Throwable) : NetworkState {
            return if (t is IOException)
                NetworkState(Status.FAILED, "Tidak Terkoneksi Dengan Server")
            else
                error(t.message)
        }

        fun error(response: Response<*>): NetworkState {
            val converter = ApiClient().retrofit
                    .responseBodyConverter<String>(String::class.java,
                            arrayOfNulls<Annotation>(0))

            var error = response.code().toString() + " : "
            val rb = response.errorBody()

            if (rb != null) {
                try {
                    error = converter.convert(rb)
                } catch (e: IOException) {
                    error += rb.toString()
                }

            }

            return error(error)
        }
    }
}