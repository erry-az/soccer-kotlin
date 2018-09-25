package id.eaz.soccerclub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_AWAY_BADGE
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_AWAY_SCORE
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_AWAY_TEAM
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_DATE
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_HOME_BADGE
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_HOME_SCORE
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_HOME_TEAM
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_ID
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.FAV_EV_TIME
import id.eaz.soccerclub.db.dao.EventFavDAO.Companion.TBL_FAV_EVENT
import id.eaz.soccerclub.db.dao.TeamFavDAO.Companion.FAV_TEAM_BADGE
import id.eaz.soccerclub.db.dao.TeamFavDAO.Companion.FAV_TEAM_ID
import id.eaz.soccerclub.db.dao.TeamFavDAO.Companion.FAV_TEAM_NAME
import id.eaz.soccerclub.db.dao.TeamFavDAO.Companion.TBL_FAV_TEAM
import org.jetbrains.anko.db.*

class DbConn(mContext: Context) :
        ManagedSQLiteOpenHelper(mContext, "FavoriteEvent.db", null, 3) {

    companion object {
        const val ID = "_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        create(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(TBL_FAV_EVENT, true)
        db.dropTable(TBL_FAV_TEAM, true)
        create(db)
    }

    private fun create(db: SQLiteDatabase){
        db.createTable(TBL_FAV_EVENT, true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FAV_EV_ID to TEXT + UNIQUE,
                FAV_EV_TIME to TEXT,
                FAV_EV_DATE to TEXT,
                FAV_EV_HOME_TEAM to TEXT,
                FAV_EV_AWAY_TEAM to TEXT,
                FAV_EV_HOME_BADGE to TEXT,
                FAV_EV_AWAY_BADGE to TEXT,
                FAV_EV_HOME_SCORE to TEXT,
                FAV_EV_AWAY_SCORE to TEXT
        )

        db.createTable(TBL_FAV_TEAM, true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FAV_TEAM_ID to TEXT + UNIQUE,
                FAV_TEAM_NAME to TEXT,
                FAV_TEAM_BADGE to TEXT)
    }
}