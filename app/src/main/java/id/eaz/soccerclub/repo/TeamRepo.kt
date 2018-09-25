package id.eaz.soccerclub.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import id.eaz.soccerclub.api.ECallBack
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.service.PlayerApi
import id.eaz.soccerclub.api.service.TeamApi
import id.eaz.soccerclub.db.dao.TeamFavDAO
import id.eaz.soccerclub.vo.PlayerVo
import id.eaz.soccerclub.vo.TeamVo
import retrofit2.Response

class TeamRepo(private val teamApi: TeamApi, private val playerApi: PlayerApi,
               private val teamFavDAO: TeamFavDAO) {
    val teams = MutableLiveData<List<TeamVo>>()
    val teamDetails = MutableLiveData<List<TeamVo>>()
    val teamFavs = MutableLiveData<List<TeamVo>>()
    val teamFounds = MutableLiveData<List<TeamVo>>()

    val players = MutableLiveData<List<PlayerVo>>()
    val playerDetails = MutableLiveData<List<PlayerVo>>()

    fun getAllTeam (league: String) : LiveData<NetworkState> {
        teams.value = listOf()
        val cb = callback {
            teams.value = it
        }
        teamApi.teamsByLeague(league).enqueue(cb)
        return cb.networkStateMLD
    }

    fun searchTeams (search: String) : LiveData<NetworkState> {
        teamFounds.value = listOf()
        val cb = callback {
            teamFounds.value = it
        }
        teamApi.searchTeam(search).enqueue(cb)
        return cb.networkStateMLD
    }

    fun getTeamDetail (teamId: String) : LiveData<NetworkState> {
        teamDetails.value = listOf()
        val cb = callback { teams ->
            teamDetails.value = teams
            teams?.map {
                it.favourites = teamFavDAO.isFavourite(it.idTeam)
                teamDetails.value = teams
            }
        }
        teamApi.teamDetail(teamId).enqueue(cb)
        return cb.networkStateMLD
    }


    fun getPlayersByTeam(teamId: String) : LiveData<NetworkState> {
        players.value = listOf()
        val cb = playerCb {
            players.value = it
        }
        playerApi.getPlayerByTeam(teamId).enqueue(cb)

        return cb.networkStateMLD
    }

    fun getPlayerDetails(playerId: String) : LiveData<NetworkState> {
        playerDetails.value = listOf()
        val cb = playerCb {
            playerDetails.value = it
        }
        playerApi.getPlayerDetails(playerId).enqueue(cb)
        return cb.networkStateMLD
    }

    fun addToFavs(teamVo: TeamVo){
        teamFavDAO.add(teamVo)
        loadFavs()
    }

    fun removeFav(teamId: String){
        teamFavDAO.remove(teamId)
        loadFavs()
    }

    fun loadFavs() {
        teamFavs.value = teamFavDAO.getAll()
    }

    private fun callback(listener: (List<TeamVo>?) -> Unit) : ECallBack<TeamApi.TeamRes>
            = object : ECallBack<TeamApi.TeamRes>(){
        override fun success(response: Response<TeamApi.TeamRes>,
                             nsMld: MutableLiveData<NetworkState>) {
            val teamRes = response.body() ?: TeamApi.TeamRes()
            listener(teamRes.teams)
        }
    }

    private fun playerCb(listener: (List<PlayerVo>?) -> Unit) : ECallBack<PlayerApi.PlayerRes>
        = object : ECallBack<PlayerApi.PlayerRes>(){
        override fun success(response: Response<PlayerApi.PlayerRes>, nsMld: MutableLiveData<NetworkState>) {
            val playerRes = response.body() ?: PlayerApi.PlayerRes()
            val players = playerRes.players ?: playerRes.player
            listener(players)
        }

    }
}