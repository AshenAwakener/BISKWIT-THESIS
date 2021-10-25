package com.example.biskwit.Content.Story.PabulaActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.example.biskwit.DBHelper;
import com.example.biskwit.R;

public class LamokLeon extends AppCompatActivity {

    TextView txtstory,txtword,txtqueue;
    ImageView next,bot;
    ImageButton mic;
    String word = "";
    DBHelper DB;
    Cursor c;
    String[] P_Lesson_Words = {"Pagod na pagod si Leon mula sa paghahanap ng pagkain","Kailangan kong matulog upang bumalik ang aking lakas sabi ni Leon sa kaniyang sarili",
            "Katutulog pa lamang ni Leon nang dumating si Lamok", "Nakita niya na natutulog si Leon kaya naisipan niyang guluhin ito",
            "Lumipad siya nang paikot-ikot kay Leon","Minsan ay binubulungan niya ito o kaya naman ay kinakagat niya ito sa tainga",
            "Pakiusap huwag mo akong guluhin","Kailangan kong matulog sabi ni Leon","Hindi kita susundin","Hindi ako natatakot sa iyo",
            "ang mayabang na sagot ni Lamok","Muling ginulo ni Lamok si Leon","Sa kalilipad ni Lamok nang paikot-ikot","hindi niya napansin ang sapot sa halaman na nasa likuran ni Leon",
            "Patuloy pa rin sa panggugulo si Lamak kaya nang tila hahampasin na siya ni Leon","bigla siyang napaatras","Sa kaniyang pag-atras ay napadikit siya sa sapot",
            "Sinubukan ni Lamok na makawala sa pagkakadikit sa sapot ngunit hindi niya magawa","Nang maubos ang kaniyang lakas at mawalan ng pag-asa",
            "bigla niyang naisip ang panggugulong ginawa niya kay Leon"};
    String queue="",story="";
    StringBuffer buff;
    int all_ctr = 0;
    int click = 0;
    int queuectr=2;
    MediaPlayer ai;

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    private int CurrentProgress = 0;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamok_leon);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);
        }

        txtword = (TextView) findViewById(R.id.Word);
        txtstory = (TextView) findViewById(R.id.Story);
        txtqueue = (TextView) findViewById(R.id.Queue);
        next = findViewById(R.id.nextButton);
        bot = findViewById(R.id.Bot);
        mic = findViewById(R.id.imageView2);

        //DB = new DBHelper(this);

        //String letter = getIntent().getStringExtra("letter");

        //c = DB.getlessondata(letter);

        /*if(c.getCount()==0){
            Toast.makeText(this, "No data...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (int i = 0;c.moveToNext();i++) {
                buff = new StringBuffer();
                //buff.append(c.getString(c.getColumnIndex("P_Lesson_Word")));
                //P_Lesson_Words[i] = buff.toString();
            }
        }
        c.close();*/
        progressBar = findViewById(R.id.ProgressBar); // need ito para sa progress

        txtword.setText(P_Lesson_Words[all_ctr]);
        for(int i = 1; i < 9; i++) {
            queue += (P_Lesson_Words[i] + "\n ");
            txtqueue.setText(queue);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++all_ctr;
                story += (P_Lesson_Words[all_ctr-1] + ". ");
                txtstory.setText(story);

                txtword.setText(P_Lesson_Words[all_ctr]);
                queue = " ";

                for(int i = queuectr; i < 9; i++) {
                    queue += (P_Lesson_Words[i] + ". ");
                }

                txtqueue.setText(queue);
                ++queuectr;

                stopPlaying();

                CurrentProgress = CurrentProgress +11;
                progressBar.setProgress(CurrentProgress);
                progressBar.setMax(100);

                //pampagrayscale lang to nung bot na icon
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                bot.setColorFilter(new ColorMatrixColorFilter(matrix));
            }
        });

        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                Resources res = getResources();
                int sound = res.getIdentifier(P_Lesson_Words[all_ctr], "raw", getPackageName());
                ai = MediaPlayer.create(LamokLeon.this, sound);
                ai.start();
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fil-PH");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                word = data.get(0);
                printSimilarity(word.toString(),P_Lesson_Words[all_ctr]);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==0){
                    speechRecognizer.startListening(speechRecognizerIntent);
                    mic.setImageResource(R.drawable.mic_on);
                    click++;
                }
                else{
                    speechRecognizer.stopListening();
                    mic.setImageResource(R.drawable.mic_off);
                    click=0;
                }
            }
        });
    }

    protected void stopPlaying(){
        // If media player is not null then try to stop it
        if(ai!=null){
            ai.stop();
            ai.release();
            ai = null;
        }
    }


    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
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
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


    // TRY NEW ALGORITHM
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
            ai = MediaPlayer.create(LamokLeon.this, R.raw.response_0_to_49);
            ai.start();
        }
        else if(val >= 0.5 && val <= 0.99){
            ai = MediaPlayer.create(LamokLeon.this, R.raw.response_50_to_69);
            ai.start();
        }
        else if(val ==1.0){
            ai = MediaPlayer.create(LamokLeon.this, R.raw.response_70_to_100);
            ai.start();
        }

    }
}