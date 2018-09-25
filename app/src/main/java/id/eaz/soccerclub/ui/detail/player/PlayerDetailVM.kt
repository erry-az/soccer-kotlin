package id.eaz.soccerclub.ui.detail.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.TeamRepo
import id.eaz.soccerclub.vo.PlayerVo

class PlayerDetailVM(teamRepo: TeamRepo) : ViewModel() {

    val playerDetails: LiveData<List<PlayerVo>> = teamRepo.playerDetails
    private val reqPlayerDetails: MutableLiveData<String> = MutableLiveData()
    val nsPlayerDetails: LiveData<NetworkState>

    init {
        nsPlayerDetails = Transformations.switchMap(reqPlayerDetails){
            teamRepo.getPlayerDetails(it)
        }
    }

    fun getPlayerDetails(playerId: String){
        reqPlayerDetails.value = playerId
    }
}