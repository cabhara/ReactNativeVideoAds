package com.mascotmedia.Media;

import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;

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

public class VideoViewManager extends ViewGroupManager<FrameLayout> {

    public static final String REACT_CLASS = "VideoViewManager";
    public final int COMMAND_CREATE = 1;
    public final int COMMAND_KILL = 2;

    private String videoUrl;
    private String adTagUrl;

    private VideoFragment localVideoFragment;

    ReactApplicationContext reactContext;

    public VideoViewManager(ReactApplicationContext reactContext) {
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
        return MapBuilder.of("create", COMMAND_CREATE,"kill", COMMAND_KILL);
    }

    /**
     * Handle "create" command (called from JS) and call createFragment method
     */
    @Override
    public void receiveCommand(@NonNull FrameLayout root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        int reactNativeViewId = args.getInt(0);
        int commandIdInt = Integer.parseInt(commandId);
        System.out.println("****VideoViewManager: receiveCommand: " + commandIdInt);
        switch (commandIdInt) {
            case COMMAND_CREATE:
                createFragment(root, reactNativeViewId);
                break;
            case COMMAND_KILL:
                removeFragment(root, reactNativeViewId);
                break;
            default: {}
        }
    }

    @ReactProp(name = "videoUrl")
    public void setVideoUrl(FrameLayout view, String value) {
        videoUrl = value;
        System.out.println("****VideoViewManager: setVideoUrl: " + value);
    }

    @ReactProp(name = "adTagUrl")
    public void setAdTagUrl(FrameLayout view, String value) {
        adTagUrl = value;
        System.out.println("****VideoViewManager: setAdTagUrl: " + value);
    }

    /**
     * Replace your React Native view with a custom fragment
     */
    public void createFragment(FrameLayout root, int reactNativeViewId) {
        ViewGroup parentView = (ViewGroup) root.findViewById(reactNativeViewId).getParent();
        setupLayout((ViewGroup) parentView);

        final VideoFragment videoFragment = new VideoFragment();
        videoFragment.setVideoUrl(videoUrl);
        videoFragment.setAdTagUrl(adTagUrl);
        videoFragment.setReactNativeViewId(reactNativeViewId);
        videoFragment.setReactContext(reactContext);
        localVideoFragment = videoFragment;
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(reactNativeViewId, videoFragment, String.valueOf(reactNativeViewId))
                .commit();
    }

    /**
     * Replace your React Native view with a custom fragment
     */
    public void removeFragment(FrameLayout root, int reactNativeViewId) {
        System.out.println("****VideoViewManager: remove Fragment: " + reactNativeViewId);
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        VideoFragment videoFragment = (VideoFragment) activity.getSupportFragmentManager().findFragmentById(reactNativeViewId);
        if(videoFragment != null) {
            System.out.println("****VideoViewManager: remove Fragment - found fragment");
            activity.getSupportFragmentManager()
                    .beginTransaction().
                    remove(videoFragment).commit();
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
        System.out.println("****VideoViewManager: onDropViewInstance");
        super.onDropViewInstance(view);
        if(localVideoFragment!=null) localVideoFragment.onDestroy();
    }
}

