package ApiManagers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import Callbacks.GeneralCallback;

import apihelpers.Untappd.UntappdApiHandler;
import apihelpers.YelpApiHandler.YelpApiHandler;
import apihelpers.YelpApiHandler.YelpData;
import apihelpers.networkhelper.SingleRequest;
import apihelpers.Untappd.UntappdData;


/**
 * Created by Allen Space on 7/12/2015.
 */
public class NetworkRequestManager {

    /**Data Fields*/
    private static NetworkRequestManager singleton = null;
    private final static int YELP_CALL = 0;
    private final static int UNTAPPD_CALL =1;

    //Log cat tags....
    private static final String TAG = "UNTAPPD";
    private static final String YELP = "YelpData";

    //------Constructors------
    //------Helper Methods----


    /**
     * @author Allen Space
     * Description: Private constructor for singleton design
     * */
    private NetworkRequestManager()
    {
        //singleton
    }

    /**
     *@author Allen Space
     * Description: Standard singleton getInstance.
     *
     * */
    public static NetworkRequestManager getInstance()
    {
        if(singleton == null)
        {
            singleton =  new NetworkRequestManager();
        }

        return singleton;
    }

    /**
     * @author Allen Space
     * @param pLatitude Double latitude values.
     * @param pLongitude Double longitude values.
     * @param pContext Context java object.
     * Description: Call to populate Untappd Data POJO from the url call.
     * */
    public void populateUntappdFeed(final GeneralCallback generalCallback, double pLatitude, double pLongitude, Context pContext)
    {
        //Build Untappd request query.
        final String url = new UntappdApiHandler().untappdURL(pLatitude, pLongitude);

        //Gather information.
        JsonObjectRequest jsObjectReq = generalJSONRequest(generalCallback, url, UNTAPPD_CALL);


        //Adds the Untappd request on the stack.
        SingleRequest.getInstance(pContext.getApplicationContext()).addToRequestQueue(jsObjectReq);
    }


    /**
     * @author Allen Space
     *
     * */
    public void populateYelpData(final GeneralCallback generalCallback,String pRadius, final Context pContext)
    {
        final double latitude = LocationHandler.getmLatitude();
        final double longitude = LocationHandler.getmLongitude();

        //Authenticate request query.
        final String sendJsonRequestURL = new YelpApiHandler().buildYelpAuthenticationUrl(pRadius, latitude, longitude);

        //Gather information.
       JsonObjectRequest jsObjectReq = generalJSONRequest(generalCallback, sendJsonRequestURL, YELP_CALL);

        // Adds Yelp request on the stack.
        SingleRequest.getInstance(pContext.getApplicationContext()).addToRequestQueue(jsObjectReq);
    }


    /**
     * @author Allen Space
     * @param generalCallback Callback object.
     * @param URL The url string for picture.
     * @param pContext needs the context it came from.
     *
     * Description: Gets a bitmap object of image from the internet.
     * */
    public static void getYelpSingleImage(final GeneralCallback generalCallback, String URL, Context pContext)
    {

        ImageRequest imageRequest =  generalImageRequest(generalCallback, URL);

        SingleRequest.getInstance(pContext.getApplicationContext()).addToRequestQueue(imageRequest);
    }


    /**
     * @author Allen Space
     * @param generalCallback GeneralCallback from CallBack package.
     * @param URL The url string needed for requests.
     * @param pContext The context of the activity  being used.
     * */
    public static void getUntappdSingleImage(final GeneralCallback generalCallback, String URL, Context pContext)
    {


        ImageRequest imageRequest = generalImageRequest(generalCallback, URL);


        SingleRequest.getInstance(pContext.getApplicationContext()).addToRequestQueue(imageRequest);
    }



    /**
     * @author Allen Space
     * @param generalCallback from Image requestee methods.
     * @param URL Simply passing it down the line.
     * Description: This to generalize the image requests call. Divide up teh code easier to read.
     * */
    private static ImageRequest generalImageRequest(final GeneralCallback generalCallback, String URL)
    {
        ImageRequest imageRequest = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {

                generalCallback.runWithResponse(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                Log.e("Image", "Could not get Image!");

                Bitmap bitmap = null;

                generalCallback.runWithResponse(bitmap);
            }
        });

        return imageRequest;
    }

    /**
     * @author Allen Space
     * @param generalCallback Passing down from populate methods.
     * @param URL String to call JSON object.
     * @param FLAG For gathering which info.
     * Descrition: Call for the JSON object and returns data dependent on the Flag bein passed in.
     * */
    private static JsonObjectRequest generalJSONRequest(final GeneralCallback generalCallback, String URL, final int FLAG)
    {
        JsonObjectRequest jsObjectReq = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if(FLAG == YELP_CALL){
                            YelpData businessesResponse = new Gson().fromJson(response.toString(), YelpData.class);

                            generalCallback.runWithResponse(businessesResponse);

                        }else if(FLAG == UNTAPPD_CALL){

                            UntappdData mData = new Gson().fromJson(response.toString(), UntappdData.class);

                            generalCallback.runWithResponse(mData);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NRM", "Failed to get Data.");
                    }
                });

        return jsObjectReq;
    }

}


