package com.tcg.satisfactorysavefilereader.io;

import com.tcg.satisfactorysavefilereader.gamedata.SaveObject;
import com.tcg.satisfactorysavefilereader.gamedata.SaveObjectBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class SatisfactorySaveFile {

    private final FilePointer filePointer;
    private final FilePointer bodyPointer;
    private final MetaData metaData;

    public final SaveObject[] saveObjects;

    private SatisfactorySaveFile(byte[] file) {
        filePointer = FilePointer.of(file);
        metaData = new MetaData();
        BodyData bodyData = new BodyData();
        bodyPointer = FilePointer.of(bodyData.body);
        int dataLength = bodyPointer.nextInt();
        int numWorldObjects = bodyPointer.nextInt();
        saveObjects = parseSaveObjects(numWorldObjects);
        int propertyCount = bodyPointer.nextInt();
        int len = bodyPointer.nextInt();
    }

    private SaveObject[] parseSaveObjects(int numWorldObjects) {
        final SaveObject[] saveObjects;
        saveObjects = new SaveObject[numWorldObjects];
        for (int i = 0; i < numWorldObjects; i++) {
            saveObjects[i] = SaveObjectBuilder.build(bodyPointer);
        }
        return saveObjects;
    }

    public static SatisfactorySaveFile open(InputStream file) throws IOException {
        return new SatisfactorySaveFile(IOUtils.toByteArray(file));
    }

    public int fileLength() {
        return filePointer.length();
    }

    public int saveHeaderVersion() {
        return metaData.saveHeaderVersion;
    }

    public int saveVersion() {
        return metaData.saveVersion;
    }

    public int buildVersion() {
        return metaData.buildVersion;
    }

    public String worldType() {
        return metaData.worldType;
    }

    public String worldProperties() {
        return metaData.worldProperties;
    }

    public String sessionName() {
        return metaData.sessionName;
    }

    public int playTimeSeconds() {
        return metaData.playTimeSeconds;
    }

    public long saveDate() {
        return metaData.saveDate;
    }

    public byte sessionVisibility() {
        return metaData.sessionVisibility;
    }

    private class MetaData {
        private final int saveHeaderVersion;
        private final int saveVersion;
        private final int buildVersion;
        private final String worldType;
        private final String worldProperties;
        private final String sessionName;
        private final int playTimeSeconds;
        private final long saveDate;
        private final byte sessionVisibility;

        private MetaData() {
            saveHeaderVersion = filePointer.nextInt();
            saveVersion = filePointer.nextInt();
            buildVersion = filePointer.nextInt();

            final int worldTypeLength = filePointer.nextInt();
            worldType = filePointer.nextString(worldTypeLength);

            final int worldPropertiesLength = filePointer.nextInt();
            worldProperties = filePointer.nextString(worldPropertiesLength);

            final int sessionNameLength = filePointer.nextInt();
            sessionName = filePointer.nextString(sessionNameLength);

            playTimeSeconds = filePointer.nextInt();
            saveDate = filePointer.nextLong();
            sessionVisibility = filePointer.nextByte();
        }
    }

    private class BodyData {
        private final List<ZLIBChunk> chunks;
        private final byte[] body;

        private BodyData() {
            chunks = readAllChunks();
            body = decompressAllChunks();
        }

        private byte[] decompressAllChunks() {
            List<byte[]> decompressedChunks = decompressChunksToListOfByteArrays();
            int length = totalBodyLength(decompressedChunks);
            return mergeListOfByteArraysToSingleByteArray(decompressedChunks, length);
        }

        private byte[] mergeListOfByteArraysToSingleByteArray(List<byte[]> decompressedChunks, int length) {
            final byte[] body;
            body = new byte[length];
            int indexInBody = 0;
            for (byte[] decompressedChunk : decompressedChunks) {
                for (byte b : decompressedChunk) {
                    body[indexInBody++] = b;
                }
            }
            return body;
        }

        private int totalBodyLength(List<byte[]> decompressedChunks) {
            return decompressedChunks.stream()
                    .mapToInt(chunk -> chunk.length)
                    .sum();
        }

        private List<byte[]> decompressChunksToListOfByteArrays() {
            return chunks.stream()
                    .map(this::decompressChunk)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }


        private List<ZLIBChunk> readAllChunks() {
            final List<ZLIBChunk> chunks;
            chunks = new ArrayList<>();
            while (!filePointer.endOfFile()) {
                chunks.add(new ZLIBChunk(filePointer));
            }
            return chunks;
        }

        private byte[] decompressChunk(ZLIBChunk chunk) {
            try {
                return chunk.decompress();
            } catch (IOException | DataFormatException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
