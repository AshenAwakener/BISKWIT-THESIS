package com.example.biskwit.Content.Lessons.KatinigActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.biskwit.R;

public class KatinigLesson1 extends AppCompatActivity {

    ImageButton back;
    ImageView bot2;
    MediaPlayer ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katinig_lesson1);

        back = findViewById(R.id.backbutton);
        bot2 = findViewById(R.id.Bot2);

        ai = MediaPlayer.create(KatinigLesson1.this, R.raw.kab4_p1_1);
        ai.start();

        bot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                ai = MediaPlayer.create(KatinigLesson1.this, R.raw.kab4_p1_1);
                ai.start();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                finish();
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
}