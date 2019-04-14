package com.bignerdranch.android.testpdfreader.model.tools.marker;

import java.io.Serializable;

public interface MarkerIntentListener extends Serializable {

    void newMarkIntent(Marker marker);

}
