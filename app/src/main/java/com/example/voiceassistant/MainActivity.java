package com.example.voiceassistant;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.voiceassistant.adapter.CustomAdapter;
import com.example.voiceassistant.model.chatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    ImageView btnMic;

    private TextToSpeech textToSpeechSystem;
    private SpeechRecognizer speechRecognizer;
    RecyclerView chatSection;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatSection = (RecyclerView) findViewById(R.id.chatSection);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.scrollToPosition(0);

        chatSection.setLayoutManager(linearLayoutManager);
        ArrayList<chatMessage> arrayList = new ArrayList<>();


        CustomAdapter customAdapter = new CustomAdapter(arrayList,getApplicationContext());
        chatSection.setAdapter(customAdapter);



        textToSpeechSystem = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechSystem.setLanguage(new Locale("en", "TR"));
                    String textToSay = "Hello gizem, can i help you ?";
                    textToSpeechSystem.speak(textToSay, TextToSpeech.QUEUE_ADD, null);
                    arrayList.add(new chatMessage(textToSay, false));
                    customAdapter.notifyDataSetChanged();



                }
            }
        });


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext(), ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(getApplicationContext(), "Listening...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(int error) {
                Toast.makeText(getApplicationContext(), "Error" + error,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                Api service = retrofit.create(Api.class);
                Call<String> repos = service.getResponse(data.get(0));
                repos.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        try {
                            JSONObject dataJson = new JSONObject(response.body());


                            String chatData = dataJson.getString("response");

                            textToSpeechSystem.speak(chatData, TextToSpeech.QUEUE_ADD, null);

                            arrayList.add(new chatMessage(chatData, false));
                            customAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                    }
                });


                arrayList.add(new chatMessage(data.get(0), true));
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });








        btnMic = (ImageView) findViewById(R.id.btnMic);

        btnMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    checkPermission();
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                    new CountDownTimer(4000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //do nothing, just let it tick
                        }

                        public void onFinish() {
                            speechRecognizer.stopListening();
                        }   }.start();
                }
                return false;
            }
        });



    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Authorized",Toast.LENGTH_SHORT).show();
        }
    }



}