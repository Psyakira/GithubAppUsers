package com.submission.fundamental.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username : String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl : String? = null
):Parcelable

