package com.example.philipgo.servodoorlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.database.Cursor;
import android.view.MenuItem;


import java.util.ArrayList;

import androidx.annotation.Nullable;


public class LockCodesActivity extends MainActivity {

    private static final String TAG = "LockCodesActivity";

    private OnBackPressedListener onBackPressedListener;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivityForResult(myIntent, 0);
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LockCodesActivity.this);
            builder.setTitle("  Logout  ").
                    setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getApplicationContext(),
                                    LoginActivity.class);
                            startActivity(i);
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        }
        return false;

    }


    Button add_lock_codes_btn;
    ListView listLockCodes;
    ArrayList<String> arrayListCodes;
    DatabaseHelper mDatabaseHelper;
    //ContactsAdapter contactAdapter;
    //AddLockCodesActivity lockcodes;


    //final int C_View=1,C_Delete=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_codes);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayListCodes = new ArrayList<String>();

        listLockCodes = (ListView) findViewById(R.id.listView);

        add_lock_codes_btn = (Button) findViewById(R.id.add_lock_codes_btn);

            mDatabaseHelper = new DatabaseHelper(this);

        //add button listener
        add_lock_codes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openData();

            }
        });


        populateListView();
    }

    public void openData(){

        Intent intent = new Intent(this, Data.class);
        intent.putExtra("arrayListCodes",arrayListCodes);
        startActivityForResult(intent, 1);
    }

    private void populateListView() {

        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getLockCodesData();
        arrayListCodes = new ArrayList<String>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            //  AddLockCodesActivity addLockCodesActivities = new AddLockCodesActivity(data.getString(1),data.getString(2));
            System.out.println("item list :: "+ data.getString(1));
            arrayListCodes.add(data.getString(1));
            //arrayListCodes.add(addLockCodesActivities);
        }
        System.out.println("array list final : "+ arrayListCodes);
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListCodes);
        listLockCodes.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        listLockCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
//                      Cursor data = mDatabaseHelper.getItemID(lockcode); //get the id associated with that lock code
                int itemID = -1;
                String retrievedName=null;
                int retrievedlockCode =-1;

                while(data.moveToNext()){
                    itemID = data.getInt(0);
                    retrievedName= data.getString(1);
                    retrievedlockCode = data.getInt(2);
                    System.out.println("++++++++++ >>>>>> name : "+ retrievedName);
                    System.out.println("++++++++++ >>>>>> retrievedlockCode : "+ retrievedlockCode);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(LockCodesActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",retrievedName);
                    editScreenIntent.putExtra("lockCode",retrievedlockCode);
                    editScreenIntent.putExtra("arrayListCodes",arrayListCodes);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
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

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else {
            Intent intent1 = new Intent(LockCodesActivity.this, MainActivity.class);
            startActivity(intent1);
        }
    }
}







