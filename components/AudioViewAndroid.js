import React, {
  useEffect,
  useRef,
  useImperativeHandle,
  forwardRef,
} from "react";
import {
  UIManager,
  findNodeHandle,
} from "react-native";
import { requireNativeComponent } from "react-native";
const AudioViewManager = requireNativeComponent("AudioViewManager");

function AudioView(props, ref) {
  const nativeRef = useRef(null);

  useImperativeHandle(ref, () => ({
    // methods connected to `ref`
    resumeAudio: () => {
      if (__DEV__) {
        console.log("RN calling resumeAudio ");
      }
      resumeAudio();
    },
    pauseAudio: () => {
      if (__DEV__) {
        console.log("RN calling pauseAudio ");
      }
      pauseAudio();
    }
  }));

  function createFragment() {
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.AudioViewManager.Commands.create.toString(), // we are calling the 'create' command
        [viewId]
      );
    }
  }

  function killFragment() {
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.AudioViewManager.Commands.kill.toString(), // we are calling the 'kill' command
        [viewId]
      );
    }
  }

  function resumeAudio() {
    if (__DEV__) {
      console.log("RN resumeAudio ");
    }
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.AudioViewManager.Commands.resume.toString(), // we are calling the 'resume' command
        [viewId]
      );
    }
  }

  function pauseAudio() {
    if (__DEV__) {
      console.log("RN pauseAudio ");
    }
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.AudioViewManager.Commands.pause.toString(), // we are calling the 'pause' command
        [viewId]
      );
    }
  }

  useEffect(() => {
    if (__DEV__) {
      console.log("RN AudioView: " + props.url);
    }
    setTimeout(() => {
      createFragment();
    }, 500);
    return () => {
      //kill the android side when leaving the view
      killFragment();
    };
  }, []);

  return (
    <AudioViewManager
      ref={nativeRef}
      audioUrl={props.url}
    />
  );
}

export default forwardRef(AudioView);
