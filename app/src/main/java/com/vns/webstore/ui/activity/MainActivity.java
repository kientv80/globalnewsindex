package com.vns.webstore.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.vns.webstore.middleware.entity.Article;
import com.vns.webstore.middleware.entity.NotifyInfo;
import com.vns.webstore.middleware.entity.WebPage;
import com.vns.webstore.middleware.network.ConnectionManager;
import com.vns.webstore.middleware.network.HttpRequestListener;
import com.vns.webstore.middleware.service.ActivityLogService;
import com.vns.webstore.middleware.service.LocationService;
import com.vns.webstore.middleware.storage.LocalStorageHelper;
import com.vns.webstore.middleware.utils.JSONHelper;
import com.vns.webstore.ui.adapter.PagerAdapter;
import com.vns.webstore.ui.fragment.ArticleFragment;
import com.webstore.webstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAP10572-local on 8/26/2016.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HttpRequestListener{
    ViewPager viewPager = null;
    long pausedTime;
    FloatingActionButton floatingBtn;
    RecyclerView articlesListingView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ConnectionManager.isNetworkAvailable(getApplicationContext());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*
        floatingBtn = (FloatingActionButton) findViewById(R.id.floatingBtn);
        floatingBtn.setVisibility(View.INVISIBLE);

        floatingBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                TextView title = (TextView) findViewById(R.id.title);
                title.setText(R.string.title_notify);
                List<WebPage> homePages = new ArrayList<>();
                WebPage feed = new WebPage("Tin Mới Nhất",null);
                feed.setHtmlContent(buildFeed());
                homePages.add(feed);
                updateViewPager(homePages);
                LocalStorageHelper.saveToFile("notifycount","");
                floatingBtn.setVisibility(View.INVISIBLE);
            }
        });
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        List<WebPage> viewPagers = webpaes.get(NEWS);
        PagerAdapter pagerAdapter = null;
        if(getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("notification")) {
            List<WebPage> notifyPagers = new ArrayList<>();
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(R.string.title_notify);
            WebPage feed = new WebPage("Feed",null);
            feed.setHtmlContent(buildFeed());
            notifyPagers.add(feed);
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(),notifyPagers);
        } else {
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(),viewPagers);
        }
        LocalStorageHelper.saveToFile("notifycount","");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //System.out.println("======================id ="+ProfileService.getProfile(this).getId());
        Intent backgroundService = new Intent(this, WebstoreService.class);
        startService(backgroundService) ;
        */
/*
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        articlesListingView = (RecyclerView)findViewById(R.id.article_listing);
        articlesListingView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        articlesListingView.setLayoutManager(layoutManager);
*/

       // HttpClientHelper.executeHttpGetRequest("http://360hay.com/getupdate?uid=" + "1234", this);

        List<Fragment> fragments = new ArrayList<Fragment>();
        createFragment("http://360hay.com/getupdate?uid=" + "1234","News",fragments);
        createFragment("http://360hay.com/getupdate?uid=" + "1234","Sport",fragments);
        createFragment("http://360hay.com/getupdate?uid=" + "1234","Store",fragments);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void createFragment(String url, String title, List<Fragment> fragments){
        ArticleFragment af = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        af.setArguments(args);
        af.setUrl(url);
        fragments.add(af);
    }
    private void callback(Object data){
        if(data != null) {
            List<NotifyInfo> newNotifyInfoList =  JSONHelper.toObjects(data.toString(),NotifyInfo.class);
            final List<Article> articles = new ArrayList<>();
            for(NotifyInfo n: newNotifyInfoList){
                Article article = new Article();
                article.setTitle(n.getTitle());
                article.setImageUrl(n.getThemeUrl());
                article.setWebsiteAvatar(n.getThemeUrl());
                article.setFromWebSite(n.getFrom()+"\n("+n.getDate()+")");
                articles.add(article);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //ArticleAdapter adapter = new ArticleAdapter(getApplicationContext(), articles);
                        //articlesListingView.setAdapter(adapter);

                        ArticleFragment af = new ArticleFragment();
                        af.setArticleList(articles);
                        List<Fragment> fragments = new ArrayList<Fragment>();
                        fragments.add(af);
                        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments);
                        viewPager = (ViewPager)findViewById(R.id.viewpager);
                        viewPager.setAdapter(pagerAdapter);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                        tabLayout.setupWithViewPager(viewPager);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
            });

        }

    }

    @Override
    protected void onPause() {
        System.out.println("MainActivity  onPause");
        pausedTime = System.currentTimeMillis();
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity  onResume");
        if(pausedTime > 0 && (System.currentTimeMillis()-pausedTime) > 10*60*1000){
            //Reload
            super.onResume();
            updateViewPager(webpaes.get(NEWS));
        }else{
            super.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("MainActivity  onDestroy");
        viewPager = null;
        LocalStorageHelper.saveToFile("lasttimeupdate",System.currentTimeMillis() + "");
        ActivityLogService.getInstance().submitLogToServer();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        TextView title = (TextView) findViewById(R.id.title);
        if (id == R.id.nav_news) {
            title.setText(getResources().getString(R.string.nav_news));
            updateViewPager(webpaes.get(NEWS));
        }  else if (id == R.id.nav_entertainments) {
            title.setText(R.string.nav_entertain);
            updateViewPager(webpaes.get(ENTERTAINMENTS));
        } else if (id == R.id.nav_music_film) {
            title.setText(R.string.nav_music_film);
            updateViewPager(webpaes.get(MUSIC_FILM));
        } else if (id == R.id.nav_knowledge) {
            title.setText(R.string.nav_knowledge);
            updateViewPager(webpaes.get(KNOWLEDGE));
        } else if (id == R.id.nav_webapps) {
            title.setText(R.string.nav_webapps);
            updateViewPager(webpaes.get(UTIL));
        }else if (id == R.id.nav_webstore) {
            title.setText(R.string.nav_webstore);
            updateViewPager(webpaes.get(WEBSTORE));
        }else if (id == R.id.nav_findaround) {
            if(LocationService.isLocationPermissionGrant(getApplicationContext())){
                LocationService.requestLocationPermission(this,getApplicationContext());
            }else{
                Location location = LocationService.getLocation(this,getApplicationContext());
                LocationService.saveLocation(location);
            }
            title.setText(R.string.nav_findaround);
            updateViewPager(webpaes.get(FINDAROUND));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void updateViewPager(List<WebPage> pages){
        PagerAdapter p = ((PagerAdapter)viewPager.getAdapter());
        //p.setWebPages(pages);
        viewPager.setAdapter(p);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationService.MY_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = LocationService.getLocation(this,getApplicationContext());
                    LocationService.saveLocation(location);
                }
            }
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getApplicationContext().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onRecievedData(Object data) {
        callback(data);
    }
}
