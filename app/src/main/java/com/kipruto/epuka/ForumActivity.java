package com.kipruto.epuka;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kipruto.epuka.Adapter.MesAdapter;
import com.kipruto.epuka.Model.FriendlyMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private MesAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEvenListener;

    TextView author,popupclose,popuptxtprivte,popupviewprof;
    TextView messageview;
    ListView mlist;

    Button closePopupBtn;
    PopupWindow popupWindow;
    RelativeLayout rtive;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

/**
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        **/

        mUsername = ANONYMOUS;

        rtive = findViewById(R.id.reative1);
        popupclose = findViewById(R.id.popupcancel);
        popuptxtprivte = findViewById(R.id.popuptxtprivately);
        popupviewprof = findViewById(R.id.popupviewprofile);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("forumchats");



        // Initialize references to views
        mProgressBar =  findViewById(R.id.progressBar);
        mMessageListView = findViewById(R.id.messageListView);
        mPhotoPickerButton =  findViewById(R.id.photoPickerButton);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton =  findViewById(R.id.sendButton);

        ImageView photoImageView =  findViewById(R.id.photoImageView);
        messageview = findViewById(R.id.messageTextView);
        author =  findViewById(R.id.nameTextView);

        // Initialize message ListView and its adapter
        List<FriendlyMessage> fmessage = new ArrayList<>();
        mMessageAdapter = new MesAdapter(this,R.layout.item_message,fmessage);
        mMessageListView.setAdapter(mMessageAdapter);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String user_id = auth.getCurrentUser().getUid();

                DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("forumchats");
                final String message = mMessageEditText.getText().toString();
                Map<String, String> friendlyMessage = new HashMap();

                //friendlyMessage.put("id", user_id);

                friendlyMessage.put("message",message);
                friendlyMessage.put("name",mUsername);
                current_user.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });

        mChildEvenListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                mMessageAdapter.add(friendlyMessage);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        mDatabaseReference.addChildEventListener(mChildEvenListener);


        mMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater layoutInflater = (LayoutInflater) ForumActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.activity_popup,null);


                popupclose = customView.findViewById(R.id.popupcancel);
                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //display the popup window
                popupWindow.showAtLocation(rtive, Gravity.CENTER, 0, 0);
                mMessageEditText.setEnabled(false);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                //close the popup window on button click
                popupclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        mMessageEditText.setEnabled(true);
                    }
                });
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forummenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fregister:
                Intent fregister = new Intent(ForumActivity.this, RegisterActivity.class);
                startActivity(fregister);
                return true;
            case R.id.flogin:
                Intent flogin = new Intent(ForumActivity.this,LoginActivity.class);
                startActivity(flogin);
                return true;
            case R.id.fexit:
                Toast.makeText(ForumActivity.this,"Kwaheri!",Toast.LENGTH_SHORT).show();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
