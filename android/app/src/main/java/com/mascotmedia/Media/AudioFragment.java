package com.mascotmedia.Media;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ServiceConnection;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import android.app.Service;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ExoPlayer;//
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class AudioFragment extends Fragment {

    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private String audioUrl;
    private int reactNativeViewId;
    private ReactApplicationContext reactContext;
    boolean bounded;
    private AudioPlayerService audioPlayerService;

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setReactNativeViewId(int reactNativeViewId) {
        this.reactNativeViewId = reactNativeViewId;
    }

    public void setReactContext(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void resumePlaying(){
        audioPlayerService.resume();
    }

    public void pausePlaying(){
        audioPlayerService.pause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.audio_view,parent,false);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioPlayerService = ((AudioPlayerService.AudioServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bounded = false;
            audioPlayerService = null;
        }

    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // do any logic that should happen in an `onCreate` method
        MultiDex.install(requireContext());

        Intent intent = new Intent(requireContext(), AudioPlayerService.class);
        intent.putExtra("audioUrl",audioUrl);
        if(requireContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE)){
            bounded = true;
        } else{
            System.out.println("****AudioFragment: could not bind service");
        }

        Util.startForegroundService(requireContext(),intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        // do any logic that should happen in an `onPause` method
        // e.g.: customView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // do any logic that should happen in an `onResume` method
        // e.g.: customView.onResume();
    }

    @Override
    public void onDestroy() {
        // do any logic that should happen in an `onDestroy` method
        if(audioPlayerService != null) {
            audioPlayerService.pause();
            audioPlayerService.stopForeground(true);
            audioPlayerService.stopSelf();
        }
        super.onDestroy();
    }

}

