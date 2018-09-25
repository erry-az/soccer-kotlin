package id.eaz.soccerclub.ui.detail.team.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.TeamRepo
import id.eaz.soccerclub.vo.PlayerVo

class PlayerVM(teamRepo: TeamRepo) : ViewModel() {

    private val reqPlayers: MutableLiveData<String> = MutableLiveData()
    val nsReqPlayers: LiveData<NetworkState>
    val playerList: LiveData<List<PlayerVo>> = teamRepo.players

    init {
        nsReqPlayers = Transformations.switchMap(reqPlayers){
            teamRepo.getPlayersByTeam(it)
        }
    }

    fun reqPlayers(teamId: String){
        reqPlayers.value = teamId
    }
}