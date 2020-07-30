package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

public abstract class SaveObject {

    public final String typePath;
    public final String rootObject;
    public final String instanceName;

    protected SaveObject(FilePointer bodyPointer) {
        this.typePath = bodyPointer.nextStringWithLength();
        this.rootObject = bodyPointer.nextStringWithLength();
        this.instanceName = bodyPointer.nextStringWithLength();
    }



}
