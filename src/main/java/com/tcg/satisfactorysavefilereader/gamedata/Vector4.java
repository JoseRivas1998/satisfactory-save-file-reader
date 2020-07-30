package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

import java.util.Objects;

public class Vector4 {

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    private Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4 vector4 = (Vector4) o;
        return Float.compare(vector4.x, x) == 0 &&
                Float.compare(vector4.y, y) == 0 &&
                Float.compare(vector4.z, z) == 0 &&
                Float.compare(vector4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Vector4{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }

    public static Vector4 ofFile(FilePointer file) {
        final float x = file.nextFloat();
        final float y = file.nextFloat();
        final float z = file.nextFloat();
        final float w = file.nextFloat();
        return new Vector4(x, y, z, w);
    }

}
