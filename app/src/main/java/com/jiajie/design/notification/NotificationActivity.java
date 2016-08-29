package com.jiajie.design.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jiajie.design.R;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationActivity for notification test
 * Created by jiajie on 16/8/21.
 */
public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";

    private Button button;

    private NotificationManager mNotificationManager;

    private static final int mDefault = 0;
    private static final int mSingle = 1;
    private static final int mMulti = 2;
    private static final int mBigPic = 3;
    private static final int mList = 4;
    private static final int mAction = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showNotification();
                singleLine();
//                multiLine();
//                bigPic();
//                list();
//                withAction();
            }
        });


    }

    private void custom() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , 0
                , new Intent(this, ResultActivity.class)
                , PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void withAction() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = createBuilder();

        builder.addAction(R.drawable.ic_email_black_24dp, "email", null);
        builder.addAction(R.drawable.ic_key_black_24dp, "key", null);

        mNotificationManager.notify(mAction, builder.build());

    }

    private void list() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = createBuilder();

        List<String> contents = new ArrayList<>();
        contents.add("1");
        contents.add("2");
        contents.add("3");
        contents.add("4");
        contents.add("5");

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builder);
        for (String content : contents) {
            style.addLine(content);
        }
        style.setSummaryText(contents.size() + "条消息");
        style.setBigContentTitle("我是标题...");
        Notification notification = style.build();
        mNotificationManager.notify(mList, notification);
    }

    private void bigPic() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = createBuilder();

        Bitmap big = BitmapFactory.decodeResource(getResources(), R.drawable.nav_bar_background);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.BigPictureStyle(builder)
                .bigLargeIcon(icon)
                .bigPicture(big)
                .build();

        mNotificationManager.notify(mBigPic, notification);


    }

    private void multiLine() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = createBuilder();

        Notification notification = new NotificationCompat.BigTextStyle(builder).bigText("bigText").build();

        mNotificationManager.notify(mMulti, notification);
    }

    private void singleLine() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "您的手机低于Android 4.1.2，不支持！！", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = createBuilder();

        mNotificationManager.notify(mSingle, builder.build());

    }

    private NotificationCompat.Builder createBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setTicker("我是状态栏文字...")
                .setContentTitle("我是标题...")
                .setContentText("我是内容...");

        builder.setColor(Color.RED);

        //正在运行，无法删除
//        builder.setOngoing(true);
        //默认提醒铃声、震动、闪烁
        builder.setDefaults(Notification.DEFAULT_SOUND |
//                Notification.DEFAULT_VIBRATE |
                Notification.DEFAULT_LIGHTS);

        return builder;
    }

    private void showNotification() {
        NotificationCompat.Builder builder = createBuilder();

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ResultActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(mDefault, builder.build());


    }


}
