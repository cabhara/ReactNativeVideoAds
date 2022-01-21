import React, { useState, useRef } from "react";
import {
  View,
  Dimensions,
  TouchableOpacity,
  Image,
} from "react-native";
import AudioViewIos from "../components/AudioViewIos";
import Constants from "expo-constants";

const audioLink = "http://www.largesound.com/ashborytour/sound/AshboryBYU.mp3";
const width=Dimensions.get("window").width;

export default function AudioStreamModal() {
  const ref = useRef(null);

  const [playingAudio, setPlayingAudio] = useState(true);

  async function toggleAudio() {
    //we are playing right now, pause
    if (playingAudio) {
      if (ref.current) {
        ref.current.pauseAudio();
        setPlayingAudio(false);
      }
      //we are paused, play
    } else {
      if (ref.current) {
        ref.current.resumeAudio();
        setPlayingAudio(true);
      }
    }
  }
  
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center", paddingBottom:80 }}>
          <TouchableOpacity onPress={toggleAudio}>
            {playingAudio && (
              <Image
                style={{ width: 100, height: 100, alignSelf: "center" }}
                resizeMode="cover"
                source={require("../assets/video_pause_transparent.png")}
              />
            )}
            {!playingAudio && (
              <Image
                style={{ width: 100, height: 100, alignSelf: "center" }}
                resizeMode="cover"
                source={require("../assets/video_play_transparent.png")}
              />
            )}
          </TouchableOpacity>

          {/* {Constants.platform.android && (
            <View
              style={{
                height: 3,
                width: width * 0.64,
                backgroundColor: "grey",
              }}
            >
              <AudioView ref={ref} url={broadcastOptionAudioLink} />
            </View>
          )} */}

          {Constants.platform.ios && (
            <View
            style={{
              height: 3,
              width: width * 0.64,
              backgroundColor: "grey",
            }}
          >
            <AudioViewIos
              ref={ref} 
              url={audioLink}
            />
            </View>
          )}
        </View>
  );
}
