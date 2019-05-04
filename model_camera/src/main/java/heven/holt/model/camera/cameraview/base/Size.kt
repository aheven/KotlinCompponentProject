package heven.holt.model.camera.cameraview.base

data class Size(val mWidth: Int, val mHeight: Int) : Comparable<Size> {
    override fun compareTo(other: Size): Int {
        return mWidth * mHeight - other.mWidth * other.mHeight
    }
}