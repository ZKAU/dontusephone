package kr.ac.cnu.computer.dontusephone;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static kr.ac.cnu.computer.dontusephone.MainActivity.st1;
import static kr.ac.cnu.computer.dontusephone.MainActivity.wt1;

public class MyService extends Service {
    static int e1=3;
    static int e2=7;

    String CHANNEL_ID="";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("jj", "foregroundService start");
        e1=intent.getIntExtra("mellung1",0);
        e2=intent.getIntExtra("mellung2",0);
        if(intent == null){
            return START_STICKY;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel =
                    new NotificationChannel(CHANNEL_ID, "알림 설정 모드 타이틀", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("알림 타이틀")
                .setContentText("알림 설명")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //변수 설명
    //numnum은 팝업의 버튼 터치 횟수에 관여합니다
    //num과 um은 팝업에 대한 오류수정을 위한 변수입니다
    //num값이 0이면 화면에 팝업이 떠있는 거고 아니면 떠있지 않은 겁니다
    //서비스가 꺼지면 um값이 1이 됩니다.
    //ServiceONOFF는 서비스가 꺼진 이후로 딜레이를 주어 워크매니저를 실행하는데 있어서 사용되는 변수입니다 값이 0이면 워크매니저가 딜레이 후 실행되고
    //값이 1이면 서비스가 종료된 이후에도 워크매니저가 실행되지 않습니다. 값이 1이되는 경우는 자기 의지로 끈 종료버튼을 눌렀을 때만 해당됩니다.
    //값이 0인 경우는 종료시간이 될 때까지 서비스를 실행하여 완전히 시간을 다 채웠을 때만 값 변경없이 다음 날 딜레이 후인 취침시간에 다시 켜지도록 하였습니다.
    int num[]={0};
    int um[]={0};
    int numnum[]={1};
    int cum[]={0};
    static int ServiceONOFF[]={0};

    int st2;
    WindowManager wm;
    View mView;
    static Context context;


    @Override
    public void onCreate() {

        super.onCreate();



        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                /*ViewGroup.LayoutParams.MATCH_PARENT*/MATCH_PARENT,
                MATCH_PARENT,
                0, 0, // X, Y 좌표
                TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        mView = inflate.inflate(R.layout.view_in_service, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d("jj", "e1="+String.valueOf(e1));
                    Log.d("jj", "e2="+String.valueOf(e2));
                    st2=e1;
                    int hour = 0;
                    int minute = 0;
                    LocalTime now= null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        now = LocalTime.now();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        hour = now.getHour();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        minute = now.getMinute();
                    }
                    String gt1 = Integer.toString(hour);
                    String gt2 = Integer.toString(minute);
                    int timenow = Integer.parseInt(gt1) * 60 + Integer.parseInt(gt2);

                    if (e1 > 1440) {
                        st2 = e1 - 1440;
                    }
                    Log.d("jj", "st2="+String.valueOf(st2));
                    Log.d("jj", "timenow="+String.valueOf(timenow));
                    if (timenow == st2) {
                        numnum[0] = 1;
                        if (num[0] == 1) {

                        }
                    }
                    if (timenow == e2) {
                        if(num[0]==0){
                            if(cum[0]==0){
                                wm.removeView(mView);
                                cum[0]=1;
                            }
                            else{

                            }

                        }
                        numnum[0]=5;
                    }
                    else{
                        cum[0]=0;
                    }
                    if (timenow == e1) {
                        if(cum[0]==0){
                            numnum[0]=1;
                            wm.addView(mView, params);
                            cum[0]=1;
                        }
                        else{
                        }
                    }
                    try{
                        Thread.sleep(10000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }


        }).start();
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.bt);
        final ImageButton bt2=  (ImageButton) mView.findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numnum[0]<3){
                    int k=(int)(Math.random()*10);
                    int t=(int)(Math.random()*10);
                    if(k>3){
                        bt2.setTranslationX((float)Math.random()*500);
                    }
                    else{
                        bt2.setTranslationX((float)Math.random()*-500);
                    }
                    if(t>5){
                        bt2.setTranslationY((float)Math.random()*500);
                    }
                    else{
                        bt2.setTranslationY((float)Math.random()*-500);
                    }
                    numnum[0]+=1;
                    Log.d("jj",String.valueOf(numnum[0]));
                }
                else if(numnum[0]==5){
                    wm.removeView(mView);
                }
                else{
                    wm.removeView(mView);
                    num[0]=1;
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {

                            num[0]=0;
                            if(um[0]==1){

                            }
                            else{
                                wm.addView(mView, params);
                            }
                        }
                    }, 5000);
                    numnum[0]=1;

                }
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        wm.addView(mView, params);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        if(wm != null) {
            if(mView != null) {
                if(num[0]==0){
                    wm.removeView(mView);
                    mView = null;
                }
                else{
                    mView = null;
                }
            }
            wm = null;
            um[0]=1;
        }



    }

    public void OneTimeWorker(Context context){
        Log.d("jj", "OTW start");
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UploadWorker.class).setInitialDelay(10, TimeUnit.SECONDS).build();
        WorkManager.getInstance(context).enqueue(request);
    }
}

