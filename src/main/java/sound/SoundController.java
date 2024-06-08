package sound;

import game.controller.GameState;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundController {

    private static final long COOLDOWN_PERIOD = 350;
    private Player backgroundMusicPlayer;
    private Map<String, Player> soundEffectPlayers = new HashMap<>();
    private Map<String, Boolean> isSoundEffectPlayingMap = new HashMap<>();
    private Thread backgroundMusicThread;
    private GameState currentGameState;
    private boolean isSoundEffectPlaying;
    private boolean isStartScreenPlaying;
    private boolean isPlayScreenPlaying;
    private long lastPlayedTime = 0;


    public SoundController() {
        preloadSoundEffects();
    }

    public void playBackgroundMusic(String filename) {
        stopBackgroundMusic();
        backgroundMusicThread = new Thread(() -> {
            try {
                GameState initialGameState = currentGameState;
                while (currentGameState != GameState.END) {
                    FileInputStream fis = new FileInputStream(filename);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    if (backgroundMusicPlayer != null) {
                        backgroundMusicPlayer.close();
                    }
                    backgroundMusicPlayer = new Player(bis);
                    backgroundMusicPlayer.play();
                    if (currentGameState != initialGameState) {
                        backgroundMusicPlayer.close();
                        break;
                    }
                    fis.close();
                    bis.close();
                }
            } catch (Exception e) {
                System.out.println("Problem playing background music");
                e.printStackTrace();
            }
        });
        backgroundMusicThread.start();
    }

    private void preloadSoundEffects() {
        loadSoundEffect("res/sounds/soundeffects/run.mp3");
        loadSoundEffect("res/sounds/soundeffects/jump.mp3");
        loadSoundEffect("res/sounds/soundeffects/attack.mp3");
        loadSoundEffect("res/sounds/soundeffects/land.mp3");
        loadSoundEffect("res/sounds/soundeffects/dash.mp3");
    }

    private void loadSoundEffect(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            Player player = new Player(bis);
            soundEffectPlayers.put(filename, player);
        } catch (Exception e) {
            System.out.println("Problem preloading sound effect");
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String filename) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPlayedTime < COOLDOWN_PERIOD) {
            return;
        }

        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(filename);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Player threadPlayer = new Player(bis);
                soundEffectPlayers.put(filename, threadPlayer);
                isSoundEffectPlayingMap.put(filename, true);
                threadPlayer.play();
                isSoundEffectPlayingMap.put(filename, false);
                fis.close();
                bis.close();
            } catch (Exception e) {
                System.out.println("Problem playing sound effect");
                e.printStackTrace();
            }
        }).start();
        lastPlayedTime = currentTime;
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.close();
            if (backgroundMusicThread != null) {
                backgroundMusicThread.interrupt();
            }
        }
    }

    public void soundControl() {
        if (currentGameState == GameState.START && !isStartScreenPlaying) {
            playBackgroundMusic("res/sounds/startScreenSound.mp3");
            setStartScreenPlaying(true);
        } else if (currentGameState == GameState.PLAYING && !isPlayScreenPlaying) {
            playBackgroundMusic("res/sounds/playScreenSound.mp3");
            setPlayScreenPlaying(true);
            stopBackgroundMusic();
        } else if (currentGameState == GameState.END) {
            stopBackgroundMusic();
        }
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public boolean isSoundEffectPlaying() {
        return isSoundEffectPlaying;
    }

    public boolean isPlayScreenPlaying() {
        return isPlayScreenPlaying;
    }

    public void setPlayScreenPlaying(boolean playScreenPlaying) {
        isPlayScreenPlaying = playScreenPlaying;
    }

    public boolean isStartScreenPlaying() {
        return isStartScreenPlaying;
    }

    public void setStartScreenPlaying(boolean startScreenPlaying) {
        isStartScreenPlaying = startScreenPlaying;
    }
}