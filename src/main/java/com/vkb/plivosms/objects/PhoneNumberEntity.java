package com.vkb.plivosms.objects;

public class PhoneNumberEntity {
    private Integer id;
    private String number;
    private Integer accountId;

    public PhoneNumberEntity(Integer id, String number, Integer accountId) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
