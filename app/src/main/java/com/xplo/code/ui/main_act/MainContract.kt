package com.xplo.code.ui.main_act

import com.xplo.code.base.BaseContract

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface MainContract {

    interface View : BaseContract.View {

        /**
         * Call when an item in the navigation menu/option menu is selected.
         *
         * @param itemId The selected item id
         * @return true to display the item as the selected item
         */
        fun selectMenuItem(itemId: Int) : Boolean

        fun onClickSearch()
        fun onRunTuto()




    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}