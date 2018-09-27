package com.example.lzyang.fyptest.Entity;

import com.example.lzyang.fyptest.R;

/**
 * Created by Lz-Yang on 14/11/2017.
 */

public class CommandClass {
    public static String setCommandPayload(int action_ID){
        String command = null;
        switch (action_ID){
            case R.id.action_send:
                command = "003001";
                break;
            case R.id.btn_rescue:
                command = "003002";
                break;
            case R.id.btn_solved:
                command = "003003";
                break;
            default:
                command = null;
                break;
        }
        return command;
    }

    public static String getCommandAction(String command){
        String action = null;
        switch (command){
            case "003001":
                action = "Emergency_Event_Massage";
                break;
            case "003002":
                action = "Rescue_Action";
                break;
            case "003003":
                action = "Solved_Emergency_Event";
                break;
            default:
                action = "Retry_Action";
                break;
        }
        return action;
    }
}
