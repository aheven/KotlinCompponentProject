package heven.holt.model.news.mvp.model.vo

/**
 * Administrator
 * created at 2018/11/23 13:32
 * TODO:快速加入房间
 */
data class RoomQuickVo(
        var userId: String?,
        var roomId: String?,
        var icon: String?,//用户头像
        var name: String?,//房间昵称
        var online: Int?,//在线人数
        var full: Int?,//是否满员，0-否，1-已满员
        var lockFlag: Int?,//是否上锁，0-没有，1-有锁
        var activeTime: Long?,//激活时间
        var grade: Int?,//房间级别，0-普通房间，1-系统房间
        var url: String?//拉流外链地址
)