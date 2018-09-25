package id.eaz.soccerclub.ui.detail.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.EventRepo
import id.eaz.soccerclub.vo.EventVo

class DetailVM(private val eventRepo: EventRepo) : ViewModel() {
    var reqEventDetail: MutableLiveData<String> = MutableLiveData()

    val eventDetail: LiveData<List<EventVo>> = eventRepo.eventDetail

    val lvNsEventDetail: LiveData<NetworkState>

    init {
        lvNsEventDetail = Transformations.switchMap(reqEventDetail) {
            eventRepo.getEventDetail(it)
        }
    }

    fun addToFav(eventVo: EventVo){
        eventRepo.addToFav(eventVo)
        reqEventDetail.value = eventVo.idEvent
    }

    fun removeFromFav(eventId: String){
        eventRepo.removeFromFav(eventId)
        reqEventDetail.value = eventId
    }
}