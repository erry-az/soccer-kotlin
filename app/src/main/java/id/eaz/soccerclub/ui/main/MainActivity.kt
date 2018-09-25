package id.eaz.soccerclub.ui.main

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import id.eaz.soccerclub.AppPref
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.Status
import id.eaz.soccerclub.enableMarque
import id.eaz.soccerclub.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var leagueId: String
    private lateinit var leagueName: String

    private val mainVm by viewModel<MainVM>()
    private val appPref: AppPref by inject()
    private lateinit var skeletonHead : ViewSkeletonScreen

    private var isFilter = false
    private var menuId = 0
    private lateinit var mainVpAdapter: MainVpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        savedInstanceState?.run {
            isFilter = getBoolean(IS_FILTER)
            menuId = getInt(MENU_ID)
        }

        leagueId = appPref.getString(AppPref.Key.STR_LEAGUE_ID) ?: "0"
        leagueName = appPref.getString(AppPref.Key.STR_LEAGUE_NAME) ?: ""

        mainVpAdapter = MainVpAdapter(supportFragmentManager)
        mainVpAdapter.reload(leagueName)

        listen()
        observeVM()
        load()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.run {
            isFilter = getBoolean(IS_FILTER)
            menuId = getInt(MENU_ID)
        }
        if(menuId != 0) loadWithIdMenu(menuId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            maxWidth = Int.MAX_VALUE
            setOnQueryTextListener(this@MainActivity)
        }

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mainVpAdapter.search(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty())
            mainVpAdapter.reload(leagueName)
        return false
    }

    private fun listen(){
        skeletonHead = Skeleton.bind(main_head)
                .load(R.layout.main_head_skeleton).show()

        fab_main.setOnClickListener {
            isFilter = true
            mainVm.reqLeagueList.postValue(true)
        }

        nav_main.setOnNavigationItemSelectedListener {
            loadWithIdMenu(it.itemId)
        }

        main_svp.adapter = mainVpAdapter
    }

    private fun loadWithIdMenu(id: Int) : Boolean{
        hideKeyboard()
        return when (id) {
            R.id.nav_match -> {
                td_app_bar.setExpanded(true)
                fab_main.show()
                main_svp.currentItem = 0
                true
            }
            R.id.nav_team -> {
                td_app_bar.setExpanded(true)
                fab_main.show()
                main_svp.currentItem = 1
                true
            }
            R.id.nav_fav -> {
                td_app_bar.setExpanded(false)
                fab_main.hide()
                main_svp.currentItem = 2
                true
            }
            else -> false
        }
    }


    private fun observeVM(){
        //league data
        mainVm.lvNsLeagueData.observe(this, Observer {
            if(it!=null)
                if (it != NetworkState.LOADING) {
                    skeletonHead.hide()
                    if(it.status == Status.FAILED) {
                        toast(it.msg ?: "Unknown Error")

                        Skeleton.bind(main_head)
                                .load(R.layout.main_head_skeleton).shimmer(false).show()
                    }
                } else
                    skeletonHead.show()
        })

        mainVm.leagueData.observe(this, Observer {
            if(it!=null) {
                skeletonHead.hide()
                tv_main_head_date.text = it.dateFirstEvent
                tv_main_head_title.enableMarque(it.strLeague)
                tv_main_head_desc.text = it.strDescription
                Glide.with(this).load(it.strBadge).apply(
                        RequestOptions().placeholder(R.drawable.badge_placeholder)
                                .error(R.drawable.badge_placeholder)
                                .fallback(R.drawable.badge_placeholder)
                ).into(iv_main_head)
            }
        })

        //league list
        mainVm.lvNsLeagueList.observe(this, Observer {
            if(it!=null) {
                fab_main_loading.visibility =
                        if (it == NetworkState.LOADING) View.VISIBLE else View.INVISIBLE
                if(it.status == Status.FAILED)
                    toast(it.msg ?: "Unknown Error")
            }
        })

        mainVm.leagueList.observe(this, Observer { list ->
            if(list!=null && isFilter) {
                fab_main_loading.visibility = View.INVISIBLE
                val listSelector = mutableListOf<String>()
                val newList = list.filter {
                    !it.strLeague.startsWith("_") && it.strSport.toLowerCase() == "soccer"
                }
                newList.forEach {
                    listSelector.add(it.strLeague)
                }
                selector(getString(R.string.league_select), listSelector) { _, i ->
                    isFilter = false
                    leagueId = newList[i].idLeague
                    leagueName = newList[i].strLeague
                    appPref.put(AppPref.Key.STR_LEAGUE_ID, leagueId)
                    appPref.put(AppPref.Key.STR_LEAGUE_NAME, leagueName)
                    load()
                }
            }
        })
    }

    private fun load() {
        mainVm.reqLeagueData.value = leagueId
        mainVpAdapter.reload(leagueName)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_FILTER, isFilter)
        outState?.putInt(MENU_ID, nav_main.selectedItemId)
    }

    companion object {
        private const val IS_FILTER = "IS_FILTER"
        private const val MENU_ID = "MENU_ID"
    }
}
