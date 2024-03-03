package com.xplo.code.ui_legacy.library;


import com.xplo.code.core.RspCallback;
import com.xplo.code.repo.DataRepo;
import com.xplo.code.ui_legacy.home.SectionBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
public class LibraryPresenterTest
        //implements LibraryContract.Presenter
{

    @Mock
    private LibraryContract.View view;
    private LibraryContract.Presenter presenter;

    @Mock
    private DataRepo repo;

    @Captor
    private ArgumentCaptor<RspCallback<List<SectionBook>>> callbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new LibraryPresenter();
        presenter.attach(view);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void fetchMyLists_Success() {

        presenter.fetchMyLists();
        verify(repo).getMyLists(callbackArgumentCaptor.capture());

        List<SectionBook> items = new ArrayList<>();
        callbackArgumentCaptor.getValue().onSuccess(items);
        verify(view).hideLoading();
        verify(view).onGetMyListData(items);

    }

    @Test
    public void fetchMyLists_Failure() {

        presenter.fetchMyLists();
        verify(repo).getMyLists(callbackArgumentCaptor.capture());

        Throwable th = new Exception("error");
        callbackArgumentCaptor.getValue().onFailure(th);
        verify(view).hideLoading();
        verify(view).onError(th.getMessage());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(view).onError(argumentCaptor.capture());
        assertEquals("error", argumentCaptor.getValue());

    }


}