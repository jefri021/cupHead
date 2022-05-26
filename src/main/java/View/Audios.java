package View;

import javafx.scene.media.AudioClip;

public enum Audios {
    EGG_0(new AudioClip(Audios.class.getResource("/Audios/Game/egg0.wav").toString())),
    EGG_1(new AudioClip(Audios.class.getResource("/Audios/Game/egg1.wav").toString())),
    EGG_2(new AudioClip(Audios.class.getResource("/Audios/Game/egg2.wav").toString())),
    EGG_3(new AudioClip(Audios.class.getResource("/Audios/Game/egg3.wav").toString())),

    SPIT_0(new AudioClip(Audios.class.getResource("/Audios/Game/spit0.wav").toString())),
    SPIT_1(new AudioClip(Audios.class.getResource("/Audios/Game/spit1.wav").toString())),
    SPIT_2(new AudioClip(Audios.class.getResource("/Audios/Game/spit2.wav").toString())),
    SPIT_3(new AudioClip(Audios.class.getResource("/Audios/Game/spit3.wav").toString())),
    SPIT_4(new AudioClip(Audios.class.getResource("/Audios/Game/spit4.wav").toString())),

    MINI_BOSS_DEATH_0(new AudioClip(Audios.class.getResource("/Audios/Game/MiniBossDeath/0.wav").toString())),
    MINI_BOSS_DEATH_1(new AudioClip(Audios.class.getResource("/Audios/Game/MiniBossDeath/1.wav").toString())),
    MINI_BOSS_DEATH_2(new AudioClip(Audios.class.getResource("/Audios/Game/MiniBossDeath/2.wav").toString())),
    MINI_BOSS_DEATH_3(new AudioClip(Audios.class.getResource("/Audios/Game/MiniBossDeath/3.wav").toString())),

    BULLET(new AudioClip(Audios.class.getResource("/Audios/Game/bullet.mp3").toString())),

    BOSS_DEATH(new AudioClip(Audios.class.getResource("/Audios/Game/bossDeath.mp3").toString())),

    LOSE(new AudioClip(Audios.class.getResource("/Audios/Game/lose.mp3").toString()));

    private final AudioClip audioClip;

    Audios (AudioClip audioClip) {
        this.audioClip = audioClip;
    }

    public AudioClip getAudioClip() {
        if (this.equals(BULLET) || this.equals(MINI_BOSS_DEATH_0) || this.equals(MINI_BOSS_DEATH_1) ||
                this.equals(MINI_BOSS_DEATH_2) || this.equals(MINI_BOSS_DEATH_3))
            audioClip.setVolume(0.2);
        return audioClip;
    }

}
