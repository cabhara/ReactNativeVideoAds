//
//  RNTAudioViewManager.m
//
//  Created by Christina Bharara on 1/4/22.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import "bhs-Swift.h"

@interface RNTAudioViewManager : RCTViewManager

@end

@implementation RNTAudioViewManager

RCT_EXPORT_VIEW_PROPERTY(audioUrl, NSString);
RCT_EXPORT_VIEW_PROPERTY(onPaused, RCTBubblingEventBlock)
RCT_EXTERN_METHOD(
  pauseFromRN:(nonnull NSNumber *)node
)
RCT_EXTERN_METHOD(
  playFromRN:(nonnull NSNumber *)node
)

RCT_EXPORT_MODULE()

- (UIView *)view {
  
  NSLog(@"RNTAudioViewManager view method");
  return [AudioView new];
  
}

- (void) pauseFromRN:(NSNumber *)node{
  RCTLog(@"pauseFromRN");
  [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView*> *viewRegistry){
    AudioView *view = (AudioView *) viewRegistry[node];
    if (!view || ![view isKindOfClass:[AudioView class]]){
      RCTLogError(@"Cannot find AudioView with tag #%@", node);
                  return;
    }
    [view pauseFromRN];
  }];
}

- (void) playFromRN:(NSNumber *)node{
  RCTLog(@"playFromRN");
  [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView*> *viewRegistry){
    AudioView *view = (AudioView *) viewRegistry[node];
    if (!view || ![view isKindOfClass:[AudioView class]]){
      RCTLogError(@"Cannot find AudioView with tag #%@", node);
                  return;
    }
    [view playFromRN];
  }];
}

@end
