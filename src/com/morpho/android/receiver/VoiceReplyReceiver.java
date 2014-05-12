package com.morpho.android.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import com.morpho.android.R;
import com.morpho.android.data.Schedule;
import com.morpho.android.util.Strings;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Schedules;
import com.morpho.android.ws.impl.MorphoClientFactory;

import java.util.List;

public class VoiceReplyReceiver extends BroadcastReceiver {

    public static final String EXTRA_VOICE_REPLAY = "extra_voice_reply";
    public static final String EXTRA_STATION_ID_REPLAY = "extra_station_id_replay";
    
    private static final String TAG = VoiceReplyReceiver.class.getSimpleName();
    
    private static int notificationId;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onReceive, intent=" + intent.getExtras());
        MorphoClientFactory morphoClientFactory = new MorphoClientFactory(context);
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String voiceOutput = intent.getExtras().getString(EXTRA_VOICE_REPLAY);
        final long stationId = intent.getExtras().getLong(EXTRA_STATION_ID_REPLAY);
        if (!Strings.isNullOrBlank(voiceOutput)) {
            morphoClientFactory.get(Schedules.class)
                .fetch()
                .comingSchedules(stationId)
                .only(voiceOutput)
                .limitTo(3)
                .loadSchedules(new AsyncTaskAdapter<List<Schedule>>() {
        
                    @Override
                    public void onPostExecute(List<Schedule> result) {
                        Log.d(TAG, "Result: " + result);
                        if (!result.isEmpty()) notificationManager.notify(++notificationId, buildNotification(stationId, result, context));
                    }
                });
        }
    }
    
    private Notification buildNotification(long stationId, List<Schedule> schedules, Context context) {
        if (schedules.size() > 1) { // More than one
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_schedules)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Horarios")
                .setTicker("Horarios de próximos autobuses")
                .setContentIntent(getIntent(stationId, context));
            WearableNotifications.Builder wearableNotificationsBuilder = new WearableNotifications.Builder(notificationBuilder);
            int i = 0;
            for (Schedule schedule : schedules) {
                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                    .setBigContentTitle("Horario");
                StringBuilder contentText = new StringBuilder()
                    .append("Ruta ")
                    .append(schedule.getRoute().getName());
                style.addLine(contentText.toString());
                contentText = new StringBuilder()
                    .append("* Próxima salida: ")
                    .append(DateFormat.getTimeFormat(context).format(schedule.getDepartureAt()));
                style.addLine(contentText.toString());
                if (i > 0) { 
                    Notification newPageNotification = new NotificationCompat.Builder(context)
                        .setStyle(style)
                        .build();
                    wearableNotificationsBuilder.addPage(newPageNotification);
                } else notificationBuilder.setStyle(style);
                i++;
            }

            return wearableNotificationsBuilder.addRemoteInputForContentIntent(
                    new RemoteInput.Builder(VoiceReplyReceiver.EXTRA_VOICE_REPLAY)
                        .setLabel("¿Otra ruta?")
                        .build())
                    .build();
        } else { // One
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_schedules)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Horario")
                .setTicker("Horario del próximo autobus")
                .setContentIntent(getIntent(stationId, context));
            StringBuilder contentText = new StringBuilder().append("Ruta ")
                    .append(schedules.get(0).getRoute().getName());
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Horario");
            style.addLine(contentText.toString());
            contentText = new StringBuilder().append("* Próxima salida: ")
                    .append(DateFormat.getTimeFormat(context).format(schedules.get(0).getDepartureAt()));
            style.addLine(contentText.toString());
            notificationBuilder.setStyle(style);

            Notification replyNotification = new WearableNotifications.Builder(notificationBuilder)
                    .addRemoteInputForContentIntent(
                            new RemoteInput.Builder(VoiceReplyReceiver.EXTRA_VOICE_REPLAY)
                                .setLabel("¿Otra ruta?")
                                .build())
                    .build();
            
            return replyNotification;
        }
    }
    
    private PendingIntent getIntent(long stationId, Context context) {
        Intent replyIntent = new Intent(context, VoiceReplyReceiver.class);
        replyIntent.setAction("VOICE_REPLY");
        replyIntent.putExtra(VoiceReplyReceiver.EXTRA_STATION_ID_REPLAY, stationId);
        return PendingIntent.getBroadcast(context, 0, replyIntent, 0);
    }
}