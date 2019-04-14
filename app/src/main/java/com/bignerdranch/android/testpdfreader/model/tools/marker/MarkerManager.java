package com.bignerdranch.android.testpdfreader.model.tools.marker;

public class MarkerManager implements MarkerIntentListener {

    private Marker currentMarker;

    public MarkerIntentListener getIntentListener() {
        return this;
    }

    @Override
    public void newMarkIntent(Marker marker) {
        if (marker == null) return;
        if (currentMarker != null) {
            if (currentMarker != marker) {
                currentMarker.setVisibility(false);
            } else {
                return;
            }
        }
        currentMarker = marker;
        currentMarker.setVisibility(true);
    }

    public void removeCurrentMarker() {
        if (currentMarker != null)
            currentMarker.setVisibility(false);
        currentMarker = null;
    }

    public Marker getCurrentMarker() {
        return currentMarker;
    }
}
