package heven.holt.model.news

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import heven.holt.lib_common.base.mvp.MvpBaseFragment
import heven.holt.model.news.adapter.HomeAdapter
import heven.holt.model.news.mvp.contract.MainContract
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import heven.holt.model.news.mvp.presenter.MainPresenter
import kotlinx.android.synthetic.main.news_activity_news.*

@Route(path = "/news/fragment")
class NewsFragment : MvpBaseFragment<MainPresenter>(), MainContract.View {
    private lateinit var homeAdapter: HomeAdapter

    override fun initPresenter(): MainPresenter = MainPresenter()

    override fun initFragmentAfterPresenter(savedInstanceState: Bundle?) {
        initRecyclerView()
        initData()
    }

    override fun getLayoutResID(): Int = R.layout.news_activity_news

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val data = mutableListOf<RoomQuickVo>()
        homeAdapter = HomeAdapter(R.layout.news_item_home, data)
        recyclerView.adapter = homeAdapter
        homeAdapter.setOnItemClickListener { _, _, _ ->
        }
    }

    private fun initData() {
        mPresenter.requestRecommendList()
    }

    override fun requestRecommendListSuccess(data: MutableList<RoomQuickVo>?) {
        if (data == null || data.isEmpty()) return
        homeAdapter.replaceData(data)
    }
}