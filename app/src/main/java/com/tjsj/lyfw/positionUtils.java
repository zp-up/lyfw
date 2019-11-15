package com.tjsj.lyfw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class positionUtils {

    public Context context;


    public positionUtils(Context context){
        this.context=context;
    }

    @JavascriptInterface
    public  void  addPosition(final String userId){
        SharedPreferences sharedPreferences =context.getSharedPreferences ("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.commit();
        PowerManager pm = (PowerManager)context.getSystemService(
                Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE,"lyfw:TAG");
        wl.acquire();//为了保证任务不被系统休眠打断，申请WakeLock
// 开始我们的任务


        //定位还未写完
//        Toast.makeText(context,"监听到了这个方法"+"--"+userId,Toast.LENGTH_LONG).show();
        final int number = 300;//设置运行五次
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int count = 0;	//从0开始计数，每运行一次timertask次数加一，运行制定次数后结束。
            @Override
            public void run() {
                if(count<number){
                    LocationUtils.initLocation(context);
                    Log.e("lyfw","经度："+LocationUtils.longitude);
                    Log.e("lyfw","纬度："+LocationUtils.latitude);
                } else  {
                    timer.cancel();
                }
                count++;

                postDataHttp(userId);//传递参数到后台
            }
        };
        timer.schedule(task, 0,60000);//每隔2分钟运行一次该程序
        if(number>=300){
            wl.release();//任务结束后释放，如果不写该句。则可以用wl.acquire(timeout)的方式把释放的工作交给系统。
        }
    }

    private static  void  postDataHttp(String userId){
        try {
            OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
            FormBody.Builder body=new FormBody.Builder();
            body.add("userId",userId);
            body.add("lat",LocationUtils.latitude+"");
            body.add("lng",LocationUtils.longitude+"");
            Request request = new Request.Builder()
                    .url("http://t.lyfw.tjsjnet.com/reserve/orders/position.htm").post(body.build()) .build();//创建一个Request对象
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Log.e("lyfw","调用成功返回值"+response.body().string());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
