package com.example.roshan.cinqsnipe;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshan.cinqsnipe.fragment.All;
import com.example.roshan.cinqsnipe.fragment.ForSale;
import com.example.roshan.cinqsnipe.fragment.Forrent;
import com.example.roshan.cinqsnipe.fragment.FragmentPageAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Home extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    private Button update;
    private TextView username,user_number;
    private ImageView imageView;
    private ProgressDialog pDialog;



    private static String url = "http://realthree60.com/dev/apis/AgentProfile/1";

    private View view;
    ViewPager viewPager;
    TabHost tabhost;
    FragmentPagerAdapter ViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);

        username=(TextView)view.findViewById(R.id.txt_user);
        user_number=(TextView)view.findViewById(R.id.txt_number);
        imageView=(ImageView)view.findViewById(R.id.user_iamge);
        initTabHost();
        initViewPager();
        setHasOptionsMenu(true);
        getActivity().setTitle("My Profile");
        update=(Button)view.findViewById(R.id.btn_update);




        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Updated User Detail", Toast.LENGTH_SHORT).show();
            }
        });
        new GetContacts().execute();
        return view;
    }


    private void initTabHost() {
        tabhost = (TabHost) view.findViewById(R.id.tabHost);
        tabhost.setup();
        String[] tabnames = {
                "All", "For Rent", "For Sale"
        };
        for (int i = 0; i < tabnames.length; i++) {
            TabHost.TabSpec tabspec;
            tabspec = tabhost.newTabSpec(tabnames[i]);
            tabspec.setIndicator(tabnames[i]);
            tabspec.setContent(new FakeContent(getActivity()));
            tabhost.addTab(tabspec);

        }
        tabhost.setOnTabChangedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int SelectedItem) {
        tabhost.setCurrentTab(SelectedItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int SelectedItem = tabhost.getCurrentTab();
        viewPager.setCurrentItem(SelectedItem);
        HorizontalScrollView scrollView = (HorizontalScrollView) view.findViewById(R.id.h_scrollview);
        View tabView = tabhost.getCurrentTabView();
        int scrollPos = tabView.getLeft() - (scrollView.getWidth() - tabView.getWidth()) / 2;
        scrollView.smoothScrollTo(scrollPos, 0);
    }


    public class FakeContent implements TabHost.TabContentFactory {
        Context context;

        public FakeContent(Context mcontext) {
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            fakeview.setMinimumHeight(0);
            fakeview.setMinimumWidth(0);
            return fakeview;
        }
    }

    private void updateInstance(){




    }

    private void initViewPager() {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new All());
        fragmentList.add(new Forrent());
        fragmentList.add(new ForSale());
        FragmentPagerAdapter pagerAdapter = new FragmentPageAdapter(getFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }



    private class GetContacts extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(getContext());
            pDialog.setMessage("Please Wait..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    //Getting JSON Object node
                    String StatusCode=jsonObj.getString("StatusCode");
                    String Success=jsonObj.getString("Success");
                    String Message=jsonObj.getString("Message");
                    JSONObject data=jsonObj.getJSONObject("data");


                    // Getting JSON Array node
                    JSONArray user_detail = data.getJSONArray("user-detail");

                    // looping through All Contacts
                    for (int i = 0; i < user_detail.length(); i++) {
                        JSONObject c = user_detail.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        username.setText(name);
                        String email = c.getString("email");
                        String user_image =c.getString("user_image");
                        String phone = c.getString("phone");
                        user_number.setText(phone);
                        String total_sale=c.getString("total_sale");
                        String total_rent=c.getString("total_rent");

                    }
                } catch (final JSONException e) {


                }
            }

            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        }
        return super.onOptionsItemSelected(item);

    }


}
