import React, { useEffect, useRef } from "react";
import { UIManager, findNodeHandle } from "react-native";
import { requireNativeComponent } from "react-native";
const VideoViewManager = requireNativeComponent("VideoViewManager");

const VideoView = ({ url, adTag }) => {
  const nativeRef = useRef(null);

  useEffect(() => {
    if (__DEV__) {
      console.log("RN VideoView: " + url);
    }
    setTimeout(() => {
      createFragment();
    }, 500);
    return () => {
      //kill the android side when leaving the view
      killFragment();
    };
  }, []);

  function createFragment() {
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.VideoViewManager.Commands.create.toString(), // we are calling the 'create' command
        [viewId]
      );
    } 
  }

  function killFragment() {
    const viewId = findNodeHandle(nativeRef.current);
    if (viewId != null) {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.VideoViewManager.Commands.kill.toString(), // we are calling the 'kill' command
        [viewId]
      );
    } 
  }

  return (
    <VideoViewManager
      ref={nativeRef}
      videoUrl={url}
      adTagUrl={adTag}
    />
  );
};

export default VideoView;
