#import "AppQuality.h"
#import <UIKit/UIKit.h>

static NSString * const OVERRIDE_KEY = @"app_quality_override";
static const int QUALITY_SD = 1;
static const int QUALITY_HD = 2;
static const double MIN_RAM_MB_FOR_HD = 2048.0;
static const int MIN_OS_MAJOR_FOR_HD = 10;

@implementation AppQuality

+ (int)quality {
    int override = (int)[[NSUserDefaults standardUserDefaults] integerForKey:OVERRIDE_KEY];
    if (override == QUALITY_SD || override == QUALITY_HD) {
        return override;
    }
    return [self deviceQuality];
}

+ (void)setOverrideQuality:(int)q {
    [[NSUserDefaults standardUserDefaults] setInteger:q forKey:OVERRIDE_KEY];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (int)deviceQuality {
    double ramMb = [self totalMemoryMb];
    int osMajor = [self osMajorVersion];
    BOOL isOld = (ramMb > 0 && ramMb <= MIN_RAM_MB_FOR_HD)
                 || (osMajor > 0 && osMajor < MIN_OS_MAJOR_FOR_HD);
    return isOld ? QUALITY_SD : QUALITY_HD;
}

+ (double)totalMemoryMb {
    unsigned long long bytes = [NSProcessInfo processInfo].physicalMemory;
    return (double)(bytes / (1024ULL * 1024ULL));
}

+ (int)osMajorVersion {
    NSString *version = [UIDevice currentDevice].systemVersion;
    if (version.length == 0) {
        return 0;
    }
    NSRange dot = [version rangeOfString:@"."];
    NSString *major = dot.location == NSNotFound ? version : [version substringToIndex:dot.location];
    return [major intValue];
}

@end
