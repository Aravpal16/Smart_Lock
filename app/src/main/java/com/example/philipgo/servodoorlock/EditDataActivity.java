package com.example.philipgo.servodoorlock;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;



public class EditDataActivity extends LockCodesActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave,btnDelete, buttonGenerate;

    EditText editable_name, editable_lockcode;
    DatabaseHelper mDatabaseHelper;
    Random myRandom;
    int lockCode;
    String mystring, LockcodeCheck;

    private String selectedName, Name, Lockcode;
    private int selectedID, selectedLockcode, Number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        btnSave = (Button) findViewById(R.id.save);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_name = (EditText) findViewById(R.id.updateName);
        editable_lockcode = (EditText) findViewById(R.id.updateNumber);
        mDatabaseHelper = new DatabaseHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Lock codes");


        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        //now get the Lock code we passed as an extra
        selectedLockcode = receivedIntent.getIntExtra("lockCode", -1);
        System.out.println("editable lock code  >>>>>>>>>>>:"+selectedLockcode);

        //set the text to show the current selected name
        editable_name.setText(selectedName);
       // editable_name.setText();

        //set the text to show the current selected Lockcode
       editable_lockcode.setText(Integer.toString(selectedLockcode));

        buttonGenerate = (Button)findViewById(R.id.auto_generate);
        final EditText textGenerateNumber = (EditText) findViewById(R.id.updateNumber);

        buttonGenerate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {                                                           // TODO Auto-generation method

                myRandom = new Random();
                lockCode = myRandom.nextInt(99999 - 10000) + 10000;
                mystring = String.valueOf(lockCode);
                textGenerateNumber.setText(mystring);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("arrayListCodes");


                Name = editable_name.getText().toString();
                Lockcode = editable_lockcode.getText().toString();

                LockcodeCheck = String.valueOf(selectedLockcode);
                if(selectedName.equals(Name) && LockcodeCheck.equals(Lockcode)){
                    Intent intent1 = new Intent(EditDataActivity.this, LockCodesActivity.class);
                    startActivity(intent1);
                }
                else{
                    if (Lockcode.equalsIgnoreCase("") || Lockcode.length() != 5) {

                    editable_lockcode.setHint("Please enter a five digit Number");
                    editable_lockcode.setError("Please enter a five digit Number");

                } else {
                    if (!selectedName.equals(Name) && arrayListCodes.contains(Name)) {
                        editable_name.setHint("This name already exists please enter a different name");
                        editable_name.setError("This name already exists please enter a different name");
                    } else {
                        if (Name.equalsIgnoreCase("")) {

                            editable_name.setHint("Please enter a name");
                            editable_name.setError("Please enter a name");

                        } else {
                            Number = Integer.parseInt(Lockcode);
                            mDatabaseHelper.updateName(Name, selectedID, selectedName);
                            mDatabaseHelper.updateLockcode(Number, selectedID, selectedLockcode);
                            editable_name.setText("");
                            editable_lockcode.setText("");
                            Intent intent2 = new Intent(EditDataActivity.this, LockCodesActivity.class);
                            startActivity(intent2);
                        }
                    }
                    }
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName,selectedLockcode);
                editable_name.setText("");
                editable_lockcode.setText("");
                toastMessage("Access Removed!!");
                Intent intent3 = new Intent(EditDataActivity.this, LockCodesActivity.class);
                intent3.putExtra("selectedID",selectedID);
                startActivity(intent3);
            }
        });

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
























