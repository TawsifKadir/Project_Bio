package com.xplo.code.ui_legacy.login_otp;

import com.xplo.code.repo.LoginRepo;

import org.junit.After;
import org.junit.Before;
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
public class OtpLoginPresenterTest
        //implements LoginContract.Presenter
{

    @Mock
    private OtpLoginContract.View view;
    private OtpLoginContract.Presenter presenter;

    @Mock
    private LoginRepo repo;

    @Captor
    private ArgumentCaptor<LoginRepo.LoginListener> callbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<LoginRepo.OtpSendListener> otpSendCaptor;
    @Captor
    private ArgumentCaptor<LoginRepo.OtpVerifyListener> otpVerifyCaptor;

    String phone = "+8801835557083";
    String otp = "4444";

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new OtpLoginPresenter();
        presenter.attach(view);
        //presenter.setNetworkCall(networkCall);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

//    @Test
//    public void otpSend() {
//
//        verify(repo).otpSend(eq(phone), otpSendCaptor.capture());
//
//        otpSendCaptor.getValue().onSuccess();
//        //verify(view).onLoginSuccess("dummy-token");
//        presenter.otpVerify("1234", phone, otpVerifyCaptor.capture());
//        verify(view).onLoginSuccess("dummy-token");
//
//        //otpSendCaptor.getValue().onFailure("something wrong");
//        //verify(view).onLoginFailure("something wrong");
//
//    }

//    @Test
//    public void otpVerify() {
//        presenter.otpVerify(eq(otp), eq(phone), otpVerifyCaptor.capture());
//        otpVerifyCaptor.getValue().onSuccess("","dummy-token");
//        //verify(view).onLoginSuccess("dummy-token");
//    }
}