package heven.holt.model.news

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.entity.MultiItemEntity
import heven.holt.lib_common.base.mvp.MvpBaseFragment
import heven.holt.lib_common.widget.refresh.EasyEvent
import heven.holt.lib_common.widget.refresh.LoadModel
import heven.holt.lib_common.widget.sticky.OnStickyChangeListener
import heven.holt.lib_common.widget.sticky.StickyItemDecoration
import heven.holt.model.news.adapter.HomeAdapter
import heven.holt.model.news.mvp.contract.MainContract
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import heven.holt.model.news.mvp.model.vo.RoomTitleVo
import heven.holt.model.news.mvp.presenter.MainPresenter
import kotlinx.android.synthetic.main.news_fragment_news.*
import kotlinx.android.synthetic.main.news_item_sticky_head.view.*

@Route(path = "/news/fragment")
class NewsFragment : MvpBaseFragment<MainPresenter>(), MainContract.View {
    private lateinit var homeAdapter: HomeAdapter
    private var isRefreshing = false

    override fun initPresenter(): MainPresenter = MainPresenter()

    override fun initFragmentAfterPresenter(savedInstanceState: Bundle?) {
        initEasyRefreshView()
        initRecyclerView()
        initStickyHeadView()
        initData()
    }

    override fun getLayoutResID(): Int = R.layout.news_fragment_news

    private fun initEasyRefreshView() {
        easyRefreshLayout.loadMoreModel = LoadModel.NONE
        easyRefreshLayout.addEasyEvent(object : EasyEvent() {
            override fun startMoveRefreshing() {
                stickyHeadContainer.visibility = View.INVISIBLE
                isRefreshing = true
            }

            override fun endMoveRefreshing() {
                stickyHeadContainer.visibility = View.VISIBLE
                isRefreshing = false
            }

            override fun onRefreshing() {
                mPresenter.requestRecommendList()
            }

            override fun onLoadMore() {
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(context,3)
        val data = mutableListOf<RoomQuickVo>()
        homeAdapter = HomeAdapter(data)
        recyclerView.adapter = homeAdapter
        homeAdapter.setOnItemClickListener { _, _, _ ->
        }
    }

    private fun initStickyHeadView() {
        stickyHeadContainer.setDataCallback {
            val multiItemEntity = homeAdapter.data[it]
            if (multiItemEntity is RoomTitleVo) {
                stickyHeadContainer.content.text = multiItemEntity.title
            }
        }
        val stickyItemDecoration = StickyItemDecoration(stickyHeadContainer, 1)
        stickyItemDecoration.setOnStickyChangeListener(object : OnStickyChangeListener {
            override fun onScrollable(offset: Int) {
                if (isRefreshing) return
                stickyHeadContainer.scrollChild(offset)
                stickyHeadContainer.visibility = View.VISIBLE
            }

            override fun onInVisible() {
                stickyHeadContainer.reset()
                stickyHeadContainer.visibility = View.INVISIBLE
            }
        })
        recyclerView.addItemDecoration(stickyItemDecoration)
    }

    private fun initData() {
        mPresenter.requestRecommendList()
    }

    override fun requestRecommendListSuccess(data: MutableList<RoomQuickVo>?) {
        if (data == null || data.isEmpty()) return

        val result = mutableListOf<MultiItemEntity>()
        result.addAll(data)

        (0..result.size step 4).forEachIndexed { index, i ->
            result.add(i, RoomTitleVo("title$index"))
        }
        homeAdapter.replaceData(result)

        easyRefreshLayout.refreshComplete()
    }
}