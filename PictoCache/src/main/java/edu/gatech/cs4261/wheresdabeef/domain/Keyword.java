package edu.gatech.cs4261.wheresdabeef.domain;

/**
 * Created by Kyle on 11/2/13.
 */
public class Keyword {
    private final int id;

    private String keyword;

    public Keyword(final int id) {
        this(id, null);
    }

    public Keyword(final int id, final String k) {
        this.id = id;
        this.keyword = k;
    }

    public int getId() {
        return this.id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(final String k) {
        this.keyword = k;
    }
}
