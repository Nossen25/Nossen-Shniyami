package com.example.nossenshniyami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nossenshniyami.BusinessModel.Business;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<Business> list;
    private Context context;

    public ShoppingListAdapter(List<Business> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_business, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Business currentItem = list.get(position);
        holder.tvName.setText(currentItem.getName());
        holder.tvPhone.setText(currentItem.getPhoneNumber());
        Picasso.get().load(currentItem.getImageURL()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Business mItem;
        public ImageView img;
        public TextView tvName, tvPhone;

        public ViewHolder(View rootView) {
            super(rootView);
            img = rootView.findViewById(R.id.Businessimage);
            tvName = rootView.findViewById(R.id.tvName);
            tvPhone = rootView.findViewById(R.id.tvPhone);
        }
    }
}
