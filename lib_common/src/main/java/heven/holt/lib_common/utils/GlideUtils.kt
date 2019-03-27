package heven.holt.lib_common.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.setImageRoundUrl(url: String) {
    val requestOptions = RequestOptions.circleCropTransform()
    Glide.with(this).load(url).apply(requestOptions).into(this)
}