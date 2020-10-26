package com.joel.a0800restinga.ui.vcsabia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<String>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}