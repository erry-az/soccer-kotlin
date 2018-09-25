package id.eaz.soccerclub.api.service

import id.eaz.soccerclub.vo.LeagueVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LeagueApi {
    @GET("all_leagues.php")
    fun allLeagues() : Call<LeagueRes>

    @GET("lookupleague.php")
    fun detailLeague(@Query("id") leagueId: String) : Call<LeagueRes>

    data class LeagueRes(val leagues: List<LeagueVo>? = listOf())
}