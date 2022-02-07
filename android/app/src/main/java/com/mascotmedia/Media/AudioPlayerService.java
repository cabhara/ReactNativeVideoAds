package com.mascotmedia.Media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class AudioPlayerService extends Service {

    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private String audioUrl;

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public class AudioServiceBinder extends Binder {
        AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        final Context context = this;

        player = new SimpleExoPlayer.Builder(context).build();

        playerNotificationManager = new PlayerNotificationManager.Builder(this, 1, "channel").setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                return "Audio Broadcast";
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                Intent intent = new Intent(context, MainActivity.class);
                return PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                return "";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                Bitmap mIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.notification_icon);
                return mIcon;
            }
        }).setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                stopSelf();
            }

            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                startForeground(notificationId,notification);
            }
        })
                .setChannelNameResourceId(R.string.notification_channel)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build();

        playerNotificationManager.setPlayer(player);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new AudioServiceBinder();

    @Override
    public void onDestroy() {
        stopForeground(true);
        playerNotificationManager.setPlayer(null);
        if(player!=null) {
            player.setPlayWhenReady(false);
            player.release();
        }
        player = null;

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAudioUrl(intent.getStringExtra("audioUrl"));
        // Set the media item to be played.
        player.setMediaItem(MediaItem.fromUri(audioUrl));
        // Prepare the player.
        player.prepare();
        player.setPlayWhenReady(true);

        return START_STICKY;
    }

    public void pause(){
        player.setPlayWhenReady(false);
    }

    public void resume(){
        player.setPlayWhenReady(true);
    }

}
