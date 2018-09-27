package com.example.lzyang.fyptest.Functions;

import com.example.lzyang.fyptest.Entity.EmergencyCard;

import java.util.ArrayList;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;

/**
 * Created by Lz-Yang on 29/11/2017.
 */

public class EmergencyEventClear {
    private ArrayList<EmergencyCard> arrayList;

    public EmergencyEventClear() {
    }

    public void clear_emergencyCards_recyclerAdapter(String recordID){
        arrayList = emergencyCards_arrayList.get_arrayList();

        for(int position = 0; position < arrayList.size() ; position++){
            if(recordID.equals(arrayList.get(position).getRecord_ID())){
                arrayList.remove(position);
            }
        }
        emergencyCards_arrayList.set_arrayList(arrayList);
    }

}
