package com.xplo.code.ui_legacy.login;

import com.xplo.code.repo.LoginRepo;
import com.xplo.code.ui.login.LoginContract;
import com.xplo.code.ui.login.LoginPresenter;
import com.xplo.code.ui.login.model.LoginCredentials;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

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
public class LoginPresenterTest
        //implements LoginContract.Presenter
{

    @Mock
    private LoginContract.View view;
    private LoginContract.Presenter presenter;

    @Mock
    private LoginRepo repo;

    @Captor
    private ArgumentCaptor<LoginRepo.LoginListener> callbackArgumentCaptor;

    private LoginCredentials loginCredentials = new LoginCredentials("test", "test");

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new LoginPresenter();
        presenter.attach(view);
        //presenter.setNetworkCall(networkCall);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void passwordLogin() {

        presenter.passwordLogin(loginCredentials);
        verify(repo).passwordLogin(eq(loginCredentials), callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().onLoginSuccess("dummy-token");
        verify(view).onLoginSuccess("dummy-token");

        callbackArgumentCaptor.getValue().onLoginFailure("something wrong");
        verify(view).onLoginFailure("something wrong");

    }

    @Test
    public void signup() {
        presenter.signupPage();
        verify(view).navigateToSignup();
    }

    @Test
    public void otpLogin() {
        presenter.otpLoginPage();
        verify(view).navigateToOtpLogin();
    }
}