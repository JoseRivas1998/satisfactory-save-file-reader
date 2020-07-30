package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

public class SaveEntity extends SaveObject {

    public static final int TYPE_ID = 1;

    public final boolean needTransform;
    public final Vector4 rotation;
    public final Vector3 position;
    public final Vector3 scale;
    public final boolean wasPlacedInLevel;

    SaveEntity(FilePointer bodyPointer) {
        super(bodyPointer);
        needTransform = bodyPointer.nextInt() == 1;
        rotation = Vector4.ofFile(bodyPointer);
        position = Vector3.ofFile(bodyPointer);
        scale = Vector3.ofFile(bodyPointer);
        wasPlacedInLevel = bodyPointer.nextInt() == 1;
    }
}
