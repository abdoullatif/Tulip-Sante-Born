package com.example.tulipsante.utils;

public class Range {
    public int from;

    public int to;

    public Range(int paramInt1, int paramInt2) {
        this.from = paramInt1;
        this.to = paramInt2;
    }

    public boolean contains(int paramInt) {
        return (paramInt > this.from && paramInt <= this.to);
    }

    public boolean isLarger(int paramInt) {
        return (paramInt > this.to);
    }

    public boolean isSmaller(int paramInt) {
        return (paramInt < this.from);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.from);
        stringBuilder.append("-");
        stringBuilder.append(this.to);
        return stringBuilder.toString();
    }
}