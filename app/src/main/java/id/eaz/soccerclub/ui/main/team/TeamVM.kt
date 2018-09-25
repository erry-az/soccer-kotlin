package id.eaz.soccerclub.ui.main.team

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.TeamRepo
import id.eaz.soccerclub.vo.TeamVo

class TeamVM(private val teamRepo: TeamRepo) : ViewModel() {


    val teamList: LiveData<List<TeamVo>> = teamRepo.teams
    val teamFavs: LiveData<List<TeamVo>> = teamRepo.teamFavs
    val teamFounds: LiveData<List<TeamVo>> = teamRepo.teamFounds

    val reqTeamList: MutableLiveData<String> = MutableLiveData()
    private val reqSearchTeams: MutableLiveData<String> = MutableLiveData()
    val nsReqTeamList: LiveData<NetworkState>
    val nsSearchTeams: LiveData<NetworkState>

    init {
        nsReqTeamList = Transformations.switchMap(reqTeamList) {
            teamRepo.getAllTeam(it)
        }

        nsSearchTeams = Transformations.switchMap(reqSearchTeams) {
            teamRepo.searchTeams(it)
        }
    }

    fun loadFavs(){
        teamRepo.loadFavs()
    }

    fun searchTeams(query: String){
        reqSearchTeams.value = query
    }
}