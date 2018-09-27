package com.example.lzyang.fyptest.ServerModule;


import android.widget.Toast;

import com.example.lzyang.fyptest.RecyclerAdapter;

import java.util.Calendar;
import java.util.Date;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Lz-Yang on 13/11/2017.
 */

public class MJobScheduler extends JobService {
    public static RecyclerAdapter emergencyCards_recyclerAdapter = new RecyclerAdapter();
    public static MQTTConnectionTask MQTTConnectionTask = new MQTTConnectionTask();
    private MJobExecuter mJobExecuter;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        System.out.println("Running onStartJob");
        MQTTConnectionTask.verify_connection(getApplicationContext());

        mJobExecuter = new MJobExecuter(){
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                jobFinished(jobParameters,false);
            }
        };

        Date currentTime = Calendar.getInstance().getTime();
        System.out.println("Running start " + currentTime);
        mJobExecuter.execute(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
