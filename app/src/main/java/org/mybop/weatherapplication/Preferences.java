package org.mybop.weatherapplication;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultInt(0)
    int unitSystem();
}
