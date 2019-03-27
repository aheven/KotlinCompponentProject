package heven.holt.model.news.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import heven.holt.lib_common.utils.setImageRoundUrl
import heven.holt.model.news.R
import heven.holt.model.news.mvp.model.vo.RoomQuickVo

class HomeAdapter(layoutResId: Int, data: List<RoomQuickVo>) :
    BaseQuickAdapter<RoomQuickVo, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: RoomQuickVo) {
        helper.setText(R.id.content, item.name)
        val imageView = helper.getView<ImageView>(R.id.icon)
        item.icon?.let { imageView.setImageRoundUrl(it) }
    }
}