package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.roguelikedeckbuilder.mygame.MyGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1440, 920); // 1440x920 looks kind of OK
        config.setResizable(false);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setTitle("Roguelike Deck-builder Game");
        new Lwjgl3Application(new MyGame(), config);
    }
}
