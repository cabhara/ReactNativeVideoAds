// requireNativeComponent automatically resolves 'RNTAudioView' to 'RNTAudioViewManager'
import React, {
    useRef,
    useImperativeHandle,
    forwardRef,
  } from "react";
  import { UIManager, findNodeHandle } from "react-native";
  import { requireNativeComponent } from "react-native";
  
  const AudioViewManager = requireNativeComponent("RNTAudioView");
  
  function AudioViewIos(props, ref) {
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
      },
    }));
  
    function resumeAudio() {
      const viewId = findNodeHandle(nativeRef.current);
      if (viewId != null) {
        UIManager.dispatchViewManagerCommand(
          viewId,
          UIManager.RNTAudioView.Commands.playFromRN, // we are calling the 'resume' command
          []
        );
      }
    }
  
    function pauseAudio() {
      const viewId = findNodeHandle(nativeRef.current);
      if (viewId != null) {
        UIManager.dispatchViewManagerCommand(
          viewId,
          UIManager.RNTAudioView.Commands.pauseFromRN, // we are calling the 'pause' command
          []
        );
      }
    }
  
    return <AudioViewManager ref={nativeRef} audioUrl={props.url} />;
  }
  
  export default forwardRef(AudioViewIos);
  