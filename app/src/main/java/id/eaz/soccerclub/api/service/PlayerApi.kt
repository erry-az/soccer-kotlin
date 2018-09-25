package id.eaz.soccerclub.api.service

import id.eaz.soccerclub.vo.PlayerVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerApi {
    @GET("lookup_all_players.php")
    fun getPlayerByTeam(@Query("id")  teamId: String) : Call<PlayerRes>

    @GET("lookupplayer.php")
    fun getPlayerDetails(@Query("id")  playerId: String) : Call<PlayerRes>

    data class PlayerRes(val player: List<PlayerVo>? = null, val players: List<PlayerVo>? = null)
}