package com.example.smartlogbook.ui.main;

import android.content.Context;
import android.database.Cursor;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.smartlogbook.database.DatabaseContract;
import com.example.smartlogbook.database.OpenHelper;
import com.example.smartlogbook.models.RegisterEntryModel;

import java.util.ArrayList;

import java.util.List;

public class RegisterViewModel extends ViewModel {

    private OpenHelper db ;
//    new OpenHelper(MainActivity.this); TODO: context in view model class
    private List<RegisterEntryModel> registerEntry = new ArrayList<>();
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<List<RegisterEntryModel>> mRegisterEntryMutableData = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }


}