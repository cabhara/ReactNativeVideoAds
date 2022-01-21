//
//  AudioView.swift
//
//  Created by Christina Bharara on 1/4/22.
//
import UIKit

class AudioView: UIView {
  
  weak var audioViewController: AudioViewController?
  
  @objc var onEnded: RCTBubblingEventBlock?
  
  @objc var audioUrl: String? {
    didSet {
      setNeedsLayout()
    }
  }
  
  @objc func pauseFromRN() {
    self.audioViewController?.playerViewController.player?.pause()
  }

  @objc func playFromRN() {
    self.audioViewController?.playerViewController.player?.play()
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
  }
  required init?(coder aDecoder: NSCoder) { fatalError("nope") }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    
    if (self.audioViewController==nil) {
      embed()
    } else {
      audioViewController?.view.frame = bounds
    }
  }
  
  private func embed() {
    guard
      let parentVC = parentViewController
    else {
        return
      }
    
    let avc = AudioViewController()
    avc.audioUrl = audioUrl ?? ""
    avc.audioView = self
    parentVC.addChild(avc)
    addSubview(avc.view)
    avc.view.frame = bounds
    avc.didMove(toParent: parentVC)
    self.audioViewController = avc
  }
}

