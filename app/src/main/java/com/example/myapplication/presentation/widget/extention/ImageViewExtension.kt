package com.example.myapplication.presentation.widget.extention

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


fun ImageView.loadImage(context: Context, imageView: ImageView, source: Any) {
    Glide.with(context)
        .load(source)
        .into(imageView)
}

fun ImageView.loadCropRoundedSquareImage(
    source: Any,
    rounded: Int
) {
    val density = this.context.resources.displayMetrics.density
    val roundedCorners = RoundedCorners((rounded * density).toInt())

    Glide.with(this.context)
        .load(source)
        .apply(RequestOptions().transform(CenterCrop(), roundedCorners))
        .into(this)
}

fun ImageView.loadCropImage(source: Any) {
    Glide.with(this.context)
        .load(source)
        .centerCrop()
        .into(this)
}

fun ImageView.loadCropPotionImage(source: Any) {
    Glide.with(this.context)
        .load(source)
        .override(300,300)
        .centerCrop()
        .into(this)
}

fun ImageView.loadProfileImage(source: Any) {
    Glide.with(this.context)
        .load(source)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(this)
}