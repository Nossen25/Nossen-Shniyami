package com.example.nossenshniyami.Home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nossenshniyami.Account.AccountFragment;
import com.example.nossenshniyami.Admin.AdminActivity;
import com.example.nossenshniyami.BusinessModel.Business;
import com.example.nossenshniyami.BusinessRecyclerViewAdapter;
import com.example.nossenshniyami.FirebaseHelper.FirebaseHelper;
import com.example.nossenshniyami.HelpUs;
import com.example.nossenshniyami.MainActivity;
import com.example.nossenshniyami.MapsActivity;
import com.example.nossenshniyami.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
private RelativeLayout frameHome;
    private ImageButton btnMapView,btnSerch,btnFilter;
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinkedList<Fragment> list = new LinkedList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }
    private FirebaseAuth mAuth;
    private HomeMoudle homeMoudle;
    private MainActivity mainActivity;
 private ImageButton btnOwner,btnHelp;
    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        homeMoudle = new HomeMoudle(getActivity());
//        frameHome=view.findViewById(R.id.frameHome);
//       // btnFilter=view.findViewById(R.id.btnFilter);
//        btnMapView=view.findViewById(R.id.btnMapView);
//        btnSerch=view.findViewById(R.id.btnSerch);
//        btnOwner=view.findViewById(R.id.btnFilter);
//        recyclerView = view.findViewById(R.id.list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) requireActivity();

        homeMoudle = new HomeMoudle(getActivity());
        frameHome = view.findViewById(R.id.frameHome);
        btnMapView = view.findViewById(R.id.btnMapView);
        btnHelp = view.findViewById(R.id.btnHelp);
        btnOwner = view.findViewById(R.id.btnFilter);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
 //פה חתכתי את הקוד של צאט
        PrintAllBus();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            btnOwner.setVisibility(View.GONE);
            movelogin();
        }
        else
        {
            if (currentUser.getEmail().equals("omertol@gmail.com"))
            {
                btnOwner.setVisibility(View.VISIBLE);
                // תציג פה את הכפתור של הוספה
                // בתור דיפולט תשים את הכפתור בxml, ל gone
            }
            else
            {
                btnOwner.setVisibility(View.GONE);
                //הכפתור לא מוצג
            }
        }
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), HelpUs.class);
                startActivity(intent);
            }
        });

btnOwner.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(requireActivity(), AdminActivity.class);
        startActivity(intent);
    }
});
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

//    public void onBusinessItemClicked(Business business) {
//        // Pass the business details to the ShoppingFragment
//        Bundle bundle = new Bundle();
//        bundle.putString("business_name", business.getName());
//        bundle.putString("business_address", business.getAddress());
//        // Add more details as needed
//        ShoppingFragment fragment = new ShoppingFragment();
//        fragment.setArguments(bundle);
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null)
//                .commit();
//    }
    public void movelogin()
    {


        Dialog builder = new Dialog(getActivity());
        builder.setCancelable(false);
        builder.setContentView(R.layout.move_login);
        WindowManager.LayoutParams lb = new WindowManager.LayoutParams();
        lb.copyFrom(builder.getWindow().getAttributes());
        lb.width = WindowManager.LayoutParams.MATCH_PARENT;
        lb.height = WindowManager.LayoutParams.WRAP_CONTENT;
        builder.getWindow().setAttributes(lb);
        Button done = builder.findViewById(R.id.done);
        TextView txt = builder.findViewById(R.id.text_input_error);
        txt.setText("Plese press the button to continue");
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                builder.dismiss();
                ((MainActivity)getActivity()).ReplaceNavBar();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,new AccountFragment());
                    fragmentTransaction.commit();
            }
        });

        builder.show();
    }

    public void PrintAllBus(){
        homeMoudle.ReadAllBusData(new FirebaseHelper.ListOfBus() {
            @Override
            public void onGotBus(LinkedList<Business> listOfBus) {
                recyclerView.setAdapter(new BusinessRecyclerViewAdapter(listOfBus, HomeFragment.this));
            }
        });
    }

    public void addToShoppingList(Business business) {
        mainActivity.addToSoppingCart(business);
    }
}

