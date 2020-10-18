package com.kipruto.epuka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,gender,firstname,lastname,residence,age,id,cell_no;
    Button btn_register;
    private FirebaseAuth auth;
    private static final String TAG = "RegisterActivity";
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        cell_no = findViewById(R.id.cellno);
        residence = findViewById(R.id.residence);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        id = findViewById(R.id.natid);
        age = findViewById(R.id.age);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_username = username.getText().toString();
                final String txt_email = email .getText().toString();
                final String txt_password = password.getText().toString();
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("txt_username").equalTo(txt_username);

                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(RegisterActivity.this,"Username",Toast.LENGTH_SHORT).show();
                        }else{
                            auth.createUserWithEmailAndPassword(txt_email, txt_password)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Toast.makeText(RegisterActivity.this, "Registration:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                                                Toast.makeText(RegisterActivity.this,"All fields required",Toast.LENGTH_SHORT).show();
                                            } else if(txt_password.length() < 6){
                                                Toast.makeText(RegisterActivity.this,"password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                                            }else {

                                                String user_id = auth.getCurrentUser().getUid();
                                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                                String name = username.getText().toString();
                                                String lname = lastname.getText().toString();
                                                String mail = email.getText().toString();
                                                String fname = firstname.getText().toString();
                                                String genders = gender.getText().toString();
                                                String resident = residence.getText().toString();
                                                String myAge = age.getText().toString();
                                                String nid = id.getText().toString();
                                                String tellno = cell_no.getText().toString();

                                                HashMap<String,String> newPost = new HashMap();
                                                newPost.put("id", user_id);
                                                newPost.put("username", name);
                                                newPost.put("first_Name",fname);
                                                newPost.put("last_Name",lname);
                                                newPost.put("national_id", nid);
                                                newPost.put("email",mail);
                                                newPost.put("Phone_no",tellno);
                                                newPost.put("age",myAge);
                                                newPost.put("gender",genders);
                                                newPost.put("residence",resident);
                                                newPost.put("imageURL", "default");
                                                newPost.put("status", "offline");
                                                newPost.put("search", name.toLowerCase());

                                                current_user_db.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
    }
}
