package com.tcg.satisfactorysavefilereader.gamedata;

import com.tcg.satisfactorysavefilereader.io.FilePointer;

import java.util.Objects;

public class Vector3 {

    public final float x;
    public final float y;
    public final float z;

    private Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector4 = (Vector3) o;
        return Float.compare(vector4.x, x) == 0 &&
                Float.compare(vector4.y, y) == 0 &&
                Float.compare(vector4.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static Vector3 ofFile(FilePointer file) {
        final float x = file.nextFloat();
        final float y = file.nextFloat();
        final float z = file.nextFloat();
        return new Vector3(x, y, z);
    }

}
