package com.dantsu.escposprinter.connection;

import android.util.Log;

import com.dantsu.escposprinter.exceptions.EscPosConnectionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public abstract class DeviceConnection {
    protected OutputStream stream;
    protected InputStream inputStream;
    protected byte[] data;

    public DeviceConnection() {
        this.inputStream = null;
        this.stream = null;
        this.data = new byte[0];
    }

    public abstract DeviceConnection connect() throws EscPosConnectionException;
    public abstract DeviceConnection disconnect();

    /**
     * Check if OutputStream is open.
     *
     * @return true if is connected
     */
    public boolean isConnected() {
        return this.stream != null;
    }

    /**
     * Add data to send.
     */
    public void write(byte[] bytes) {
        byte[] data = new byte[bytes.length + this.data.length];
        System.arraycopy(this.data, 0, data, 0, this.data.length);
        System.arraycopy(bytes, 0, data, this.data.length, bytes.length);
        this.data = data;
    }


    /**
     * Send data to the device.
     */
    public void send() throws EscPosConnectionException {
        this.send(0);
    }
    /**
     * Send data to the device.
     */
    public void send(int addWaitingTime) throws EscPosConnectionException {
        if(!this.isConnected()) {
            throw new EscPosConnectionException("Unable to send data to device.");
        }
        try {
            this.stream.write(this.data);
            this.stream.flush();
            this.data = new byte[0];

            int waitingTime = addWaitingTime + (int) Math.floor(this.data.length / 16f);
            if(waitingTime > 0) {
                Thread.sleep(waitingTime);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new EscPosConnectionException(e.getMessage());
        }
    }

    public String getStatusPrinter(){
        String s = null;

        return s;

    }


}
