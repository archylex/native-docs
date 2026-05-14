#import "YandexMetrica.h"

@implementation YandexMetrica

+ (void)reportEvent:(NSString *)event {
    if (event != nil) {
        [YMMYandexMetrica reportEvent:event onFailure:nil];
    }
}

@end
