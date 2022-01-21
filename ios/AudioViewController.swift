//
//  AudioViewController.swift
//
//  Created by Christina Bharara on 1/4/22.
//

import AVFoundation
import AVKit
import UIKit

class AudioViewController: UIViewController {
  var audioUrl:String = ""
    
  var audioView:AudioView?
  
  var playerViewController: AVPlayerViewController!
  
  deinit {
    NotificationCenter.default.removeObserver(self)
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.view.backgroundColor = UIColor.black;
    setUpContentPlayer()
  }
  
  func setUpContentPlayer() {
    // Load AVPlayer with path to your content.
    do{
      // Configure and activate the AVAudioSession
      try AVAudioSession.sharedInstance().setCategory(
        AVAudioSession.Category.playback
      )
      try AVAudioSession.sharedInstance().setActive(true)
    } catch {
      //error setting audio
    }
    if(audioUrl != ""){
      
      let contentURL = URL(string: audioUrl)
      let player = AVPlayer(url: contentURL!)
      playerViewController = AVPlayerViewController()

      playerViewController.player = player

      showContentPlayer()
      playerViewController.player?.play()
    }
  }
  
  func showContentPlayer() {
    self.addChild(playerViewController)
    playerViewController.view.frame = self.view.bounds
    self.view.insertSubview(playerViewController.view, at: 0)
    playerViewController.didMove(toParent:self)
  }
}

