package heven.holt.model.camera.cameraview.base

import androidx.collection.ArrayMap
import java.util.*

class SizeMap{
    private val mRatios = ArrayMap<AspectRatio, SortedSet<Size>>()

    /**
     * Add a new [Size] to this collection.
     *
     * @param size The size to add.
     * @return `true` if it is added, `false` if it already exists and is not added.
     */
    fun add(size: Size): Boolean {
        for (ratio in mRatios.keys) {
            if (ratio.matches(size)) {
                val sizes = mRatios[ratio]
                return if (sizes!!.contains(size)) {
                    false
                } else {
                    sizes.add(size)
                    true
                }
            }
        }
        // None of the existing ratio matches the provided size; add a new key
        val sizes = TreeSet<Size>()
        sizes.add(size)
        mRatios[AspectRatio.of(size.mWidth, size.mHeight)] = sizes
        return true
    }

    fun sizes(ratio: AspectRatio): SortedSet<Size>? {
        return mRatios[ratio]
    }

    fun ratios(): Set<AspectRatio> {
        return mRatios.keys
    }

    fun clear() {
        mRatios.clear()
    }

    fun remove(ratio: AspectRatio) {
        mRatios.remove(ratio)
    }
}