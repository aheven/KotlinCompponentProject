package heven.holt.lib_common.model.vo

data class PagerHelperModel<T>(
        var pageSize: Int,//页大小
        var pageNo: Int,//页码
        var pageCount: Int,//总页数
        var count:Int,//总条数
        var data: MutableList<T>?
) : SingleBaseModel()