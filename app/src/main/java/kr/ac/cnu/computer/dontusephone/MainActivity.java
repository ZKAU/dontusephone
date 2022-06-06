package kr.ac.cnu.computer.dontusephone;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;
import static kr.ac.cnu.computer.dontusephone.timesetting.*;

public class MainActivity extends AppCompatActivity {
    public static int st1;
    public static int wt1;



    static final String d1="d1";
    static final String d2="d2";
    int a;
    static Context context;
    static TextView t1;
    static TextView t2;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.time1);//시작 시간
        t2=findViewById(R.id.time2);//종료 시간
        if (savedInstanceState != null) {
            String data1 = savedInstanceState.getString(d1);
            String data2 = savedInstanceState.getString(d2);
            t1.setText(data1);
            t2.setText(data2);
        }


        Button bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jj", String.valueOf(st1));
                Log.d("jj", String.valueOf(wt1));
                if(st1==wt1){
                    Toast.makeText(getApplicationContext(), "시작시간과 종료시간이 같아요", Toast.LENGTH_SHORT).show();
                }
                else if(st1==0 && wt1==0){
                    Toast.makeText(getApplicationContext(), "시작시간과 종료시간이 같아요", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "서비스가 10초후 시작됩니다", Toast.LENGTH_SHORT).show();
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {

                            checkPermission();
                        }
                    }, 10000);
                }
            }
        });
        Button butout=(Button)findViewById(R.id.but3);
        butout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),timesetting.class);
                startActivity(intent);
            }
        });

        Button bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jj", String.valueOf(st1));
                Log.d("jj", String.valueOf(wt1));
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });

    }

    public void checkPermission() {
        Intent service=new Intent(this,MyService.class);
        service.putExtra("mellung1",st1);
        service.putExtra("mellung2",wt1);
        Log.d("jj", "check start");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(service);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(service);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        startService(new Intent(this, MyService.class));
    }
    public void OneTimeWorker(Context context){
        Log.d("jj", "OTW start");
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadWorker.class).setInitialDelay(5, TimeUnit.SECONDS).build();
        WorkManager.getInstance(context).enqueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sleeptime.equals("")){

        }
        else{
            t1.setText(sleeptime);
        }
        if(waketime.equals("")){

        }
        else{
            t2.setText(waketime);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String data1=t1.getText().toString();
        String data2=t2.getText().toString();
        outState.putString(d1,data1);
        outState.putString(d2,data2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}