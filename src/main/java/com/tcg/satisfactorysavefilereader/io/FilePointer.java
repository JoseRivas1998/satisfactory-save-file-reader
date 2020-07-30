package com.tcg.satisfactorysavefilereader.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class FilePointer {

    private final byte[] bytes;
    private int pointer;

    private FilePointer(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
        this.pointer = 0;
    }

    public static FilePointer of(byte[] bytes) {
        return new FilePointer(bytes);
    }

    public String nextString(int length) {
        byte[] nextBytes = nextBytes(length);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = nextBytes[i];
            if (!(i == length - 1 && b == 0)) result.append((char) b);
        }
        return result.toString();
    }

    public int nextInt() {
        byte[] nextBytes = nextBytes(Integer.SIZE / Byte.SIZE);
        final ByteBuffer bb = ByteBuffer.wrap(nextBytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    public long nextLong() {
        byte[] nextBytes = nextBytes(Long.BYTES);
        final ByteBuffer bb = ByteBuffer.wrap(nextBytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getLong();
    }

    public byte nextByte() {
        byte[] nextBytes = nextBytes(1);
        return nextBytes[0];
    }

    public String nextStringWithLength() {
        final int length = nextInt();
        return nextString(length);
    }

    public float nextFloat() {
        byte[] nextBytes = nextBytes(Float.BYTES);
        final ByteBuffer bb = ByteBuffer.wrap(nextBytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getFloat();
    }

    public byte[] nextBytes(int length) {
        if (pointer + length > this.bytes.length) throw new FilePointerReadOutOfRangeException();
        byte[] nextBytes = Arrays.copyOfRange(this.bytes, pointer, pointer + length);
        pointer += length;
        return nextBytes;
    }

    public int length() {
        return this.bytes.length;
    }

    public boolean endOfFile() {
        return pointer >= this.bytes.length;
    }

    public static class FilePointerReadOutOfRangeException extends RuntimeException {

    }

}
