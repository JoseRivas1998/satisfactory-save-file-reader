package com.tcg.satisfactorysavefilereader.io;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class FilePointerTest {

    @Test
    public void createFilePointer() {
        FilePointer filePtr = FilePointer.of(new byte[0]);
        assertNotNull(filePtr);
    }

    @Test
    public void fileLength() {
        String file = "Hello world";
        FilePointer filePtr = createStringFile(file);
        final int expected = file.length();
        final int actual = filePtr.length();
        assertEquals(expected, actual);
    }

    @Test
    public void readNextString() {
        String expected = "Hello world!";
        FilePointer filePtr = createStringFile(expected);
        String actual = filePtr.nextString(expected.length());
        assertEquals(expected, actual);
    }

    @Test
    public void readNullTerminatedString() {
        byte[] bytes = {'H', 'e', 'l', 'l', 'o', 0};
        FilePointer filePointer = FilePointer.of(bytes);
        String expected = "Hello";
        String actual = filePointer.nextString(bytes.length);
        assertEquals(expected, actual);
    }

    @Test
    public void readTwoStrings() {
        FilePointer filePtr = createStringFile("Hello world!");

        String expected1 = "Hello";
        String actual1 = filePtr.nextString(expected1.length());
        assertEquals(expected1, actual1);

        String expected2 = " world!";
        String actual2 = filePtr.nextString(expected2.length());
        assertEquals(expected2, actual2);
    }

    @Test(expected = FilePointer.FilePointerReadOutOfRangeException.class)
    public void readToCompletionAndThenTryToReadAgain() {
        String file = "Hello world!";
        FilePointer filePtr = createStringFile(file);
        filePtr.nextString(file.length());
        filePtr.nextString(1);
    }

    @Test(expected = FilePointer.FilePointerReadOutOfRangeException.class)
    public void tryToReadTooMuch() {
        String file = "Hello world!";
        FilePointer filePtr = createStringFile(file);
        filePtr.nextString(100);
    }

    @Test
    public void readInt() {
        final int expected = 123456789;
        FilePointer filePtr = littleEndianIntFilePtr(expected);
        final int actual = filePtr.nextInt();
        assertEquals(expected, actual);
    }

    @Test
    public void readLong() {
        final long expected = Integer.MAX_VALUE + 100L;
        FilePointer filePtr = littleEndianLongFilePointer(expected);
        final long actual = filePtr.nextLong();
        assertEquals(expected, actual);
    }

    @Test
    public void readByte() {
        final byte expected1 = 1;
        final byte expected2 = 2;
        FilePointer ptr = FilePointer.of(new byte[]{expected1, expected2});

        final byte actual1 = ptr.nextByte();
        assertEquals(expected1, actual1);

        final byte actual2 = ptr.nextByte();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testEndOfFile() {
        String file = "Hello world";
        FilePointer filePtr = createStringFile(file);
        assertFalse(filePtr.endOfFile());
        filePtr.nextString(file.length() - 3);
        assertFalse(filePtr.endOfFile());
        filePtr.nextString(3);
        assertTrue(filePtr.endOfFile());
    }

    @Test
    public void nextNullTerminatedStringLength() {
        byte[] file = {13, 0, 0, 0, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', 0};
        FilePointer filePtr = FilePointer.of(file);
        String expected = "Hello World!";
        String actual = filePtr.nextStringWithLength();
        assertEquals(expected, actual);
    }

    @Test
    public void nextFloat() {
        byte[] file = {0, 0, -128, 63};
        FilePointer filePtr = FilePointer.of(file);
        final float expected = 1.0f;
        final float actual = filePtr.nextFloat();
        assertEquals(expected, actual, 1e-5);
    }

    private FilePointer createStringFile(String file) {
        return FilePointer.of(file.getBytes());
    }

    private FilePointer littleEndianIntFilePtr(int expected) {
        final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(expected);
        return FilePointer.of(bb.array());
    }

    private FilePointer littleEndianLongFilePointer(long value) {
        final ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(value);
        return FilePointer.of(bb.array());
    }

}
