package com.xiaozhuzhijia.webbbs.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {


    private Integer code;
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

    public static Result ok(Object data) {
        return new Result(200, "ok", data);
    }

    public static Result okMsg(String msg) {
        return new Result(200, msg, null);
    }
}
