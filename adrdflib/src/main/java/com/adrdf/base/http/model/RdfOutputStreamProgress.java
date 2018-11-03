package com.adrdf.base.http.model;

import com.adrdf.base.http.listener.RdfHttpResponseListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOutputStreamProgress
 * Describe：带进度的输出流
 * Date：2017-08-27 15:51:05
 * Author: CapRobin@yeah.net
 *
 */
public class RdfOutputStreamProgress extends OutputStream {

    private OutputStream outputStream;
    private long bytesWritten = 0;
    private long totalSize = 0;
    private boolean isThread = false;
    private RdfHttpResponseListener responseListener;

    public RdfOutputStreamProgress(OutputStream outputStream, long totalSize , RdfHttpResponseListener responseListener, boolean isThread) {
        this.outputStream = outputStream;
        this.responseListener = responseListener;
        this.totalSize = totalSize;
        this.isThread = isThread;
    }
    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        bytesWritten++;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }

    }
    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        bytesWritten += b.length;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }
    }
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        bytesWritten += len;
        if(isThread){
            responseListener.sendProgressMessage(bytesWritten, totalSize);
        }else{
            responseListener.onProgress(bytesWritten,totalSize);
        }
    }
    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }
    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}