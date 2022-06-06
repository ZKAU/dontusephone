package kr.ac.cnu.computer.dontusephone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;

import static kr.ac.cnu.computer.dontusephone.MainActivity.*;
import static kr.ac.cnu.computer.dontusephone.MyService.e1;
import static kr.ac.cnu.computer.dontusephone.MyService.e2;

public class timesetting extends Activity {
    static Context context;
    int hour_s, minute_s, hour_w, minute_w;
    static String sleeptime="";//시작 시간값을 String값으로 받아 t1에 입력
    static String waketime="";//종료 시간값을 String값으로 받아 t2에 입력


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeset);
        context=this;
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
                sleeptime=hour_s+" "+":"+" "+minute_s;
                waketime=hour_w+" "+":"+" "+minute_w;
                st1=hour_s*60+minute_s;
                wt1=hour_w*60+minute_w;
                MyService.e1=hour_s*60+minute_s;
                MyService.e2=hour_w*60+minute_w;
                if(((MainActivity)MainActivity.context).st1<((MainActivity)MainActivity.context).wt1){
                    ((MainActivity)MainActivity.context).st1=((MainActivity)MainActivity.context).st1+1440;
                }
                t1.setText(sleeptime);
                t2.setText(waketime);
                Toast.makeText(getApplicationContext(), "시간이 설정되었습니다", Toast.LENGTH_SHORT).show();
                onBackPressed();
                Log.d("jj", String.valueOf(st1));
                Log.d("jj", String.valueOf(wt1));


            }
        });


    }


}
