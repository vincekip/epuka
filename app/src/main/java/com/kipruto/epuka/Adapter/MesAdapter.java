package com.kipruto.epuka.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kipruto.epuka.Model.FriendlyMessage;
import com.kipruto.epuka.R;

import java.util.List;

public class MesAdapter extends ArrayAdapter<FriendlyMessage> {

    TextView mt;
    private DatabaseReference reference;
    private FirebaseUser fuser;


    public MesAdapter(Context context, int resource, List<FriendlyMessage> objects){
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        ImageView photoImageView =  convertView.findViewById(R.id.photoImageView);
        final TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        final TextView authorTextView =  convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message = getItem(position);
        boolean isPhoto = message.getPhotoUrl() != null;



        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getMessage());
        }
        authorTextView.setText(message.getName());

        return convertView;
    }

}
