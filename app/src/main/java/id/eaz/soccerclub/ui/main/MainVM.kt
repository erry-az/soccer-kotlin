package id.eaz.soccerclub.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.EventRepo
import id.eaz.soccerclub.repo.LeagueRepo
import id.eaz.soccerclub.vo.EventVo
import id.eaz.soccerclub.vo.LeagueVo

class MainVM(leagueRepo: LeagueRepo) : ViewModel() {


    val leagueData: LiveData<LeagueVo> = leagueRepo.league
    val leagueList: LiveData<List<LeagueVo>> = leagueRepo.leagues

    val reqLeagueData: MutableLiveData<String> = MutableLiveData()
    val reqLeagueList: MutableLiveData<Boolean> = MutableLiveData()

    val lvNsLeagueData: LiveData<NetworkState>
    val lvNsLeagueList: LiveData<NetworkState>

    init {
        lvNsLeagueData = Transformations.switchMap(reqLeagueData) {
           leagueRepo.getLeague(it)
        }
        lvNsLeagueList = Transformations.switchMap(reqLeagueList) {
            leagueRepo.getLeagues()
        }
    }
}