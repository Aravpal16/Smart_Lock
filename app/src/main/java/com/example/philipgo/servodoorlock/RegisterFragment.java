package com.example.philipgo.servodoorlock;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    RadioButton btnresident,btnmanager;
    EditText editable_secret_code_input;
    EditText editable_name;
    EditText editable_email;
    EditText editable_password;
    EditText editable_confirm_password;
    RadioGroup radioGroup;
     String name=null, emailk=null, password=null, confirm_password=null, secret_code=null;
            private String tm;
    Button register;
    Boolean flag=true;
    DatabaseHelper nDatabaseHelper;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;


    public RegisterFragment() {
        // Required empty public constructor

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_register);
//        btnresident = (Button) findViewById(R.id.radio_resident);
//        btnmanager = (Button) findViewById(R.id.radio_manager);
//
//        btnresident.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                System.out.println("Button clicked");
//            }
//        });
//    }


    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        final View myView = inflater.inflate(R.layout.fragment_register,
                container, false);
        // Inflate the layout for this fragment
//        btnresident = inflater.inflate(R.layout.fragment_register,
//                container, false).findViewById(R.id.radio_resident);

//        btnmanager = inflater.inflate(R.layout.fragment_register,
//                container, false).findViewById(R.id.radio_manager);
        editable_secret_code_input = (EditText) myView.findViewById(R.id.et_secret_code);
        nDatabaseHelper = new DatabaseHelper(getContext());
        register = (Button) myView.findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        editable_name = (EditText) myView.findViewById(R.id.et_name);
        editable_email = (EditText) myView.findViewById(R.id.et_email);
        editable_password = (EditText) myView.findViewById(R.id.et_password);
        editable_confirm_password = (EditText) myView.findViewById(R.id.et_repassword);
        editable_secret_code_input = (EditText) myView.findViewById(R.id.et_secret_code);
//        btnresident.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                System.out.println("Button clicked");
//                secretcodeinput.setVisibility(View.INVISIBLE);
//            }
//        });
//

//        btnmanager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                System.out.println("Button clicked");
//                secretcodeinput.setVisibility(View.VISIBLE);
//            }
//        });

        radioGroup = (RadioGroup) myView.findViewById(R.id.radio_group);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                System.out.println("button clicked");

                switch (checkedId) {
                    case R.id.radio_resident:
                        editable_secret_code_input.setVisibility(View.INVISIBLE);
                        editable_name = (EditText) myView.findViewById(R.id.et_name);
                        editable_email = (EditText) myView.findViewById(R.id.et_email);
                        editable_password = (EditText) myView.findViewById(R.id.et_password);
                        editable_confirm_password = (EditText) myView.findViewById(R.id.et_repassword);



                        flag = true;
                        break;
                    case R.id.radio_manager:
                        editable_secret_code_input.setVisibility(View.VISIBLE);
                        editable_name = (EditText) myView.findViewById(R.id.et_name);
                        editable_email = (EditText) myView.findViewById(R.id.et_email);
                        editable_password = (EditText) myView.findViewById(R.id.et_password);
                        editable_confirm_password = (EditText) myView.findViewById(R.id.et_repassword);
                        editable_secret_code_input = (EditText) myView.findViewById(R.id.et_secret_code);

                        flag = false;
                        break;
                }
            }
        });


            register.setOnClickListener(new View.OnClickListener() {
              @Override
                public void onClick(View v) {

                        name = editable_name.getText().toString();

                        emailk = editable_email.getText().toString();
                        password = editable_password.getText().toString();
                        confirm_password = editable_confirm_password.getText().toString();
                        secret_code = editable_secret_code_input.getText().toString();


                        if (nDatabaseHelper.getSecretCodeDetails(secret_code) == 0) {
                            if (!name.equalsIgnoreCase("")) {
                                if (!emailk.equalsIgnoreCase("")) {
                                    if (!password.equalsIgnoreCase("")) {
                                        if (!confirm_password.equalsIgnoreCase("")) {
                                            if (!secret_code.equalsIgnoreCase("") || flag == true) {
                                                if (password.equals(confirm_password)) {
                                                    if (flag) {
                                                        secret_code =" ";
                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                    private static final String TAG ="" ;
                                                                    StringBuilder  tok = new StringBuilder() ;
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            Log.w(TAG, "getInstanceId failed", task.getException());
                                                                            return;
                                                                        }

                                                                        // Get new Instance ID token
                                                                        tm = task.getResult().getToken();
                                                                        System.out.println("---> 2 "+tm);
                                                                        writeNewResident(name,emailk,password,tm);

                                                                        // Log and toast
                                                                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, tm);
                                                                        Log.d(TAG, msg);

                                                                    }

                                                                });




                                                    } else {
                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                    private static final String TAG ="" ;
                                                                    StringBuilder  tok = new StringBuilder() ;
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            Log.w(TAG, "getInstanceId failed", task.getException());
                                                                            return;
                                                                        }

                                                                        // Get new Instance ID token
                                                                        tm = task.getResult().getToken();
                                                                        System.out.println("---> 2 "+tm);
                                                                        writeNewUser(name,emailk,password,secret_code,tm);

                                                                        // Log and toast
                                                                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, tm);
                                                                        Log.d(TAG, msg);

                                                                    }

                                                                });


                                                    }
                                                } else {
                                                    editable_confirm_password.setError("Confirm password should match the password!!");
                                                }
                                            } else {
                                                editable_secret_code_input.setError("Secret code cannot be empty!!");
                                            }
                                        } else {
                                            editable_confirm_password.setError("Confirm password cannot be empty!!");
                                        }
                                    } else {
                                        editable_password.setError("Password cannot be empty!!");
                                    }
                                } else {
                                    editable_email.setError("Email cannot be empty!!");
                                }
                            } else {
                                editable_name.setError("Name cannot be empty!!");
                            }
                        } else {
                            editable_secret_code_input.setError("Please enter a different Secret code!!");
                        }

                }
            });


        return myView;
    }



    public void add() {

        editable_name.getText().clear();
        editable_email.getText().clear();
        editable_password.getText().clear();
        editable_confirm_password.getText().clear();
        editable_confirm_password.getText().clear();
        editable_secret_code_input.getText().clear();

    }


    @Override
    public void onClick(View v) {
        System.out.println("button clicked");
    }

    private void writeNewUser(String name1, final String email_id, String pass, String code, String tok) {
        add();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        mDatabase = FirebaseDatabase.getInstance().getReference("/Managers");
        final String emailid=email_id;
        final String uname=name1;
        final String upass=pass;
        final String ucode=code;
        final String utok=tok;
        mDatabase.orderByChild("email").equalTo(email_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Register user = new Register(uname,emailid,upass,ucode,utok);
                    mDatabase.push().setValue(user);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "USER ADDED!!", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "USER EXIST!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void writeNewResident(String name1, String email_id,String pass,String tok) {
        add();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Please Wait"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
         mDatabase = FirebaseDatabase.getInstance().getReference("/Resident");
        final String emailid=email_id;
        final String uname=name1;
        final String upass=pass;
        final String utok=tok;

        mDatabase.orderByChild("email").equalTo(email_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Register user1 = new Register(uname,emailid,upass,utok);
                    mDatabase.push().setValue(user1);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "USER ADDED!!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(getActivity(), "USER EXIST!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

  /*  public  String getidentity() {
        String repsonse;



     return repsonse;
    }*/



}
