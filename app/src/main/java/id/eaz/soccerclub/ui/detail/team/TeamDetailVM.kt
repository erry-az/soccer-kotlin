package id.eaz.soccerclub.ui.detail.team

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.TeamRepo
import id.eaz.soccerclub.vo.TeamVo

class TeamDetailVM(private val teamRepo: TeamRepo) : ViewModel() {
    val teamDetails: LiveData<List<TeamVo>> = teamRepo.teamDetails
    private val reqTeamDetails: MutableLiveData<String> = MutableLiveData()
    val nsReqTeamDetails: LiveData<NetworkState>

    init {
        nsReqTeamDetails = Transformations.switchMap(reqTeamDetails){
            teamRepo.getTeamDetail(it)
        }
    }

    fun reqTeamDetails(teamId: String){
        reqTeamDetails.value = teamId
    }

    fun addToFav(team: TeamVo){
        teamRepo.addToFavs(team)
        reqTeamDetails.value = team.idTeam
    }

    fun removeFav(idTeam: String){
        teamRepo.removeFav(idTeam)
        reqTeamDetails.value = idTeam
    }
}