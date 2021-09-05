package ru.losev.developerslife.extensions

import android.view.View

fun Boolean.toViewVisibility(invisibleType: Int = View.GONE) = if (this) {
    View.VISIBLE
} else {
    invisibleType
}