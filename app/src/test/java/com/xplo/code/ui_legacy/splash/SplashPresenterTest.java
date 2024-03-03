package com.xplo.code.ui_legacy.splash;

import com.xplo.code.ui.splash.SplashContract;
import com.xplo.code.ui.splash.SplashPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Copyright 2020 (C) xplo
 * <p>
 * <p>
 * Created  : 5/22/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
public class SplashPresenterTest
        //implements SplashContract.Presenter
{

    @Mock
    private SplashContract.View view;
    private SplashContract.Presenter presenter;

//    @Mock
//    private SplashContract.repo repo;

//    @Captor
//    private ArgumentCaptor<SplashContract.SplashListener> callbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new SplashPresenter();
        presenter.attach(view);
        //presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }


    @Test
    public void splashDelay() {

    }
}