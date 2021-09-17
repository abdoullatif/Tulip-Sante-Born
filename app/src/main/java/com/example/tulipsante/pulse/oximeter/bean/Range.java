package com.example.tulipsante.pulse.oximeter.bean;

public class Range {

    public int from;
    public int to;

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean contains(int value) {

        if (value > from && value <= to)
            return true;
        else
            return false;
    }

    public boolean isLarger(int value) {
        return value > to;
    }

    public boolean isSmaller(int value) {
        return value < from;
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
