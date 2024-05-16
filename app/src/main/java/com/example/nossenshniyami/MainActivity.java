package com.example.nossenshniyami;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nossenshniyami.Account.AccountFragment;
import com.example.nossenshniyami.BusinessModel.Business;
import com.example.nossenshniyami.Home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static boolean creditonoff=false;
    private BottomNavigationView bottom_navigation;

    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private ShoppingFragment shoppingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());
        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.Home);

        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        shoppingFragment = new ShoppingFragment();


        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.Account) {

                replaceFragment(accountFragment);
            } else if (itemId == R.id.Home) {

                replaceFragment(homeFragment);
            } else if (itemId == R.id.Cart) {
                replaceFragment(shoppingFragment);
            }
            return true;
        });

        //יקבל מהקרדיט קרד אינטנט ויעביר לאקונט פרגמט ואז יציג הערה

    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Use the correct container view ID
        fragmentTransaction.commit();

    }

    public void ReplaceNavBar()
    {
        bottom_navigation.setSelectedItemId(R.id.Account);
    }


    public void addToSoppingCart(Business business)
    {
              
        shoppingFragment.addToShoppingCart(business);
    }
}