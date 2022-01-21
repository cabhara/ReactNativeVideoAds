import * as React from 'react';
import { View, Text, Button } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import VideoScreen from './screens/VideoScreen';
import AudioScreen from './screens/AudioScreen'

function HomeScreen({ navigation }) {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center',paddingBottom:80 }}>
      <Button
        title="Video"
        onPress={() => navigation.navigate('Video')}
      />
      <Button
        title="Audio"
        onPress={() => navigation.navigate('Audio')}
      />
    </View>
  );
}

const Stack = createNativeStackNavigator();

function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Video" component={VideoScreen} />
        <Stack.Screen name="Audio" component={AudioScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
