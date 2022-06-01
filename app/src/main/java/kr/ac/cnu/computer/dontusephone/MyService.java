package kr.ac.cnu.computer.dontusephone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;


public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //변수 설명
    //numnum은 팝업의 버튼 터치 횟수에 관여합니다
    //num과 um은 팝업에 대한 오류수정을 위한 변수입니다
    //ServiceONOFF는 서비스가 꺼진 이후로 딜레이를 주어 워크매니저를 실행하는데 있어서 사용되는 변수입니다 값이 0이면 워크매니저가 딜레이 후 실행되고
    //값이 1이면 서비스가 종료된 이후에도 워크매니저가 실행되지 않습니다. 값이 1이되는 경우는 자기 의지로 끈 종료버튼을 눌렀을 때만 해당됩니다.
    //값이 0인 경우는 종료시간이 될 때까지 서비스를 실행하여 완전히 시간을 다 채웠을 때만 값 변경없이 다음 날 딜레이 후인 취침시간에 다시 켜지도록 하였습니다.
    int num[]={0};
    int um[]={0};
    int numnum[]={1};
    static int ServiceONOFF[]={0};
    int time1_H;//취침시간(시간)
    int time2_H;//기상시간(시간)
    int time1_M;//취침시간(분)
    int time2_M;//취침시간(분)

    int HOUR;//time2_H-time1_H인 기상시간으로부터 취침시간까지의 딜레이가 됨
    int MINUTES;//time2_S-time1_S, 위와 마찬가기
    //만약 취침시간이 새벽이면

    int REALTIME;//분으로 계산할 예정 HOUR*60+MINUTES
    WindowManager wm;
    View mView;
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("jj", "Service start");



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
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.bt);
        final ImageButton bt2=  (ImageButton) mView.findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now=System.currentTimeMillis();
                Date date=new Date(now);
                SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
                String getTime = simpleDateFormatTime.format(date);

                if(numnum[0]!=3){
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
    public void onDestroy() {
        super.onDestroy();
        context=this;
        OneTimeWorker(context);
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

