package com.tcg.satisfactorysavefilereader.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ZLIBChunk {

    public final long packageFileTag;
    public final long maximumChunkSize;
    public final long currentChunkCompressedLength;
    public final long currentChunkUncompressedLength;
    private final byte[] compressedData;


    public ZLIBChunk(FilePointer file) {
        packageFileTag = file.nextLong();
        maximumChunkSize = file.nextLong();
        currentChunkCompressedLength = file.nextLong();
        currentChunkUncompressedLength = file.nextLong();
        file.nextBytes(Long.BYTES * 2);
        compressedData = file.nextBytes((int) currentChunkCompressedLength);
    }

    public byte[] decompress() throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

}
