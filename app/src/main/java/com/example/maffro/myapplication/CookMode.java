package com.example.maffro.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class CookMode extends AppCompatActivity {
    TextView titleText;
    TextView currentPrep;
    Button ForwardControl;
    Button BackControl;
    Button timerButton;
    List<String> prepArray;
    int arraySize;
    Recipe recipe;
    int index = 0;

    private int toCookFor = 0;

    //timer variables
    EditText EditSec;
    EditText EditMin;
    TextView mTimeLabel;
    Runnable mUpdateTimeTask;
    long mins;
    long secs;
    boolean timerOn = false;
    Handler mHandler;
    private long mStartTime;

    //camera variables
    private static final int CONTENT_REQUEST = 1234;
    private File output=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_mode);


        //setup UI
        titleText = (TextView) findViewById(R.id.RecipeTitle);
        currentPrep = (TextView) findViewById(R.id.currentPrep);
        mTimeLabel = (TextView) findViewById(R.id.mTimeLabel);
        ForwardControl = (Button) findViewById(R.id.ForwardcontrolButton);
        BackControl = (Button) findViewById(R.id.BackControlButton);
        timerButton = (Button) findViewById(R.id.timerButton);
        EditMin = (EditText) findViewById(R.id.editmin);
        EditSec = (EditText) findViewById(R.id.editsec);

        EditMin.setText(String.valueOf("0"));
        EditSec.setText(String.valueOf("0"));

        prepArray = null;
        mHandler = new Handler();

        Intent i = getIntent();
        if (i != null) {
            i.getExtras();
            recipe = (Recipe) i.getSerializableExtra("Recipe");
            titleText.setText(recipe.getTitle());
            prepArray = recipe.getPrep();
            arraySize = prepArray.size();
            currentPrep.setText(prepArray.get(index));
        }
        /*
        //setup dialog for handling recipe scaling input
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input number of people to cook for:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toCookFor = Integer.parseInt(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        */
        ForwardControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onForwardControlClick(view);
            }
        });
        BackControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackControlClick(view);
            }
        });

    }

    //timer start
    public void onStartClick(View v) {
        if (v.getId() == R.id.timerButton) {
            if (!timerOn) {                               //checks for timerButton
                timerOn = true;
                timerButton.setText("Stop");

                //getting timer value
                String str = EditSec.getText().toString();
                secs = Long.parseLong(str);
                secs *= 1000;
                mStartTime = secs;
                str = EditMin.getText().toString();
                mins = Long.parseLong(str);
                mins *= 60000;
                mStartTime += mins;

                new CountDownTimer(mStartTime, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (!timerOn) {
                            cancel();
                        }
                        int seconds = (int) (millisUntilFinished / 1000);
                        int minutes = seconds / 60;
                        seconds = seconds % 60;

                        if (seconds < 10) {
                            mTimeLabel.setText("" + minutes + ":0" + seconds);
                        } else if (!timerOn) {
                            mTimeLabel.setText("00:00");
                        } else {
                            mTimeLabel.setText("" + minutes + ":" + seconds);
                        }
                    }

                    @Override
                    public void onFinish() {
                        timerButton.setText("Start");
                        timerOn = false;
                        mTimeLabel.setText("00:00");
                        Toast.makeText(CookMode.this, "TIMER COMPLETE!", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            } else if (timerOn) {
                timerOn = false;
                timerButton.setText("Start");
                mHandler.removeCallbacks(mUpdateTimeTask);
                mStartTime = 0;
            }
        }
    }



    public void onForwardControlClick(View v){
        index++;
        if (index != 0 && index != arraySize -1) {
            ForwardControl.setText("Next");
        } else if (index == arraySize - 1) {
            ForwardControl.setText("Finish");
        }
        if (index != arraySize) {
                currentPrep.setText(prepArray.get(index));
            } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you wish to take a picture to save with the recipe?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //start camera activity
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CONTENT_REQUEST);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(CookMode.this, Menu.class);
                    startActivity(i);
                }
            });
            builder.show();
        }
    }
    public void onBackControlClick(View v){

        if(index != 0){
            index--;
            if (index != 0 && index != arraySize -1)
            {
                ForwardControl.setText("Next");
            } else if (index == arraySize - 1) {
                ForwardControl.setText("Finish");
            }
            currentPrep.setText(prepArray.get(index));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CONTENT_REQUEST){
            if(resultCode == RESULT_OK){
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                try {
                    saveToInternalStorage(rotateImage(photo,90));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(CookMode.this,Menu.class);
                startActivity(i);

            }
        }
    }
    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
    private String saveToInternalStorage(Bitmap bmi) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, recipe.getTitle() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bmi.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }




}
