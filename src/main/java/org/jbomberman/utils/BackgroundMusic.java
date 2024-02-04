package org.jbomberman.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class BackgroundMusic {
    private static final String GAMESOUNDTRACK = Objects.requireNonNull(BackgroundMusic.class.getResource("UndertaleOST2.mp3")).toExternalForm();
    private static final AudioClip GAMEBOMB = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("tnt_exp.mp3")).toExternalForm());
    private static final AudioClip SUCCESS = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("success.mp3")).toExternalForm());
    private static final AudioClip COIN = new AudioClip(BackgroundMusic.class.getResource("coin.mp3").toExternalForm());
    private static final AudioClip DEATH = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("death.mp3")).toExternalForm());
    private static final AudioClip DOOR = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("door.mp3").toExternalForm()));
    private static final AudioClip ONE_UP = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("smb_1-up.wav").toExternalForm()));
    private static final AudioClip BIG_BOMB = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("smb_powerup.wav").toExternalForm()));

    private static MediaPlayer mediaPlayer;
    private static boolean isPlaying = false;


    public static void playMusic(){
        Media media = new Media(GAMESOUNDTRACK);
        mediaPlayer =  new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduce la musica in modo continuo
        mediaPlayer.play();
        isPlaying = true;
    }

    public static boolean isPlaying(){
        return isPlaying;
    }
    public static void stopMusic(){
        isPlaying = false;
        mediaPlayer.stop();
    }

    public static void playBomb(){
        GAMEBOMB.play();
    }

    public static void playDoor(){
     DOOR.play();
    }

    public static void playSuccess() {
        SUCCESS.play();
    }

    public static void playCoin(){
        COIN.play();
    }

    public static void playDeath(){
        DEATH.play();
    }

    public static void playOneUp(){
        ONE_UP.play();
    }

    public static void playBigBomb() {
        BIG_BOMB.play();
    }
}