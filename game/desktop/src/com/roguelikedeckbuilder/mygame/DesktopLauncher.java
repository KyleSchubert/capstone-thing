package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(MyGame.windowWidth, MyGame.windowHeight);
        config.useVsync(true);
        config.setForegroundFPS(MyGame.FPS_CAP);
        config.setTitle("Roguelike Deck-builder Game");
        new Lwjgl3Application(new MyGame(), config);
    }
}
