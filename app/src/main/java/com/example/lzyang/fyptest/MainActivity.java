package com.example.lzyang.fyptest;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lzyang.fyptest.Functions.ProcessingEmergencyCard;
import com.example.lzyang.fyptest.ServerModule.MJobScheduler;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;
import static com.example.lzyang.fyptest.ServerModule.MJobScheduler.emergencyCards_recyclerAdapter;

//i have change my new sentence
//i have deleted this

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public RecyclerView emergencyCards_recyclerView;

    private static final int JOB_ID = 100;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_profile);
        setSupportActionBar(toolbar);



//Build Connection----------------------------------------------------------------------------------
        jobScheduler = JobScheduler.getInstance(this);
        jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(getPackageName(), MJobScheduler.class.getName()))
//                .setPeriodic(30000)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .build();

        jobScheduler.schedule(jobInfo);
        System.out.println("Pass");


//--------------------------------------------------------------------------------------------------
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        emergencyCards_recyclerView = (RecyclerView)findViewById(R.id.ess_recycler);
        emergencyCards_recyclerView.setHasFixedSize(true);
        emergencyCards_recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        ProcessingEmergencyCard processingEmergencyCard = new ProcessingEmergencyCard(this);
        processingEmergencyCard.execute();
        if(!emergencyCards_arrayList.get_arrayList().isEmpty()){
            emergencyCards_recyclerAdapter.setArrayList(emergencyCards_arrayList.get_arrayList());
        }
        emergencyCards_recyclerView.setAdapter(emergencyCards_recyclerAdapter);
//        System.out.println("Run emergencyCards_recyclerView finished");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            System.out.println("Information");
            Intent intent_profile = new Intent(this, HelpInfoActivity.class);
            this.startActivity(intent_profile);
            return true;
        }else if(id == android.R.id.home){
            System.out.println("Profile");
            Intent intent_profile = new Intent(this, ProfileActivity.class);
            this.startActivity(intent_profile);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        init_EmergencyCards_Prefs(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        set_EmergencyCards_Prefs(emergencyCards_arrayList.get_arrayList());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void request_help(View view) {
        Intent intent = new Intent(this, RequestActivity.class);
        this.startActivity(intent);
    }
}
