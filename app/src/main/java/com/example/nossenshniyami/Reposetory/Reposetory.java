package com.example.nossenshniyami.Reposetory;

import android.content.Context;

import com.example.nossenshniyami.Account.Account;
import com.example.nossenshniyami.FirebaseHelper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class Reposetory
{

    FirebaseHelper firebaseHelper;
    Context context;



    public Reposetory(FirebaseHelper firebaseHelper, Context context) {
        this.firebaseHelper = new FirebaseHelper(context);
        this.context = context;

    }
public void GetInfo(FirebaseAuth ma,FirebaseHelper.userFound userFound)
{
    FirebaseHelper fbg = new FirebaseHelper(context);
    fbg.getUserInfo(ma.getCurrentUser().getEmail(), new FirebaseHelper.userFound() {
        @Override
        public void onUserFound(String name, String phone) {
            userFound.onUserFound(name,phone);
        }
    });
}
    //מפעיל פעולות מתוך הפיר בייס + שמןור את המידע פה נשמר בתור ליסט או כל דבר + מפפה לוקח את המידע שצריך
    public void signIN(Account accountMoudle, FirebaseHelper.Completed callback)
    {
        firebaseHelper = new FirebaseHelper(context);
        firebaseHelper.SignIn(accountMoudle.getEmail(),accountMoudle.getPassword(), new FirebaseHelper.Completed() {
            @Override
            public void onComplete(Boolean flag) {
                callback.onComplete(flag);
            }
        });
    }
    public void ReadAllBusData(FirebaseHelper.ListOfBus callback){
        firebaseHelper.ReadAllBusData(callback);
    }
//    public void GetInfoF(FirebaseHelper.ListOfBus callback){
//        firebaseHelper.GetInfoF();
//    }

    public void SigNUp(Account accountMoudle, Map<String,Object> map)
    {
//        firebaseHelper=new FirebaseHelper(context);
     //למה בפייר בייס הלפר יש פעולה אבל פה אי אפשר להשתמש  / לא מופיע
        firebaseHelper.SignUp(context, accountMoudle.getEmail(), accountMoudle.getPassword(),map ); // תהנה! :)
    }


}
