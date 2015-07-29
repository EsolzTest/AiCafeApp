package com.esolz.aicafeapp.gcmnotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.esolz.aicafeapp.ActivityLandingPage;
import com.esolz.aicafeapp.ActivitySplash;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ltp on 24/07/15.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    String TAG = "GcmIntentService";

    public static String MY_EVENT_ACTION = "My_Events";


    public GCMIntentService() {
        super("GcmIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString(), "error", "");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                //extras.toString(), "lostMessage", "");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                Log.v("Notification", "############ Received ############");
                // Post notification of received message.
                Log.i(TAG, "Received: " + extras.toString());

//                final String chat_id = "", send_from = "", send_to = "", message = "", type = "", stickername = "", chat_time = "", chat_date = "",
//                        status = "", file_link = "", file_available = "", name = "", photo = "", photo_thumb = "";

                final String chat_id = extras.getString("chat_id");
                final String send_from = extras.getString("send_from");
                final String send_to = extras.getString("send_to");
                final String message = extras.getString("message");
                final String type = extras.getString("type");
                final String stickername = extras.getString("stickername");
                final String chat_time = extras.getString("2");
                final String chat_date = extras.getString("chat_date");
                final String status = extras.getString("status");
                final String file_link = extras.getString("file_link");
                final String file_available = extras.getString("file_available");
                final String name = extras.getString("name");
                final String photo = extras.getString("photo");
                final String photo_thumb = extras.getString("photo_thumb");

//                /*
//                Bundle[{=, =Unread,
//                from=104883704281,
//                =Rahul.Roy, =s,
//                =21,
//                =userimage/normal/21_.jpg, =303,
//                =test,
//                =Y,
//                =userimage/thumb/21_.jpg,
//                =abc,
//                =2015-07-24 19:25:37,
//                =O, android.support.content.wakelockid=1,
//                collapse_key=do_not_collapse, =43}]
//                 */


                if (AppController.isAppRunning().equals("YES")) {

                    Intent brodIntent = new Intent();

                    brodIntent.setAction(MY_EVENT_ACTION);
                    // brodIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    brodIntent.putExtra("chat_id", "" + chat_id);
                    brodIntent.putExtra("send_from", "" + send_from);
                    brodIntent.putExtra("send_to", "" + send_to);
                    brodIntent.putExtra("message", "" + message);
                    brodIntent.putExtra("type", "" + type);
                    brodIntent.putExtra("stickername", "" + stickername);
                    brodIntent.putExtra("chat_time", "" + chat_time);
                    brodIntent.putExtra("chat_date", "" + chat_date);
                    brodIntent.putExtra("status", "" + status);
                    brodIntent.putExtra("file_link", "" + file_link);
                    brodIntent.putExtra("file_available", "" + file_available);
                    brodIntent.putExtra("name", "" + name);
                    brodIntent.putExtra("photo", "" + photo);
                    brodIntent.putExtra("photo_thumb", "" + photo_thumb);

                    sendBroadcast(brodIntent);

                    Log.d("----TAG----", "Received new msg.....");
                } else {
                    if (type.equals("s")) {
                        sendNotification(name + " sent a sticker ", "", send_to);
                    } else if (type.equals("m")) {
                        sendNotification(message, name, send_to);
                    } else {
                        sendNotification(message, name, send_to);
                    }
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadCastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String name, String sendTo) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = null;
        String notificationTitle = "";


//        Intent intentLand = new Intent(this, ActivityLandingPage.class);
//        intentLand.putExtra("Notification", "notify");
//        intentLand.putExtra("NotiSendId", sendTo);
//
//        contentIntent = PendingIntent.getActivity(this, 0, intentLand, 0);

        notificationTitle = name;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(notificationTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public int getStikerImg(String jsonStiker) {

        if (jsonStiker.equals("1")) {
            return R.drawable.aione;
        } else if (jsonStiker.equals("2")) {
            return R.drawable.aitwo;
        } else if (jsonStiker.equals("3")) {
            return R.drawable.aithree;
        } else if (jsonStiker.equals("4")) {
            return R.drawable.aifour;
        } else if (jsonStiker.equals("5")) {
            return R.drawable.aifive;
        } else if (jsonStiker.equals("6")) {
            return R.drawable.aisix;
        } else if (jsonStiker.equals("7")) {
            return R.drawable.aiseven;
        } else {
            return R.drawable.aieight;
        }

    }

}

