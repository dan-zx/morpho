package com.morpho.android.intent;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.morpho.android.R;
import com.morpho.android.data.Schedule;
import com.morpho.android.receiver.VoiceReplyReceiver;
import com.morpho.android.ws.AsyncTaskAdapter;
import com.morpho.android.ws.Schedules;
import com.morpho.android.ws.impl.MorphoClientFactory;

import java.util.List;

public class ReceiveTransitionsIntentService extends IntentService {

    private static final String TAG = ReceiveTransitionsIntentService.class.getSimpleName();

    private static int notificationId;
    
    private NotificationManagerCompat notificationManager;
    
    public ReceiveTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent");
        if (LocationClient.hasError(intent)) {
            int errorCode = LocationClient.getErrorCode(intent);
            Log.e(TAG, "Location Services error: " + Integer.toString(errorCode));
        } else {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                final long stationId = Long.parseLong(LocationClient.getTriggeringGeofences(intent).get(0).getRequestId());
                Log.d(TAG, "Entered in:" + LocationClient.getTriggeringGeofences(intent));
                MorphoClientFactory morphoClientFactory = new MorphoClientFactory(getApplicationContext());
                Schedules schedules = morphoClientFactory.get(Schedules.class);
                schedules.fetch()
                    .comingSchedules(stationId)
                    .only("CIRCUITO") // TODO: retrieve bus route from preferences
                    .limitTo(3)
                    .loadSchedules(new AsyncTaskAdapter<List<Schedule>>() {

                        @Override
                        public void onPostExecute(List<Schedule> result) {
                            Log.d(TAG, "Result: " + result);
                            if (!result.isEmpty()) notificationManager.notify(++notificationId, buildNotification(stationId, result));
                        }
                    });
            } else Log.e(TAG, "Geofence transition error: " + Integer.toString(transitionType));
        }
    }
    
    private Notification buildNotification(long stationId, List<Schedule> schedules) {
        if (schedules.size() > 1) { // More than one
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_notification_schedules)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Horarios")
                .setTicker("Horarios de próximos autobuses")
                .setContentIntent(getIntent(stationId));
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
                    .append(DateFormat.getTimeFormat(getApplicationContext()).format(schedule.getDepartureAt()));
                style.addLine(contentText.toString());
                if (i > 0) { 
                    Notification newPageNotification = new NotificationCompat.Builder(this)
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
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_notification_schedules)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Horario")
                .setTicker("Horario del próximo autobus")
                .setContentIntent(getIntent(stationId));
            StringBuilder contentText = new StringBuilder().append("Ruta ")
                    .append(schedules.get(0).getRoute().getName());
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Horario");
            style.addLine(contentText.toString());
            contentText = new StringBuilder().append("* Próxima salida: ")
                    .append(DateFormat.getTimeFormat(getApplicationContext()).format(schedules.get(0).getDepartureAt()));
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
    
    private PendingIntent getIntent(long stationId) {
        Intent replyIntent = new Intent(this, VoiceReplyReceiver.class);
        replyIntent.setAction("VOICE_REPLY");
        replyIntent.putExtra(VoiceReplyReceiver.EXTRA_STATION_ID_REPLAY, stationId);
        return PendingIntent.getBroadcast(this, 0, replyIntent, 0);
    }
}