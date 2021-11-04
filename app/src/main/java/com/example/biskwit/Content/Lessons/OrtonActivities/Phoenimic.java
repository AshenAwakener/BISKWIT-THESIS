package com.example.biskwit.Content.Lessons.OrtonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biskwit.Content.Lessons.AlphabetActivities.Alphabet;
import com.example.biskwit.Content.Lessons.Score;
import com.example.biskwit.Data.Constants;
import com.example.biskwit.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;

public class Phoenimic extends AppCompatActivity {

    TextView word1,word2,category,txtresult;
    ImageButton mic1,mic2;
    ImageView next,bot,bot2,bot3;
    String word;
    String[] holder;
    String[][] words1;
    String[][] words2;
    String[] categ = {"Hayop","Kasuotan","Prutas","Gulay"};
    int all_ctr = 0, all_ctr2 = 0, click = 0, micctr1 = 0, micctr2 = 0, mic_ctr1 = 0, mic_ctr2 = 0;
    double add = 0, score = 0;
    MediaPlayer ai;

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoenimic);

        word1 = findViewById(R.id.Word);
        word2 = findViewById(R.id.Word2);
        category = findViewById(R.id.Category);
        mic1 = findViewById(R.id.Mic);
        mic2 = findViewById(R.id.Mic2);
        bot2 = findViewById(R.id.Bot2);
        next = findViewById(R.id.nextButton);
        txtresult = findViewById(R.id.result);

        progressDialog = new ProgressDialog(Phoenimic.this);

        getData();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fil-PH");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() { txtresult.setText("Listening..."); }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                txtresult.setText("Press the Mic Button Again");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                if(micctr1>0) {
                    ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    word = data.get(0);
                    printSimilarity(word.toString(), words1[all_ctr][all_ctr2]);
                    micctr1=0;
                }
                else if(micctr2>0){
                    ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    word = data.get(0);
                    printSimilarity(word.toString(), words2[all_ctr][all_ctr2]);
                    micctr2=0;
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==0){
                    stopPlaying();
                    speechRecognizer.startListening(speechRecognizerIntent);
                    mic1.setImageResource(R.drawable.mic_on);
                    txtresult.setText("Speak Now");
                    micctr1++;
                    mic_ctr1++;
                    click++;
                }
                else{
                    speechRecognizer.stopListening();
                    mic1.setImageResource(R.drawable.mic_off);
                    txtresult.setText("Press the Mic Button");
                    click=0;
                }
            }
        });

        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==0){
                    stopPlaying();
                    speechRecognizer.startListening(speechRecognizerIntent);
                    mic2.setImageResource(R.drawable.mic_on);
                    txtresult.setText("Speak Now");
                    micctr2++;
                    mic_ctr2++;
                    click++;
                }
                else{
                    speechRecognizer.stopListening();
                    mic2.setImageResource(R.drawable.mic_off);
                    txtresult.setText("Press the Mic Button");
                    click=0;
                }
            }
        });

        word1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                Resources res = getResources();
                int sound = res.getIdentifier(words1[all_ctr][all_ctr2], "raw", getPackageName());
                ai = MediaPlayer.create(Phoenimic.this, sound);
                ai.start();
            }
        });

        bot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                ai = MediaPlayer.create(Phoenimic.this, R.raw.kab2_p1);
                ai.start();
            }
        });

       word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                Resources res = getResources();
                int sound = res.getIdentifier(words2[all_ctr][all_ctr2], "raw", getPackageName());
                ai = MediaPlayer.create(Phoenimic.this, sound);
                ai.start();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all_ctr < (words1.length - 1)) {
                    if (mic_ctr1 == 0 || mic_ctr2 == 0) {
                        showToast("Try it both first!");
                    } else {
                        setupnext();
                        txtresult.setText("Press the Mic Button");
                        word1.setText(words1[all_ctr][all_ctr2]);
                        word2.setText(words2[all_ctr][all_ctr2]);
                        category.setText(categ[all_ctr]);
                        mic_ctr1 = 0;
                        mic_ctr2 = 0;
                        score += add;
                        stopPlaying();
                    }
                } else {
                    if (mic_ctr1 == 0 || mic_ctr2 == 0) {
                        showToast("Try it both first!");
                    } else {
                        setupnext();
                        Intent intent = new Intent(Phoenimic.this, Score.class);
                        intent.putExtra("LessonType","Orton");
                        intent.putExtra("LessonMode","Phonemic");
                        //intent.putExtra("Letter",letter);
                        intent.putExtra("Score", score);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void showToast(String s) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(s);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void setupnext(){
        if(all_ctr2 < 4){
            ++all_ctr2;
        } else {
            ++all_ctr;
            all_ctr2=0;
        }
    }

    protected void stopPlaying(){
        // If media player is not null then try to stop it
        if(ai!=null){
            ai.stop();
            ai.release();
            ai = null;
        }
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0;}

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public void printSimilarity(String s, String t) {

        float val = Float.parseFloat(String.format(
                "%.3f", similarity(s, t), s, t));
        if(val >= 0.0 && val <= 0.49){
            add = 0;
            showToast("TRY AGAIN");
            ai = MediaPlayer.create(Phoenimic.this, R.raw.response_0_to_49);
            ai.start();
        }
        else if(val >= 0.5 && val <= 0.99){
            add = 0.5;
            showToast("GOOD, BUT YOU CAN DO BETTER");
            ai = MediaPlayer.create(Phoenimic.this, R.raw.response_50_to_69);
            ai.start();
        }
        else if(val ==1.0){
            add = 1;
            showToast("GREAT! YOU DID IT!");
            ai = MediaPlayer.create(Phoenimic.this, R.raw.response_70_to_100);
            ai.start();
        }
    }

    private void getData() {

        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading lesson...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "https://biskwitteamdelete.000webhostapp.com/fetch_phoenemic.php";

        StringRequest stringRequest = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Phoenimic.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        //HashSet<String> data = new HashSet<String>();
        ArrayList<String> data2 = new ArrayList<String>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constants.JSON_ARRAY);
            int length = result.length();
            for(int i = 0; i < length; i++) {
                JSONObject collegeData = result.getJSONObject(i);
                //data.add(collegeData.getString("category"));
                data2.add(collegeData.getString("word"));
            }
            //categ = new String[data.size()];
            //categ = data.toArray(categ);
            holder = new String[data2.size()];
            holder = data2.toArray(holder);

            int holder_ctr=0;
            words1 = new String[4][5];
            words2 = new String[4][5];
            for(int z = 0;z < 4;z++) {
                for (int i = 0; i < 5; i++) {
                    words1[z][i] = holder[holder_ctr];
                    holder_ctr++;
                }
                for (int k = 0; k < 5; k++) {
                    words2[z][k] = holder[holder_ctr];
                    holder_ctr++;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!holder[0].equals("")){
            category.setText(categ[0]);
            word1.setText(words1[0][0]);
            word2.setText(words2[0][0]);
            progressDialog.dismiss();
        } else {
            Toast.makeText(Phoenimic.this, "No data", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    // code para di magkeep playing yung sounds
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit now?")
                .setMessage("You will not be able to save your progress.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Phoenimic.super.onBackPressed();
                        stopPlaying();
                    }
                }).create().show();
    }
}