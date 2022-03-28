package com.example.a7sticker;

public class SentSticker {
    private int stickerId;
    private int count;

    public SentSticker(int stickerId, int count) {
        this.stickerId = stickerId;
        this.count = count;
    }

    public int getStickerId() {
        return stickerId;
    }

    public int getCount() {
        return count;
    }
}
