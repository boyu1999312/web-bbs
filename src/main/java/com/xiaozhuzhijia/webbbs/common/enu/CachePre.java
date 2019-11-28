package com.xiaozhuzhijia.webbbs.common.enu;

public enum CachePre {
    /** 卡片缓存名称 */
    CARD_CACHE(",Card"),
    FRIEND_NOTICE(",friendNotice");
    /** 具体信息 */
    private String name;
    private CachePre(String name){
        this.name = name;
    }
    public String getName(){return this.name;}
}
