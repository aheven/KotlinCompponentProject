package heven.holt.model.news.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import heven.holt.lib_common.utils.setImageRoundUrl
import heven.holt.lib_common.widget.sticky.FullSpanUtil
import heven.holt.model.news.R
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import heven.holt.model.news.mvp.model.vo.RoomTitleVo

class HomeAdapter(data: List<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.news_item_home)
        addItemType(1, R.layout.news_item_sticky_head)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (getItemViewType(helper.layoutPosition)) {
            0 -> {
                item as RoomQuickVo
                helper.setText(R.id.content, item.name)
                val imageView = helper.getView<ImageView>(R.id.icon)
                item.icon?.let { imageView.setImageRoundUrl(it) }
                helper.addOnClickListener(R.id.delete)
            }
            1 -> {
                item as RoomTitleVo
                helper.setText(R.id.content, item.title)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD)
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

    companion object {
        const val TYPE_STICKY_HEAD = 1
    }
}