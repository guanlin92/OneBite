package Callbacks;

/**
 * Created by Allen Space, Darin Vergara, Lijie on 7/22/2015.
 */
abstract public class GeneralCallback implements Runnable{

    @Override
    public void run() {

    }

    abstract public void runWithResponse(Object object);


}
