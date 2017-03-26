package com.vkb.plivosms.objects;

public class AccountEntity {
    private Integer id;
    private String authId;
    private String userName;

    public AccountEntity(Integer id, String authId, String userName) {
        this.id = id;
        this.authId = authId;
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
