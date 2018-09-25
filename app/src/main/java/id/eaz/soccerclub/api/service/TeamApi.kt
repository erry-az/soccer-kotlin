package id.eaz.soccerclub.api.service

import id.eaz.soccerclub.vo.TeamVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TeamApi {
    @GET("lookupteam.php")
    fun teamDetail(@Query("id") teamId: String) : Call<TeamRes>

    @GET("search_all_teams.php")
    fun teamsByLeague(@Query("l") leagueName: String) : Call<TeamRes>

    @GET("searchteams.php")
    fun searchTeam(@Query("t") search: String): Call<TeamRes>

    data class TeamRes(val teams: List<TeamVo>? = listOf())
}