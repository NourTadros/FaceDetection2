package com.example.katy.facedetention;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface FragmentChangeListener {
    public void replaceFragment(Fragment fragment, String tag);
    public void replaceFragmentwithArgs(Fragment fragment, String tag, Bundle bundle);
}