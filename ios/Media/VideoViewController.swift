//
//  VideoViewController.swift
//
//  Created by Christina Bharara.
//
import AVFoundation
import AVKit
import UIKit
import GoogleInteractiveMediaAds

class VideoViewController: UIViewController, IMAAdsLoaderDelegate,IMAAdsManagerDelegate {
  var videoUrl:String = ""
  var adTagUrl:String = ""
  
  var videoView:VideoView?
  
  var adsLoader: IMAAdsLoader!
  var adsManager: IMAAdsManager!
  var contentPlayhead: IMAAVPlayerContentPlayhead?
  
  var playerViewController: AVPlayerViewController!
  
  deinit {
    NotificationCenter.default.removeObserver(self)
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.view.backgroundColor = UIColor.black;
    setUpContentPlayer()
    setUpAdsLoader()
  }
  
  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated);
    requestAds()
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

    let contentURL = URL(string: videoUrl)
    let player = AVPlayer(url: contentURL!)
    
    playerViewController = AVPlayerViewController()
          
    playerViewController.player = player
    
    // Set up your content playhead and contentComplete callback.
    contentPlayhead = IMAAVPlayerContentPlayhead(avPlayer: player)
    NotificationCenter.default.addObserver(
      self,
      selector: #selector(VideoViewController.contentDidFinishPlaying(_:)),
      name: NSNotification.Name.AVPlayerItemDidPlayToEndTime,
      object: player.currentItem);
    
    showContentPlayer()
  }
  
  func showContentPlayer() {
    self.addChild(playerViewController)
    playerViewController.view.frame = self.view.bounds
    self.view.insertSubview(playerViewController.view, at: 0)
    playerViewController.didMove(toParent:self)
  }
  
  func hideContentPlayer() {
    // The whole controller needs to be detached so that it doesn't capture  events from the remote.
    playerViewController.willMove(toParent:nil)
    playerViewController.view.removeFromSuperview()
    playerViewController.removeFromParent()
  }
  
  func setUpAdsLoader() {
      adsLoader = IMAAdsLoader(settings: nil)
      adsLoader.delegate = self
    }
  
  func requestAds() {
    // Create ad display container for ad rendering.
    let adDisplayContainer = IMAAdDisplayContainer(adContainer: self.view, viewController: self)
    // Create an ad request with our ad tag, display container, and optional user context.
    let request = IMAAdsRequest(
      adTagUrl: adTagUrl,
      adDisplayContainer: adDisplayContainer,
      contentPlayhead: contentPlayhead,
      userContext: nil)
    
    adsLoader.requestAds(with: request)
  }
  
  @objc func contentDidFinishPlaying(_ notification: Notification) {
    adsLoader.contentComplete()
  }
  
  // MARK: - IMAAdsLoaderDelegate
  
  func adsLoader(_ loader: IMAAdsLoader, adsLoadedWith adsLoadedData: IMAAdsLoadedData) {
    adsManager = adsLoadedData.adsManager
    adsManager.delegate = self
    adsManager.initialize(with: nil)
  }
  
  func adsLoader(_ loader: IMAAdsLoader, failedWith adErrorData: IMAAdLoadingErrorData) {
    showContentPlayer()
    playerViewController.player?.play()
  }
  
  // MARK: - IMAAdsManagerDelegate
  
  func adsManager(_ adsManager: IMAAdsManager, didReceive event: IMAAdEvent) {
    // Play each ad once it has been loaded
    if event.type == IMAAdEventType.LOADED {
      adsManager.start()
    }
  }
  
  func adsManager(_ adsManager: IMAAdsManager, didReceive error: IMAAdError) {
    // Fall back to playing content
    showContentPlayer()
    playerViewController.player?.play()
  }
  
  func adsManagerDidRequestContentPause(_ adsManager: IMAAdsManager) {
    // Pause the content for the SDK to play ads.
    playerViewController.player?.pause()
    hideContentPlayer()
  }
  
  func adsManagerDidRequestContentResume(_ adsManager: IMAAdsManager) {
    // Resume the content since the SDK is done playing ads (at least for now).
    showContentPlayer()
    playerViewController.player?.play()
  }
  
}

