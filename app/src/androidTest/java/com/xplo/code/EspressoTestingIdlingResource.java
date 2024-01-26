package com.xplo.code;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

/**
 * Copyright 2020 (C) xplo
 * <p>
 * Created  : 5/24/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
public class EspressoTestingIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource mCountingIdlingResource =
            new CountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
