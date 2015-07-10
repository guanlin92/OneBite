package Callbacks;

import apiHelpers.YelpApiHandler.YelpData.SearchForBusinessesResponse;

/**
 * Created by darver on 7/7/15.
 */
abstract public class BusinessResponseRunnable implements Runnable{
    @Override
    public void run() {

    }

    abstract public void runWithBusinessResponse(SearchForBusinessesResponse businessResponse);

}