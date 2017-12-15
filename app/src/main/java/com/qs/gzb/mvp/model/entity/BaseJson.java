
package com.qs.gzb.mvp.model.entity;

import java.io.Serializable;

import com.qs.gzb.mvp.model.api.Api;

/**
 * 如果你服务器返回的数据格式固定为这种方式(这里只提供思想,服务器返回的数据格式可能不一致,可根据自家服务器返回的格式作更改)
 * 替换范型即可重用 {@link BaseJson}
 *
 * @author 华清松
 */
public class BaseJson<T> implements Serializable, DataSuccess {

    private String code;
    private String msg;
    private T data;

    BaseJson(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean success() {
        return Api.REQUEST_SUCCESS.equals(code);
    }

    @Override
    public String toString() {
        return "BaseJson{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
