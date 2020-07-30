package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

public class SaveObjectBuilder {

    public static SaveObject build(FilePointer bodyPointer) {
        final int type = bodyPointer.nextInt();
        switch (type) {
            case SaveEntity.TYPE_ID:
                return new SaveEntity(bodyPointer);
            case SaveComponent.TYPE_ID:
                return new SaveComponent(bodyPointer);
        }
        return null;
    }

}
