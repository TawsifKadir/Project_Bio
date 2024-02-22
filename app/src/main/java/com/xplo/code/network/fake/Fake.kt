package com.xplo.code.network.fake

import com.xplo.code.R
import com.xplo.code.core.Contextor
import com.xplo.code.network.model.IbdbModel
import com.xplo.data.model.user.User
import kotlin.random.Random

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object Fake {


    var context = Contextor.getInstance().context
    var details = Contextor.getInstance().context.resources.getString(R.string.book_details)
    //var type = Contextor.getInstance().context.resources.getString(R.string.history)

    var bookTitles = context.resources.getStringArray(R.array.dummy_titles_book)
    var bookImages = context.resources.getStringArray(R.array.dummy_images_book)
    var authorNames = context.resources.getStringArray(R.array.dummy_names_author)
    var authorImages = context.resources.getStringArray(R.array.dummy_images_author)
    var categories = context.resources.getStringArray(R.array.dummy_categories)
    var myLists = context.resources.getStringArray(R.array.dummy_my_lists)
    var bannerImages = context.resources.getStringArray(R.array.dummy_images_banner)

    var fakeSize = bookTitles.size - 1

    fun getUsers(): List<User> {

        var items = ArrayList<User>()

        repeat(10) {

            var n = Random.nextInt(fakeSize)

//            var item1 = User(1001, "abc07", "aaa", "", authorNames[n], authorImages[n])
//
//            items.add(item1)

        }

        return items
    }


    fun getBooks(): List<IbdbModel.Book> {

        var items = ArrayList<IbdbModel.Book>()

        repeat(10) {

            var n = Random.nextInt(fakeSize)

            var item1 = IbdbModel.Book(
                1001, bookTitles[n], getAuthors()[n], details,
                4.5, 0.0, categories[n], bookImages[n]
            )

            items.add(item1)

        }

        return items
    }


    fun getAuthors(): List<IbdbModel.Author> {

        var items = ArrayList<IbdbModel.Author>()


        repeat(10) {

            var n = Random.nextInt(fakeSize)

            var item1 = IbdbModel.Author(
                1001, authorNames[n], details,
                4.5, categories[n], authorImages[n]
            )
            items.add(item1)
        }


        return items
    }

    fun getCategories(): List<IbdbModel.Category> {

        var items = ArrayList<IbdbModel.Category>()

        repeat(10) {

            var n = Random.nextInt(fakeSize)
            var item1 = IbdbModel.Category(1001, categories[n])

            items.add(item1)

        }
        return items
    }



}