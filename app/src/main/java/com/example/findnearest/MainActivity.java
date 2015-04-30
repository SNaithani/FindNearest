package com.example.findnearest;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends ListActivity implements OnItemClickListener {
	


    String[] values = new String[] { "Gas Station", "Cafe", "Pharmacy"};
    String[] typeValues = new String[] { "gas_station", "cafe", "pharmacy"};
    public static int RESULT_SETTINGS =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        //Shiv - Creating the first screen of the app.
        ItemArrayAdapter adapter = new ItemArrayAdapter(this, values);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, AppPreferences.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case 1:
//                showUserSettings();
//                break;
//
//        }
//
//    }

//    private void showUserSettings() {
//        SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(this);
//
//
//    }



    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

        System.out.println("position:" +position);

        String selectedValue = typeValues[position];
        System.out.println("selectedValue:" +selectedValue);

        Intent myIntent = new Intent(MainActivity.this, GooglePlaces.class);
        myIntent.putExtra("category", selectedValue);
        startActivity(myIntent);
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
