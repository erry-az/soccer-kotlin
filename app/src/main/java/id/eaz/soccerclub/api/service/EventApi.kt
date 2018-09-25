package id.eaz.soccerclub.api.service

import id.eaz.soccerclub.vo.EventVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {
    @GET("eventsnextleague.php")
    fun nextEvents(@Query("id") leagueId: String) : Call<EventRes>

    @GET("eventspastleague.php")
    fun pastEvents(@Query("id") leagueId: String) : Call<EventRes>

    @GET("lookupevent.php")
    fun detailEvent(@Query("id") eventId: String) : Call<EventRes>

    @GET("searchevents.php")
    fun searchEvent(@Query("e") search: String) : Call<EventRes>

    data class EventRes(val events: List<EventVo>? = null,
                        val event: List<EventVo>? = null)
}