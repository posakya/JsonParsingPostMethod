package com.example.roshan.cinqsnipe.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.roshan.cinqsnipe.R;
import com.example.roshan.cinqsnipe.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;


public class ForSale extends Fragment {

    private View myView;
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://www.realthree60.com/dev/apis/AgentPost/Sale";

    ArrayList<HashMap<String, String>> agentpost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.detail_list, container, false);
        agentpost = new ArrayList<>();

        lv = (ListView) myView.findViewById(R.id.detail_list_view);
        new GetContacts().execute();
        return myView;
    }

    private class GetContacts extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(getContext());
            pDialog.setMessage("Please Wait..");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        public HashMap<String, String> getHashMap(String user_id ) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", user_id);
            return map;
        }


        @Override
        protected String doInBackground(String... params) {
            WebService sh=new WebService();
            String jsonstr=sh.performPostCall(url, getHashMap("1"));
            Log.i("JSON", "String = " + jsonstr);

            if (jsonstr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonstr);


                    //Getting JSON Object node
                    String StatusCode=jsonObj.getString("StatusCode");
                    String Success=jsonObj.getString("Success");
                    String Message=jsonObj.getString("Message");

                    JSONArray data = jsonObj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString("id");
                        String type=c.getString("type");
                        String purpose=c.getString("purpose");
                        String featured_img=c.getString("featured_img");
                        String title=c.getString("title");
                        String street_name=c.getString("street_name");
                        String asking_price=c.getString("asking_price");
                        String floor_area=c.getString("floor_area");
                        String floor_area_unit=c.getString("floor_area_unit");


                        HashMap<String, String> map = new HashMap<>();
                        map.put("id",id);
                        map.put("title",title);
                        map.put("street_name",street_name);
                        map.put("type",type);
                        map.put("asking_price",asking_price);
                        map.put("floor_area_unit",floor_area_unit);
                        map.put("floor_area",floor_area);

                        agentpost.add(map);

                    }
                } catch (final JSONException e) {


                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter=new SimpleAdapter(
                    getActivity(),agentpost,R.layout.detail, new String[]{
                    "title", "street_name","type","asking_price","floor_area_unit","floor_area"}, new  int[]{
                    R.id.txt_title,R.id.txt_location,R.id.txt_type,R.id.txt_price,R.id.txt_rate,R.id.txt_area}
            );
            lv.setAdapter(adapter);

        }
    }
}


