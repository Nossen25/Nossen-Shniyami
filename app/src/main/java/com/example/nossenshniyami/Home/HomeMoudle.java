package com.example.nossenshniyami.Home;

import android.content.Context;

import com.example.nossenshniyami.FirebaseHelper.FirebaseHelper;
import com.example.nossenshniyami.Reposetory.Reposetory;

public class HomeMoudle {
    private Reposetory rep;
    Context context;

    public HomeMoudle(Context c)
    {
        context = c;
        rep = new Reposetory(new FirebaseHelper(context), context);
    }

    public void ReadAllBusData(FirebaseHelper.ListOfBus callback){
        rep.ReadAllBusData(callback);
    }

}
