package com.xplo.code.ui_legacy.category_list;


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
public class CategoryListPresenterTest
        //implements CategoryListContract.Presenter
{

    @Mock
    private CategoryListContract.View view;
    private CategoryListContract.Presenter presenter;

    @Mock
    private DataRepo repo;

    @Captor
    private ArgumentCaptor<RspCallback<List<IbdbModel.Category>>> callbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new CategoryListPresenter();
        presenter.attach(view);
        presenter.setRepo(repo);


    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void fetchCategories_Success() {

        presenter.fetchCategories();
        verify(repo).getCategories(callbackArgumentCaptor.capture());

        List<IbdbModel.Category> items = new ArrayList<>();
        callbackArgumentCaptor.getValue().onSuccess(items);
        verify(view).hideLoading();
        verify(view).onGetCategoryData(items);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).onGetCategoryData(argumentCaptor.capture());
        assertEquals(0, argumentCaptor.getValue().size());


    }

    @Test
    public void fetchCategories_Failure() {

        presenter.fetchCategories();
        verify(repo).getCategories(callbackArgumentCaptor.capture());

        Throwable th = new Exception("error");
        callbackArgumentCaptor.getValue().onFailure(th);
        verify(view).hideLoading();
        verify(view).onError(th.getMessage());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(view).onError(argumentCaptor.capture());
        assertEquals("error", argumentCaptor.getValue());

    }


}