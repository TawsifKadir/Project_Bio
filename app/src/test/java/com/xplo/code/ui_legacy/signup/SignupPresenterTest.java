package com.xplo.code.ui_legacy.signup;

import com.xplo.code.repo.LoginRepo;
import com.xplo.code.ui_legacy.signup.model.SignupData;

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
public class SignupPresenterTest
        //implements SignupContract.Presenter
{

    @Mock
    private SignupContract.View view;
    private SignupContract.Presenter presenter;

    @Mock
    private LoginRepo repo;

    @Captor
    private ArgumentCaptor<LoginRepo.SignupListener> callbackArgumentCaptor;

    private SignupData signupData = new SignupData("test", "test");

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new SignupPresenter();
        presenter.attach(view);
        //presenter.setNetworkCall(networkCall);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void signup() {

        presenter.signup(signupData);
        verify(repo).signup(eq(signupData), callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().onSignupSuccess();
        verify(view).onSignupSuccess();

        callbackArgumentCaptor.getValue().onSignupFailure("something wrong");
        verify(view).onSignupFailure("something wrong");

    }


}