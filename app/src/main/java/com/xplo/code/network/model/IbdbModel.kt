package com.xplo.code.network.model

import java.io.Serializable

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/22/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object IbdbModel {

    data class Book(
        var id: Int,
        var title: String,
        var author: Author,
        var details: String,
        var rating: Double,
        var price: Double,
        var type: String,
        var imageUrl: String
    ) : Serializable


    data class Author(
        var id: Int,
        var name: String,
        var details: String,
        var rating: Double,
        var type: String,
        var imageUrl: String
    ) : Serializable

    data class Category(
        var id: Int,
        var title: String
    )


}