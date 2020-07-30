package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

public class SaveComponent extends SaveObject {

    public static final int TYPE_ID = 0;

    public final String ParentEntityName;

    protected SaveComponent(FilePointer bodyPointer) {
        super(bodyPointer);
        ParentEntityName = bodyPointer.nextStringWithLength();
    }
}
