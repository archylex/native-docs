#include "Game.h"
#include "AppQualityBridge.h"
#include "cocos/bindings/jswrapper/SeApi.h"

#ifndef GAME_NAME
#define GAME_NAME "CocosGame";
#endif

#ifndef SCRIPT_XXTEAKEY
#define SCRIPT_XXTEAKEY "";
#endif

Game::Game() = default;

int Game::init() {
  _windowInfo.title = GAME_NAME;
  // configurate window size
  // _windowInfo.height = 600;
  // _windowInfo.width  = 800;

#if CC_DEBUG
  _debuggerInfo.enabled = true;
#else
  _debuggerInfo.enabled = false;
#endif
  _debuggerInfo.port = 6086;
  _debuggerInfo.address = "0.0.0.0";
  _debuggerInfo.pauseOnStart = false;

  _xxteaKey = SCRIPT_XXTEAKEY;

  se::ScriptEngine::getInstance()->addPermanentRegisterCallback(
      [](se::Object *global) -> bool {
        se::Value v;
        v.setInt32(cc_app::resolveQuality());
        global->setProperty("APP_QUALITY", v);
        return true;
      });

  BaseGame::init();
  return 0;
}

void Game::onPause() { BaseGame::onPause(); }

void Game::onResume() { BaseGame::onResume(); }

void Game::onClose() { BaseGame::onClose(); }

CC_REGISTER_APPLICATION(Game);
