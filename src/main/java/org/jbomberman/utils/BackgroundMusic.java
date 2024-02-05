package org.jbomberman.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class BackgroundMusic {
    private static final String MENUSOUNDTRACK = Objects.requireNonNull(BackgroundMusic.class.getResource("intro.mp3").toExternalForm());
    private static final String GAMESOUNDTRACK = Objects.requireNonNull(BackgroundMusic.class.getResource("game.mp3")).toExternalForm();
    private static final AudioClip GAMEBOMB = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("tnt_exp.mp3")).toExternalForm());
    private static final AudioClip SUCCESS = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("success.mp3")).toExternalForm());
    private static final AudioClip COIN = new AudioClip(BackgroundMusic.class.getResource("coin.mp3").toExternalForm());
    private static final AudioClip LOST = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("lost.mp3")).toExternalForm());
    private static final AudioClip DOOR = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("door.mp3").toExternalForm()));
    private static final AudioClip ONE_UP = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("smb_1-up.wav").toExternalForm()));
    private static final AudioClip BIG_BOMB = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("smb_powerup.wav").toExternalForm()));
    private static final AudioClip CLICK = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("click.wav").toExternalForm()));
    private static final AudioClip DEATH = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("death.mp3").toExternalForm()));
    private static final AudioClip ENEMY_DEATH = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("enemy_death.mp3").toExternalForm()));
    private static final AudioClip INVINCIBLE = new AudioClip(Objects.requireNonNull(BackgroundMusic.class.getResource("Invincible.mp3").toExternalForm()));

    private static MediaPlayer gameMediaPlayer;
    private static MediaPlayer menuMediaPlayer;
    private static boolean isGamePlaying = false;
    private static boolean isMenuPlaying = false;


    public static void playGameMusic(){
        Media media = new Media(GAMESOUNDTRACK);
        gameMediaPlayer =  new MediaPlayer(media);
        gameMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduce la musica in modo continuo
        gameMediaPlayer.play();
        isGamePlaying = true;
    }

    public static void playMenuMusic(){
        Media media = new Media(MENUSOUNDTRACK);
        menuMediaPlayer = new MediaPlayer(media);
        menuMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        menuMediaPlayer.play();
        isMenuPlaying = true;
    }

    public static void stopMenuMusic(){
        menuMediaPlayer.stop();
        isMenuPlaying = false;
    }
    public static boolean isPlaying(){
        return isGamePlaying;
    }
    public static boolean isMenuPlaying() {
        return isMenuPlaying;
    }

    public static void stopGameMusic(){
        isGamePlaying = false;
        gameMediaPlayer.stop();
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
    public static void playClick(){
        CLICK.play();
    }

    public static void playLost(){
        LOST.play();
    }

    public static void playOneUp(){
        ONE_UP.play();
    }

    public static void playBigBomb() {
        BIG_BOMB.play();
    }

    public static void playDeath(){
        DEATH.play();
    }

    public static void playInvincible(){
        INVINCIBLE.play();
    }

    public static void stopInvincible(){
        if (INVINCIBLE.isPlaying()){
            INVINCIBLE.stop();
        }
    }

    public static void playEnemyDeath(){
         ENEMY_DEATH.play();
     }
}