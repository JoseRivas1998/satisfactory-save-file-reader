package com.tcg.satisfactorysavefilereader.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

public class SatisfactorySaveFileTest {

    private static final String FILE_NAME = "First Game_0906-133956.sav";
    private static final int FILE_LENGTH = 497941;
    private InputStream file;
    private SatisfactorySaveFile satisfactorySaveFile;

    @Before
    public void setup() throws IOException {
        file = openFile();
        satisfactorySaveFile = SatisfactorySaveFile.open(file);
    }

    @After
    public void teardown() throws IOException {
        if (file != null) file.close();
    }

    @Test
    public void canCreateSatisfactorySaveFile() {
        assertNotNull(satisfactorySaveFile);
    }

    @Test
    public void fileLength() {
        int actual = satisfactorySaveFile.fileLength();
        assertEquals(FILE_LENGTH, actual);
    }

    @Test
    public void saveHeaderVersionShouldBe6() {
        final int expected = 6;
        int actual = satisfactorySaveFile.saveHeaderVersion();
        assertEquals(expected, actual);
    }

    @Test
    public void saveVersionShouldBe25() {
        final int expected = 25;
        int actual = satisfactorySaveFile.saveVersion();
        assertEquals(expected, actual);
    }

    @Test
    public void buildVersionShouldBe124233() {
        final int expected = 124233;
        int actual = satisfactorySaveFile.buildVersion();
        assertEquals(expected, actual);
    }

    @Test
    public void worldTypeShouldBePersistentLevel() {
        final String expected = "Persistent_Level";
        String actual = satisfactorySaveFile.worldType();
        assertEquals(expected, actual);
    }

    @Test
    public void sessionNameShouldBeFirstGame() {
        final String expected = "First Game";
        String actual = satisfactorySaveFile.sessionName();
        assertEquals(expected, actual);
    }

    @Test
    public void testWorldProperties() {
        final String expected = "?startloc=Grass Fields?sessionName=First Game?Visibility=SV_Private";
        String actual = satisfactorySaveFile.worldProperties();
        assertEquals(expected, actual);
    }

    @Test
    public void playTimeShouldBe4731() {
        final int expected = 4731;
        int actual = satisfactorySaveFile.playTimeSeconds();
        assertEquals(expected, actual);
    }

    @Test
    public void saveDateShouldBe637273319968730000() {
        final long expected = 637273319968730000L;
        long actual = satisfactorySaveFile.saveDate();
        assertEquals(expected, actual);
    }

    @Test
    public void sessionVisibilityShouldBeZero() {
        final byte expected = 0;
        byte actual = satisfactorySaveFile.sessionVisibility();
        assertEquals(expected, actual);
    }

    @Test
    public void has13927WorldObjects() {
        final int expected = 13927;
        int actual = satisfactorySaveFile.saveObjects.length;
        assertEquals(expected, actual);
    }

    private InputStream openFile() {
        return getClass().getClassLoader().getResourceAsStream(FILE_NAME);
    }

}
