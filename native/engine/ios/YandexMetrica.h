#import <Foundation/Foundation.h>
#import <YandexMobileMetrica/YandexMobileMetrica.h>

@interface YandexMetrica : NSObject

+ (void)reportEvent:(NSString *)event;

@end
