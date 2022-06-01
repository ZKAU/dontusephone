package kr.ac.cnu.computer.dontusephone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.Nullable;

import java.sql.Time;

import static kr.ac.cnu.computer.dontusephone.MainActivity.t1;
import static kr.ac.cnu.computer.dontusephone.MainActivity.t2;

public class timesetting extends Activity {
    int hour_s, minute_s, hour_w, minute_w;

    static String sleeptime="";//시작 시간값을 String값으로 받아 t1에 입력
    static String waketime="";//종료 시간값을 String값으로 받아 t2에 입력


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeset);
        TimePicker timePicker_sleep = findViewById(R.id.timePicker1);
        TimePicker timePicker_wakeUp = findViewById(R.id.timePicker2);
        Button btn_getTime =findViewById(R.id.btn_getTime);
        btn_getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour_s = timePicker_sleep.getCurrentHour();
                minute_s = timePicker_sleep.getCurrentMinute();
                hour_w = timePicker_wakeUp.getCurrentHour();
                minute_w = timePicker_wakeUp.getCurrentMinute();
                sleeptime=Integer.toString(hour_s)+" "+":"+" "+Integer.toString(minute_s);
                waketime=Integer.toString(hour_w)+" "+":"+" "+Integer.toString(minute_w);
                t1.setText(sleeptime);
                t2.setText(waketime);
                onBackPressed();


            }
        });


    }


}
