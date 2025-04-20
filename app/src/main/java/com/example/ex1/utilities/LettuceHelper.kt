package com.example.ex1.utilities

import android.app.Activity
import androidx.appcompat.widget.AppCompatImageView
import com.example.ex1.R

fun buildLettuceMatrix(activity: Activity): Array<Array<AppCompatImageView>> {
    return arrayOf(
        arrayOf(
            activity.findViewById(R.id.main_IMG_lettuce00),
            activity.findViewById(R.id.main_IMG_lettuce01),
            activity.findViewById(R.id.main_IMG_lettuce02)
        ),
        arrayOf(
            activity.findViewById(R.id.main_IMG_lettuce10),
            activity.findViewById(R.id.main_IMG_lettuce11),
            activity.findViewById(R.id.main_IMG_lettuce12)
        ),
        arrayOf(
            activity.findViewById(R.id.main_IMG_lettuce20),
            activity.findViewById(R.id.main_IMG_lettuce21),
            activity.findViewById(R.id.main_IMG_lettuce22)
        ),
        arrayOf(
            activity.findViewById(R.id.main_IMG_lettuce30),
            activity.findViewById(R.id.main_IMG_lettuce31),
            activity.findViewById(R.id.main_IMG_lettuce32)
        )
    )
}

