package com.example.findnearest;

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GooglePlaces extends ActionBarActivity implements LocationListener , OnItemClickListener{
    ListView listView ;
    ArrayList<GooglePlace> venuesList;

    final String GOOGLE_KEY = "AIzaSyAa1zrBKTwElksUmWGZLVEExhOjxiHfaYo";

    String provider;
    boolean gps_enabled;

    double latitude;
    double longitude;

    public String requestType = "";

    String lat;
    String lon;

    ArrayAdapter<String> myAdapter;

    String[] lString;
    
    float prevResult = 0.0f;
	BigDecimal convert;
    double[] latArray;
    double[] lngArray;
    String[] placeNameArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places);

        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //updateWithNewLocation(location);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);
        //Toast.makeText(getBaseContext(), provider , Toast.LENGTH_SHORT).show();

        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, this);

            try{gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}

            if(gps_enabled){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }

        requestType = (String)getIntent().getExtras().get("category");

        lat = Double.toString(latitude);
        lon = Double.toString(longitude);

        listView = (ListView) findViewById(R.id.listView1);

        new googleplaces().execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        latitude = location.getLatitude();

        // Getting longitude of the current location
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class googleplaces extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + lat + "," + lon + "&rankby=distance&sensor=true&types=" + requestType+ "&key=" + GOOGLE_KEY);

            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right

                // parse Google places search result
                venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);
                
                //Anku : initiating the arrays for lat, lng and place name
                double[] curLatArray = new double[venuesList.size()];
                double[] curLngArray = new double[venuesList.size()];
                String[] currPlaceNameArray = new String[venuesList.size()];


                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    //listTitle.add(i, venuesList.get(i).getName() + "\n(" + venuesList.get(i).getCategory() + ")");
                	
                	//Anku : Latitude and Longitude of each place
                	double curLat = venuesList.get(i).getLat();
                    double curLng = venuesList.get(i).getLng();
                    
                    // Anku : including the values in arrays for later use in google maps.
                    curLatArray[i] = curLat;
                    curLngArray[i] = curLng;    
                    currPlaceNameArray[i] = venuesList.get(i).getName();
                    
                    //Anku : results array to carry the distance between each place and our location
                    float[] results = new float[1];
                    Location.distanceBetween(latitude, longitude,curLat, curLng, results);
                    
                  //Anku : convert the distance into miles 
                    results[0] = (float) (results[0] * 0.000621);
                    convert=round(results[0],2);
                    //System.out.println(convert);
                    
                    //Anku : Include total distance in the list of places
                    listTitle.add(i, venuesList.get(i).getName() + "\nTotal Distance " + convert + " miles " +  "\n(" + venuesList.get(i).getCategory() + ")");
                    
                    latArray = curLatArray.clone() ;
                    lngArray = curLngArray.clone() ;
                    placeNameArray = currPlaceNameArray.clone();
                    
                    // set the results to the list
                    // and show them in the xml
                   myAdapter = new ArrayAdapter<String>(GooglePlaces.this, R.layout.row_layout, R.id.listText, listTitle);
                   listView.setAdapter(myAdapter);
                   // Anku : calling onclick listener on the list of addresses
                   listView.setOnItemClickListener(GooglePlaces.this);
                }

            }
        }
    }
    
  //Anku : function to open maps when clicked on an item on the list
   
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
    	String item = parent.getItemAtPosition(position).toString() + latArray[position] + lngArray[position];
		//Toast.makeText(GooglePlaces.this, "CLICK: " + item, Toast.LENGTH_SHORT).show();

		//Anku : calling the google maps for the place clicked
		String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latArray[position], lngArray[position], placeNameArray[position]);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
		
	}

    
    //Anku : function to round of the total distance miles
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // create an instance of HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // create an instance of HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the response of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(replyString);

        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
        try {

            // make a jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make a jsonObject to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        //Anku : get the details of latitide and longitude from the json file
                        poi.setLat(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lat"));
                        poi.setLng(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lng"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<GooglePlace>();
        }
        return temp;

    }

}
