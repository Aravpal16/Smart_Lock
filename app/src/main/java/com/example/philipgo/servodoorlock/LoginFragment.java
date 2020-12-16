package com.example.philipgo.servodoorlock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    EditText editable_email, editable_password, editable_secret_code;
    Button login;
    String name, email, password, secret_code;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myView =  inflater.inflate(R.layout.fragment_login,
                container, false);

        login = (Button) myView.findViewById(R.id.btn_login);

        editable_email = (EditText) myView.findViewById(R.id.et_email);
        editable_password = (EditText) myView.findViewById(R.id.et_password);
        editable_secret_code = (EditText) myView.findViewById(R.id.et_secret_code_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editable_email.getText().toString();
                password = editable_password.getText().toString();
                secret_code = editable_secret_code.getText().toString();
                if (!email.equalsIgnoreCase("")) {
                    if (!password.equalsIgnoreCase("")) {
                        if (!secret_code.equalsIgnoreCase("")) {
                            residentLogin();

                        } else {
                            editable_secret_code.setError("Secret code cannot be empty!!");
                        }
                    } else {
                        editable_password.setError("Password cannot be empty!!");
                    }
                }
                else
                {
                    editable_email.setError("Email cannot be empty!!");

                }
            }
        });
        return myView;
    }

    private void residentLogin()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        mDatabase = FirebaseDatabase.getInstance().getReference("/Resident");


        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Register userBean = user.getValue(Register.class);

                        if (userBean.password.equals(password)) {

                             secretPass();
                        } else {
                            progressDialog.dismiss();
                            editable_password.setError("Wrong Password!!");
                        }
                    }
                } else {
                    managerLogin();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void managerLogin()
    {

        mDatabase = FirebaseDatabase.getInstance().getReference("/Managers");

        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Register userBean = user.getValue(Register.class);

                        if (userBean.password.equals(password)) {
                            if (userBean.secert_code.equals(secret_code)) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                progressDialog.dismiss();
                                editable_secret_code.setError("Wrong Secert code");
                            }
                        } else {
                            progressDialog.dismiss();
                            editable_password.setError("Wrong Password");
                        }
                    }



                } else {
                    progressDialog.dismiss();

                    Toast.makeText(getActivity(), "No User Found", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void secretPass()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("/Managers");

        mDatabase.orderByChild("secert_code").equalTo(secret_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    progressDialog.dismiss();
                    editable_secret_code.setError("Wrong Secret code!!");
                }
                System.out.println("SCREt-->"+dataSnapshot.exists());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
