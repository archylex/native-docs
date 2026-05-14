#import "WebUrl.h"

@implementation WebUrl

+ (void)openUrl:(NSString *)sUrl {
    NSURL *url = [NSURL URLWithString:sUrl];

    dispatch_async(dispatch_get_main_queue(), ^{
        [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:nil];
    });
}

@end
