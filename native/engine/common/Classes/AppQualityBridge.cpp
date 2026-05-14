#include "AppQualityBridge.h"

#if defined(__ANDROID__)

#include "cocos/platform/java/jni/JniHelper.h"

namespace cc_app {

int resolveQuality() {
    int quality = cc::JniHelper::callStaticIntMethod(
        "com/cocos/game/AppQuality", "getQuality");
    return quality == 2 ? 2 : 1;
}

}

#elif !defined(__APPLE__)

namespace cc_app {

int resolveQuality() {
    return 1;
}

}

#endif
