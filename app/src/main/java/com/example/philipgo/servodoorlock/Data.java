package com.example.philipgo.servodoorlock;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.google.gson.JsonObject;

import java.text.ChoiceFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.ActionBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Data extends LockCodesActivity {

    DatabaseHelper mDatabaseHelper;
    EditText editName, editNumber;
    Button save, buttonGenerate;
    Random myRandom;
    int lockCode, number;
    String Name;
    String Number;
    String mystring;
    long time;

    @BindView(R.id.doubleText)
    TextView doubleText;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder=null;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat simpleDateOnlyFormat;
    SimpleDateFormat simpleDateLocaleFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lock_codes_screen);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());

        this.simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        this.simpleDateOnlyFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());

        this.simpleDateLocaleFormat = new SimpleDateFormat("EEE d MMM", Locale.US);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Lock codes");

        editName = (EditText) findViewById(R.id.editName);
        editNumber = (EditText) findViewById(R.id.editNumber);
        save = (Button) findViewById(R.id.save);
        mDatabaseHelper = new DatabaseHelper(this);

        buttonGenerate = (Button) findViewById(R.id.auto_generate);
        final EditText textGenerateNumber = (EditText) findViewById(R.id.editNumber);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {                                                           // TODO Auto-generation method

                myRandom = new Random();
                lockCode = myRandom.nextInt(99999 - 10000) + 10000;
                mystring = String.valueOf(lockCode);
                textGenerateNumber.setText(mystring);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("arrayListCodes");

                Name = editName.getText().toString();
                Number = editNumber.getText().toString();

                if (Number.equalsIgnoreCase("") || Number.length() != 5) {

                    editNumber.setHint("Please enter a five digit Number");
                    editNumber.setError("Please enter a five digit Number");

                } else {
                    if (arrayListCodes.contains(Name)) {
                        editName.setHint("This name already exists please enter a different name");
                        editName.setError("This name already exists please enter a different name");
                    } else {
                        if (Name.equalsIgnoreCase("")) {

                            editName.setHint("Please enter a name");
                            editName.setError("Please enter a name");

                        } else {
                            number = Integer.parseInt(Number);
                            addData(Name, number);
                            new RequestAsync().execute();

                            editName.setText("");
                            editNumber.setText("");

                            Intent intent2 = new Intent(Data.this, LockCodesActivity.class);
                            startActivity(intent2);

                        }
                    }
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (doubleBuilder != null)
            doubleBuilder.dismiss();
    }
    @OnClick(R.id.doubleLayout)
    public void doubleClicked() {

        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(now); // Set min now
        calendarMax.setTime(new Date(now.getTime() + TimeUnit.DAYS.toMillis(150))); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))
                .setTimeZone(TimeZone.getDefault())
                //.bottomSheet()
                //.curved()

                .backgroundColor(Color.BLACK)
               .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .secondDateAfterFirst(true)

                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))

                .title("Double")

                .tab0Text("Depart")
                .tab1Text("Return")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                        }
                        doubleText.setText(stringBuilder.toString());  
                    }

                    
                });
        System.out.println("---->"+doubleBuilder .setTimeZone(TimeZone.getDefault()));
       doubleBuilder.display();
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                //return RequestHandler.sendGet("https://prodevsblog.com/android_get.php");

                // POST Request
                JsonObject postDataParams = new JsonObject();

                postDataParams.addProperty("Lock", number);

                return RequestHandler.sendPost("https://arduino-1a350.firebaseio.com/smart.json",postDataParams);
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }


    }
    public void addData(String name, int Lockcode) {
        boolean insertData = mDatabaseHelper.addData(name, Lockcode);

        if (insertData) {
            toastMessage("Successfully added!!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
