package id.eaz.soccerclub

import id.eaz.soccerclub.api.ApiClient
import id.eaz.soccerclub.api.service.EventApi
import id.eaz.soccerclub.api.service.LeagueApi
import id.eaz.soccerclub.api.service.PlayerApi
import id.eaz.soccerclub.api.service.TeamApi
import id.eaz.soccerclub.db.DbConn
import id.eaz.soccerclub.db.dao.EventFavDAO
import id.eaz.soccerclub.db.dao.TeamFavDAO
import id.eaz.soccerclub.repo.EventRepo
import id.eaz.soccerclub.repo.LeagueRepo
import id.eaz.soccerclub.repo.TeamRepo
import id.eaz.soccerclub.ui.detail.team.TeamDetailVM
import id.eaz.soccerclub.ui.detail.event.DetailVM
import id.eaz.soccerclub.ui.detail.player.PlayerDetailVM
import id.eaz.soccerclub.ui.detail.team.player.PlayerVM
import id.eaz.soccerclub.ui.main.MainVM
import id.eaz.soccerclub.ui.main.event.EventVM
import id.eaz.soccerclub.ui.main.team.TeamVM
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val appModule: Module = applicationContext {
    bean { AppPref(get()) }
    bean { DbConn(get()) }
}

val networkModule: Module = applicationContext {
    bean { ApiClient(get()).retrofit.create(LeagueApi::class.java) }
    bean { ApiClient(get()).retrofit.create(EventApi::class.java) }
    bean { ApiClient(get()).retrofit.create(TeamApi::class.java) }
    bean { ApiClient(get()).retrofit.create(PlayerApi::class.java) }
}

val daoModule: Module = applicationContext {
    bean { EventFavDAO(get()) }
    bean { TeamFavDAO(get()) }
}

val repoModule: Module = applicationContext {
    bean { LeagueRepo(get()) }
    bean { EventRepo(get(), get(), get()) }
    bean { TeamRepo(get(), get(), get()) }
}

val vmModule: Module = applicationContext {
    viewModel { MainVM(get()) }
    viewModel { EventVM(get()) }
    viewModel { DetailVM(get()) }
    viewModel { TeamVM(get()) }
    viewModel { PlayerVM(get()) }
    viewModel { TeamDetailVM(get()) }
    viewModel { PlayerDetailVM(get()) }
}

