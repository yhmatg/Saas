package com.common.esimrfid.uhf;


/**
 * Created by tiansen on 18-3-28.
 */

public interface IEsimUhfService {
    boolean initRFID();

    boolean startScanning();

    boolean stopScanning();

    boolean closeRFID();

    boolean startStopScanning();

    boolean isEnable();

    boolean isStart();

    void setSoundOpen(boolean isOpen);

    boolean isSound();

    int getDownKey();
}
