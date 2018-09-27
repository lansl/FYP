package com.example.lzyang.fyptest.Functions;

import com.example.lzyang.fyptest.Entity.EmergencyCard;

import java.util.ArrayList;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;

/**
 * Created by Lz-Yang on 29/11/2017.
 */

public class EmergencyRescuer {
    private ArrayList<EmergencyCard> arrayList;

    public EmergencyRescuer() {
    }

    public void add_emergencyCards_noOfRescuer(String recordID){
        int NoOfRescuer;
        arrayList = emergencyCards_arrayList.get_arrayList();

        for(int position = 0; position < arrayList.size() ; position++){
            if(recordID.equals(arrayList.get(position).getRecord_ID())){
                NoOfRescuer = arrayList.get(position).getNoOfRescuer();
                NoOfRescuer = NoOfRescuer + 1;
                arrayList.get(position).setNoOfRescuer(NoOfRescuer);
            }
        }
        emergencyCards_arrayList.set_arrayList(arrayList);
    }
}
