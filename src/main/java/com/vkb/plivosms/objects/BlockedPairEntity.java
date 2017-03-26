package com.vkb.plivosms.objects;

public class BlockedPairEntity {
    private Integer id;
    private String from;
    private String to;
    private Long setAtTime;

    public BlockedPairEntity(Integer id, String from, String to, Long setAtTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.setAtTime = setAtTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getSetAtTime() {
        return setAtTime;
    }

    public void setSetAtTime(Long setAtTime) {
        this.setAtTime = setAtTime;
    }
}
