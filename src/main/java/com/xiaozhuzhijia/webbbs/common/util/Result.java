package com.xiaozhuzhijia.webbbs.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;


    public static Result ok() {
        return new Result(200, "ok", null);
    }

    public static Result error(String msg) {
        return new Result(404, msg, null);
    }

    public static Result error() {
        return new Result(404, null, null);
    }

    public static Result ok(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result ok(String msg) {
        return new Result(200, msg, null);
    }
}
