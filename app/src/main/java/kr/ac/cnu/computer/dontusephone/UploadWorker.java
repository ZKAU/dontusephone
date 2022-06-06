package kr.ac.cnu.computer.dontusephone;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.*;

public class UploadWorker extends Worker {
    Context context;
    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);


    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Intent intent=new Intent(MyService.context,MyService.class);
            Log.d("jj", "doWork start");
            (MyService.context).startService(intent);
            return Result.success();
        } catch(Exception e){
            return Result.failure();
        }

    }


}
