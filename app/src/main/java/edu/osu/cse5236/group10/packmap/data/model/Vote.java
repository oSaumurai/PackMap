package edu.osu.cse5236.group10.packmap.data.model;

public class Vote extends BaseDocument {

    private int value;
    private String accountId;

    public Vote(int value, String accountId) {
        this.value = value;
        this.accountId = accountId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
