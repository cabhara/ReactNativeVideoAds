#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import "Media-Swift.h"

@interface RNTVideoViewManager : RCTViewManager

@end

@implementation RNTVideoViewManager

RCT_EXPORT_VIEW_PROPERTY(videoUrl, NSString);
RCT_EXPORT_VIEW_PROPERTY(adTagUrl, NSString);

RCT_EXPORT_MODULE()

- (UIView *)view {
  
  return [VideoView new];
  
}

@end
