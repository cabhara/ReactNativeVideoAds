//
//  VideoView.swift
//
//  Created by Christina Bharara
//

import UIKit

class VideoView: UIView {
  
  weak var videoViewController: VideoViewController?
  
  @objc var videoUrl: String? {
    didSet {
      setNeedsLayout()
    }
  }
  
  @objc var adTagUrl: String? {
    didSet {
      print("VideoView adTagUrl: " + (adTagUrl ?? "nothing"));
      setNeedsLayout()
    }
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
  }
  required init?(coder aDecoder: NSCoder) { fatalError("nope") }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    
    if (self.videoViewController==nil) {
      embed()
    } else {
      videoViewController?.view.frame = bounds
    }
  }
  
  private func embed() {
    guard
      let parentVC = parentViewController
    else {
        return
    }
    
    let vc = VideoViewController()
    vc.videoUrl = videoUrl ?? ""
    vc.adTagUrl = adTagUrl ?? ""
    
    vc.videoView = self
    
    parentVC.addChild(vc)
    addSubview(vc.view)
    vc.view.frame = bounds
    vc.didMove(toParent: parentVC)
    self.videoViewController = vc
  }
}

extension UIView {
  var parentViewController: UIViewController? {
    var parentResponder: UIResponder? = self
    while parentResponder != nil {
      parentResponder = parentResponder!.next
      if let viewController = parentResponder as? UIViewController {
        return viewController
      }
    }
    return nil
  }
}
