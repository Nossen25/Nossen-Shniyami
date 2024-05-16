package com.example.nossenshniyami;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nossenshniyami.BusinessModel.Business;
import com.example.nossenshniyami.FirebaseHelper.FirebaseHelper;
import com.example.nossenshniyami.Home.HomeFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BusinessRecyclerViewAdapter extends RecyclerView.Adapter<BusinessRecyclerViewAdapter.ViewHolder> {

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    private FirebaseHelper firebaseHelper;

    private final List<Business> mValues;
    private OnBusinessItemClickListener listener;
    private HomeFragment fragment;

    public BusinessRecyclerViewAdapter(List<Business> items, HomeFragment fragment) {
        mValues = items;
        this.fragment = fragment;
    }

    public interface OnBusinessItemClickListener {
        void onBusinessItemClicked(Business business);
    }
    public void setOnBusinessItemClickListener(OnBusinessItemClickListener listener) {
        this.listener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Business currentItem = mValues.get(position);
        holder.tvName.setText(currentItem.getName());
        holder.tvPhone.setText(currentItem.getPhoneNumber());
        holder.tvAddress.setText(currentItem.getAddress());
        // Load image using Picasso
        Picasso.get().load(currentItem.getImageURL()).into(holder.img);







        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Context context = view.getContext();
                firebaseHelper = new FirebaseHelper(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(holder.tvName.getText().toString());
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                TextView topicTextView = new TextView(context);
                layout.addView(topicTextView);
                Button button = new Button(context);
                button.setText("To Shop");
                layout.addView(button);
                builder.setView(layout);
                AlertDialog dialog = builder.create();

                // Pass the phone number along with the callback
                String phoneNumber = currentItem.getPhoneNumber(); // Assuming you have a method to get the phone number
                firebaseHelper.GetInfoF(phoneNumber, new FirebaseHelper.InfoCallback() {
                    @Override
                    public void onInfoReceived(String info) {
                        if (info != null) {
                            topicTextView.setText(info);
                        } else {
                            // Handle the case when info retrieval fails
                            Log.e(TAG, "Failed to retrieve info");
                        }
                        dialog.show();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        AppCompatActivity activity = (AppCompatActivity) context;
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//    fragmentTransaction.replace(R.id.fragment_container, ShoppingFragment.newInstance("param1", "param2"));
//    fragmentTransaction.addToBackStack(null); // Add this line to enable back navigation
//    fragmentTransaction.commit();

//

                        fragment.addToShoppingList(currentItem);
                    }
                });
            }
        });
    }




    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Business mItem;
        public ImageView img;
        public TextView tvName, tvPhone, tvAddress;

        public ViewHolder(View rootView) {
            super(rootView);
            img = rootView.findViewById(R.id.Businessimage);
            tvName = rootView.findViewById(R.id.tvName);
            tvPhone = rootView.findViewById(R.id.tvPhone);
            tvAddress = rootView.findViewById(R.id.tvAddress);





        }





    }
//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment); // Use the correct container view ID
//        fragmentTransaction.commit();
//
//    }
}