#import "AppQualityBridge.h"
#import "AppQuality.h"

namespace cc_app {

int resolveQuality() {
    int q = (int)[AppQuality quality];
    return q == 2 ? 2 : 1;
}

}
