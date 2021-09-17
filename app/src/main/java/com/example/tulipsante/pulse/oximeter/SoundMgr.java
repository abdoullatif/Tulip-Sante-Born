package com.example.tulipsante.pulse.oximeter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AndroidRuntimeException;


import com.example.tulipsante.R;

import java.util.HashMap;

public class SoundMgr implements SoundPool.OnLoadCompleteListener {

    private int[] soundIDs = {R.raw.heart_beat, R.raw.lead_off};

    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundMapInit = new HashMap<>();
    private HashMap<Integer, Integer> mSoundMapComplete = new HashMap<>();
    private static SoundMgr mSoundMangerInstance;

    public static SoundMgr getSoundManager() {
        if (mSoundMangerInstance == null) {
            synchronized (SoundMgr.class) {
                if (mSoundMangerInstance == null) {
                    mSoundMangerInstance = new SoundMgr();
                }
            }
        }
        return mSoundMangerInstance;
    }

    public void initSoundLoad(Context context) {
        if (context == null) {
            return;
        }
        for (int i = 0; i < soundIDs.length; i++) {
            loadSound(context, i);
        }
    }

    public void cleanSoundRes() {
        mSoundMangerInstance = null;
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        if (mSoundMapInit != null) {
            mSoundMapInit.clear();
            mSoundMapInit = null;
        }
        if (mSoundMapComplete != null) {
            mSoundMapComplete.clear();
            mSoundMapComplete = null;
        }
    }

    public void playHeartBeat() {
        playSound(0);
    }

    public void playLeadOff() {
        playSound(1);
    }

    /**
     * 记录soundID，用于播放时指定特定的音频
     */
    private void loadSound(Context context, int resId) throws AndroidRuntimeException {
        AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(soundIDs[resId]);
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 2);
        }
        int soundId = mSoundPool.load(fileDescriptor, 1);
        mSoundMapInit.put(soundId, resId);
        mSoundPool.setOnLoadCompleteListener(this);
    }

    private void playSound(int resId) {
        if (mSoundMangerInstance.mSoundPool != null) {
            if (mSoundMangerInstance.mSoundMapComplete != null) {
                Integer soundID = mSoundMangerInstance.mSoundMapComplete.get(resId);
                if (soundID != null) {
                    mSoundMangerInstance.mSoundPool.play(soundID, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            mSoundMapComplete.put(mSoundMapInit.get(sampleId), sampleId);
        }
    }
}
