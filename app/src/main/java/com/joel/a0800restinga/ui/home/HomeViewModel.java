package com.joel.a0800restinga.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("Esse App foi desenvolvido para auxiliar você, morador de Restinga, com alguns contatos e informações úteis!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}