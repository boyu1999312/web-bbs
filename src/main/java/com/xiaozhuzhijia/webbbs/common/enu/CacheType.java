package com.xiaozhuzhijia.webbbs.common.enu;

public enum  CacheType {
    /** 缓存类型 */
    SELECT(1),
    UPDATE(2);
    /** 具体信息 */
    private int id;
    private CacheType(int id){
        this.id = id;
    }
    public boolean isSelect(){return this.id == 1;}
}
