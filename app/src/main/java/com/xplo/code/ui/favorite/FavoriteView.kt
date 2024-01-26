package com.xplo.code.ui.favorite

import com.xplo.code.base.BaseContract
import com.xplo.data.model.content.ContentItem

interface FavoriteView : BaseContract.View {

    fun navigateToContentDetails(content: ContentItem)

    fun onRefreshUI()
    fun onEmptyList()
    fun onNoMoreItems()
    fun isListHasItems(): Boolean

    fun loadData(offset: Int)

    fun onGetFavorites(items: List<ContentItem>?)
    fun onGetFavoritesFailure(msg: String?)

    fun onRemoveFromFavoriteSuccess(position: Int)
    fun onUpdateFavoriteStatusFailure(msg: String?)

}