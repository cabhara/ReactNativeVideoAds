package com.mascotmedia.Media;

import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import java.util.Map;

public class AudioViewManager extends ViewGroupManager<FrameLayout> {

    public static final String REACT_CLASS = "AudioViewManager";
    public final int COMMAND_CREATE = 1;
    public final int COMMAND_KILL = 2;
    public final int COMMAND_RESUME = 3;
    public final int COMMAND_PAUSE = 4;

    private String audioUrl;
    private AudioFragment localAudioFragment;

    ReactApplicationContext reactContext;

    public AudioViewManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /**
     * Return a FrameLayout which will later hold the Fragment
     */
    @Override
    public FrameLayout createViewInstance(ThemedReactContext reactContext) {
        return new FrameLayout(reactContext);
    }

    /**
     * Map the "create" command to an integer
     */
    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("create", COMMAND_CREATE,"kill", COMMAND_KILL, "resume", COMMAND_RESUME, "pause", COMMAND_PAUSE);
    }

    /**
     * Handle "create" command (called from JS) and call createFragment method
     */
    @Override
    public void receiveCommand(@NonNull FrameLayout root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        int reactNativeViewId = args.getInt(0);
        int commandIdInt = Integer.parseInt(commandId);
        System.out.println("****AudioViewManager: receiveCommand: " + commandIdInt);
        switch (commandIdInt) {
            case COMMAND_CREATE:
                createFragment(root, reactNativeViewId);
                break;
            case COMMAND_KILL:
                removeFragment(root, reactNativeViewId);
                break;
            case COMMAND_RESUME:
                resumeAudio(root, reactNativeViewId);
                break;
            case COMMAND_PAUSE:
                pauseAudio(root, reactNativeViewId);
                break;
            default: {}
        }
    }

    @ReactProp(name = "audioUrl")
    public void setAudioUrl(FrameLayout view, String value) {
        audioUrl = value;
        System.out.println("****AudioViewManager: setAudioUrl: " + value);
    }

    /**
     * Replace your React Native view with a custom fragment
     */
    public void createFragment(FrameLayout root, int reactNativeViewId) {
        ViewGroup parentView = (ViewGroup) root.findViewById(reactNativeViewId).getParent();
        setupLayout((ViewGroup) parentView);

        final AudioFragment audioFragment = new AudioFragment();
        audioFragment.setAudioUrl(audioUrl);
        audioFragment.setReactNativeViewId(reactNativeViewId);
        audioFragment.setReactContext(reactContext);
        localAudioFragment = audioFragment;
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(reactNativeViewId, audioFragment, String.valueOf(reactNativeViewId))
                .commit();
    }

    /**
     * Stop Playing and Remove the Fragment
     */
    public void removeFragment(FrameLayout root, int reactNativeViewId) {
        //System.out.println("****AudioViewManager: remove Fragment: " + reactNativeViewId);
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        AudioFragment audioFragment = (AudioFragment) activity.getSupportFragmentManager().findFragmentById(reactNativeViewId);
        if(audioFragment != null) {
            //System.out.println("****AudioViewManager: remove Fragment - found fragment");
            activity.getSupportFragmentManager()
                    .beginTransaction().
                    remove(audioFragment).commit();
        }
    }

    /**
     * Pause Playing
     */
    public void pauseAudio(FrameLayout root, int reactNativeViewId) {
        System.out.println("****AudioViewManager: pause Audio: " + reactNativeViewId);
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        AudioFragment audioFragment = (AudioFragment) activity.getSupportFragmentManager().findFragmentById(reactNativeViewId);
        if(audioFragment != null) {
            audioFragment.pausePlaying();
        }
    }

    /**
     * Resume Playing
     */
    public void resumeAudio(FrameLayout root, int reactNativeViewId) {
        System.out.println("****AudioViewManager: resume Audio: " + reactNativeViewId);
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        AudioFragment audioFragment = (AudioFragment) activity.getSupportFragmentManager().findFragmentById(reactNativeViewId);
        if(audioFragment != null) {
            audioFragment.resumePlaying();
        }
    }


    public void setupLayout(ViewGroup view) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren(view);
                view.getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    /**
     * Layout all children properly
     */
    public void manuallyLayoutChildren(ViewGroup view) {
        for (int i=0; i < view.getChildCount(); i++){
            View child = view.getChildAt(i);
            child.measure(
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(),
                            View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(),
                            View.MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Nullable
    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return super.getExportedCustomDirectEventTypeConstants();
    }


    @Override
    public void onDropViewInstance(FrameLayout view) {
        super.onDropViewInstance(view);
        if(localAudioFragment!=null) localAudioFragment.onDestroy();
        localAudioFragment = null;
    }

}
