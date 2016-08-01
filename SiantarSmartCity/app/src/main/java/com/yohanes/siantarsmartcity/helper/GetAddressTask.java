package com.yohanes.siantarsmartcity.helper;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.yohanes.siantarsmartcity.activity.AddPengaduan;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yohanes_marthin on 05/04/2016.
 */
public class GetAddressTask extends AsyncTask<String, Void, String> {
    private AddPengaduan activity;

    public GetAddressTask(AddPengaduan activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);

            //get current Street name
            String address = addresses.get(0).getAddressLine(0);

            //get current province/City
            String province = addresses.get(0).getAdminArea();

            String kecamatan = addresses.get(0).getLocality();

            String kelurahan = addresses.get(0).getSubLocality();
            //get postal code
            String postalCode = addresses.get(0).getPostalCode();

            //get place Name
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            return address + ", "+ kelurahan+ ", " + kecamatan;

        } catch (IOException ex) {
            ex.printStackTrace();
            return "IOE EXCEPTION";

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return "IllegalArgument Exception";
        }

    }

    /**
     * When the task finishes, onPostExecute() call back data to Activity UI and displays the address.
     * @param address
     */
    @Override
    protected void onPostExecute(String address) {
        // Call back Data and Display the current address in the UI
        activity.callBackDataFromAsyncTask(address);
    }
}
