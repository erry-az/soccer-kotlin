package id.eaz.soccerclub

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import org.koin.android.ext.android.startKoin

@Suppress("UNUSED")
class SoccerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO)

        val appPref = AppPref(this)
        val leagueId = appPref.getString(AppPref.Key.STR_LEAGUE_ID)
        if(leagueId == null){
            appPref.put(AppPref.Key.STR_LEAGUE_ID, "4332")
            appPref.put(AppPref.Key.STR_LEAGUE_NAME, "Italian Serie A")
        }

        startKoin(this, listOf(appModule, networkModule, daoModule, repoModule,
                vmModule))
    }
}