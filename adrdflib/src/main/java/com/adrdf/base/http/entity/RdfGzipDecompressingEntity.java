package com.adrdf.base.http.entity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Copyright © CapRobin
 *
 * Name：RdfGzipDecompressingEntity
 * Describe：Http解压内容
 * Date：2017-06-27 10:46:30
 * Author: CapRobin@yeah.net
 *
 */
public class RdfGzipDecompressingEntity extends HttpEntityWrapper{
    
    public RdfGzipDecompressingEntity(final HttpEntity entity){
        super(entity);
    }

    public InputStream getContent() throws IOException, IllegalStateException{
        InputStream wrappedin = wrappedEntity.getContent();
        return new GZIPInputStream(wrappedin);
    }

    public long getContentLength(){
        return -1;
    }
}