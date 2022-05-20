package kr.ac.cnu.computer.dontusephone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;


public class MyService extends Service {
    int num[]={0};
    int um[]={0};
    WindowManager wm;
    View mView;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.bt);
        final ImageButton bt2=  (ImageButton) mView.findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k=(int)Math.random()*10;
                int t=(int)Math.random()*10;
                if(k>5){
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
                }, 1000);
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
}
