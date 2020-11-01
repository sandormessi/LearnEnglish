package com.example.learnenglish;

public class State
{
    private int wordCount;
    private int tipCount;
    private String actualWord;

    public int getTipCount() {
        return tipCount;
    }

    public void setTipCount(int tipCount) {
        this.tipCount = tipCount;
    }

    public String getActualWord() {
        return actualWord;
    }

    public void setActualWord(String actualWord) {
        this.actualWord = actualWord;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
