package id.eaz.soccerclub.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import id.eaz.soccerclub.api.ECallBack
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.service.LeagueApi
import id.eaz.soccerclub.vo.LeagueVo
import retrofit2.Response

class LeagueRepo(private val leagueApi: LeagueApi) {

    val leagues = MutableLiveData<List<LeagueVo>>()
    val league  = MutableLiveData<LeagueVo>()

    fun getLeagues() : LiveData<NetworkState> {
        val callback = object : ECallBack<LeagueApi.LeagueRes>() {
            override fun success(response: Response<LeagueApi.LeagueRes>,
                                 nsMld: MutableLiveData<NetworkState>) {
                if (response.isSuccessful && response.body()!=null) {
                    val leagueRes = response.body() ?:
                    LeagueApi.LeagueRes(listOf())
                    leagues.value = leagueRes.leagues
                }
            }
        }

        leagueApi.allLeagues().enqueue(callback)

        return callback.networkStateMLD
    }

    fun getLeague(leagueId: String) : LiveData<NetworkState> {

        val callback = object : ECallBack<LeagueApi.LeagueRes>() {
            override fun success(response: Response<LeagueApi.LeagueRes>,
                                 nsMld: MutableLiveData<NetworkState>) {
                if (response.isSuccessful && response.body()!=null) {
                    val leagueRes = response.body() ?:
                    LeagueApi.LeagueRes(listOf())
                    val leagueResList = leagueRes.leagues ?: listOf()
                    if(leagueResList.isNotEmpty()) league.value = leagueResList[0]
                }
            }
        }

        leagueApi.detailLeague(leagueId).enqueue(callback)

        return callback.networkStateMLD
    }
}