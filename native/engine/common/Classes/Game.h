#pragma once

#include "cocos/cocos.h"

/**
 @brief    The cocos2d Application.

 The reason for implement as private inheritance is to hide some interface call
 by Director.
 */
class Game : public cc::BaseGame {
public:
  Game();
  int init() override;
  // bool init() override;
  void onPause() override;
  void onResume() override;
  void onClose() override;
};
