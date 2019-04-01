package heven.holt.model.news.mvp.model.vo

import com.chad.library.adapter.base.entity.MultiItemEntity

data class RoomTitleVo(val title:String):MultiItemEntity {
    override fun getItemType(): Int = 1
}