package com.xplo.data.repo

import com.xplo.data.network.api.ContentApi
import javax.inject.Inject

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/23/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class ContentRepoImpl @Inject constructor(
    private val api: ContentApi
) : ContentRepo {


}