package com.xplo.code.ui.content_list

import com.xplo.code.base.BaseContract
import com.xplo.code.data_module.model.content.ContentItem

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface ContentListContract {

    interface View : BaseContract.View {

        fun navigateToContentDetails(content: ContentItem)

        fun onGetContentList(items: List<ContentItem>?)
        fun onGetContentListFailure(msg: String?)


    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}