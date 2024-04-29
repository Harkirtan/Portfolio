package com.controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Loads music and sets if it should start/pause
 * @author Harkirtan Singh
 */
public class MusicManager {
    private boolean m_playing = false;
    private Clip m_clip;

    /**
     * Getter for if the music is playing
     * @return true if music is playing
     */
    public boolean isM_Playing(){return this.m_playing;}

    /**
     * Setter for if the music is playing
     * @param m_playing true if music is playing
     */
    private void setM_playing(boolean m_playing) {
        this.m_playing = m_playing;
    }

    /**
     * Getter for the clip that holds music
     * @return clip containing music
     */
    private Clip getM_clip() {
        return m_clip;
    }

    /**
     * Setter for the clip that holds music
     * @param m_clip clip containing music
     */
    private void setM_clip(Clip m_clip) {
        this.m_clip = m_clip;
    }

    /**
     * Loads music from file and adds it to clip
     */
    public void initialiseMusic(){
        File file = new File("src/main/resources/temp_music.wav");
        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        this.setM_clip(null);
        try {
            this.setM_clip(AudioSystem.getClip());
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            getM_clip().open(audioStream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets playing to true and plays music infinitely
     */
    public void startMusic(){
        this.setM_playing(true);
        getM_clip().start();
        getM_clip().loop(Clip.LOOP_CONTINUOUSLY);
    }
    /**
     * Sets playing to false and pauses music
     */
    public void stopMusic(){
        this.setM_playing(false);
        getM_clip().stop();
    }
}
