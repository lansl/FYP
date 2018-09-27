package com.example.lzyang.fyptest.Map;

import java.util.List;

/**
 * Created by User on 19/11/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> routes);
}
