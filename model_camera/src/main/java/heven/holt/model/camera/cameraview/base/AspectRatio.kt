package heven.holt.model.camera.cameraview.base

import android.os.Parcel
import android.os.Parcelable
import androidx.collection.SparseArrayCompat

data class AspectRatio(val mX: Int, val mY: Int) : Comparable<AspectRatio>, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    fun matches(size: Size): Boolean {
        val gcd = gcd(size.mWidth, size.mHeight)
        val x = size.mWidth / gcd
        val y = size.mHeight / gcd
        return mX == x && mY == y
    }

    fun inverse(): AspectRatio {
        return of(mY, mX)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mX)
        parcel.writeInt(mY)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AspectRatio> {
        private val sCache = SparseArrayCompat<SparseArrayCompat<AspectRatio>>(16)

        override fun createFromParcel(parcel: Parcel): AspectRatio {
            return AspectRatio(parcel)
        }

        override fun newArray(size: Int): Array<AspectRatio?> {
            return arrayOfNulls(size)
        }

        /**
         * Returns an instance of [AspectRatio] specified by `x` and `y` values.
         * The values `x` and `` will be reduced by their greatest common divider.
         *
         * @param x The width
         * @param y The height
         * @return An instance of [AspectRatio]
         */
        fun of(x: Int, y: Int): AspectRatio {
            var tempX = x
            var tempY = y
            val gcd = gcd(tempX, tempY)
            tempX /= gcd
            tempY /= gcd
            var arrayX = sCache.get(tempX)
            return if (arrayX == null) {
                val ratio = AspectRatio(tempX, tempY)
                arrayX = SparseArrayCompat()
                arrayX.put(tempY, ratio)
                sCache.put(tempX, arrayX)
                ratio
            } else {
                var ratio = arrayX.get(tempY)
                if (ratio == null) {
                    ratio = AspectRatio(tempX, tempY)
                    arrayX.put(tempY, ratio)
                }
                ratio
            }
        }

        private fun gcd(a: Int, b: Int): Int {
            var tempA = a
            var tempB = b
            while (tempB != 0) {
                val c = tempB
                tempB = tempA % tempB
                tempA = c
            }
            return tempA
        }

        /**
         * Parse an [AspectRatio] from a [String] formatted like "4:3".
         *
         * @param s The string representation of the aspect ratio
         * @return The aspect ratio
         * @throws IllegalArgumentException when the format is incorrect.
         */
        fun parse(s: String): AspectRatio {
            val position = s.indexOf(':')
            if (position == -1) {
                throw IllegalArgumentException("Malformed aspect ratio: $s")
            }
            try {
                val x = Integer.parseInt(s.substring(0, position))
                val y = Integer.parseInt(s.substring(position + 1))
                return of(x, y)
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Malformed aspect ratio: $s", e)
            }

        }
    }

    override fun compareTo(other: AspectRatio): Int {
        if (equals(other)) {
            return 0
        } else if (toFloat() - other.toFloat() > 0) {
            return 1
        }
        return -1
    }

    private fun toFloat(): Float {
        return mX.toFloat() / mY
    }
}