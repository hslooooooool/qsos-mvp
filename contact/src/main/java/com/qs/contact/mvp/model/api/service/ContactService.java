package com.qs.contact.mvp.model.api.service;

import com.qs.contact.mvp.model.entity.ContactGroup;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 *
 * @author 华清松
 */
public interface ContactService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    /**
     * 获得用户列表
     *
     * @return Observable
     */
    @Headers({HEADER_API_VERSION})
    @GET("/contact/MAIN")
    Observable<List<ContactGroup>> getContactGroup();

}
