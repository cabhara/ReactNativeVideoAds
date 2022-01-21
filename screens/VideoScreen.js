import * as React from "react";
import { View, Text, Dimensions } from "react-native";
import Constants from "expo-constants";
import VideoViewIos from "../components/VideoViewIos"

const videoLink = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8";
const adTagUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";

const width=Dimensions.get("window").width;

export default function VideoScreen() {
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center",paddingBottom:80 }}>
      {Constants.platform.ios && (
            <VideoViewIos
              videoUrl={videoLink}
              adTagUrl={adTagUrl}
              style={{
                height: width * 0.56,
                width: width,
                backgroundColor: "grey",
              }}
            />
          )}
    </View>
  );
}
