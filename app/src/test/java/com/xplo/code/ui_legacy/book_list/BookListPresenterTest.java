package com.xplo.code.ui_legacy.book_list;


import com.xplo.code.core.RspCallback;
import com.xplo.code.repo.DataRepo;
import com.xplo.code.network.model.IbdbModel;

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
public class BookListPresenterTest
        //implements BookListContract.Presenter
{

    @Mock
    private BookListContract.View view;
    private BookListContract.Presenter presenter;

    @Mock
    private DataRepo repo;

    @Captor
    private ArgumentCaptor<RspCallback<List<IbdbModel.Book>>> callbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new BookListPresenter();
        presenter.attach(view);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void fetchBooks_Success() {

        presenter.fetchBooks();
        verify(repo).getBooks(callbackArgumentCaptor.capture());

        List<IbdbModel.Book> items = new ArrayList<>();
        callbackArgumentCaptor.getValue().onSuccess(items);
        verify(view).hideLoading();
        verify(view).onGetBookData(items);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).onGetBookData(argumentCaptor.capture());
        assertEquals(0, argumentCaptor.getValue().size());

    }

    @Test
    public void fetchBooks_Failure() {

        presenter.fetchBooks();
        verify(repo).getBooks(callbackArgumentCaptor.capture());

        Throwable th = new Exception("error");
        callbackArgumentCaptor.getValue().onFailure(th);
        verify(view).hideLoading();
        verify(view).onError(th.getMessage());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(view).onError(argumentCaptor.capture());
        assertEquals("error", argumentCaptor.getValue());
    }


}