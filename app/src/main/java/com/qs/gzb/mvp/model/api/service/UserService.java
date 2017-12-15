package com.qs.gzb.mvp.model.api.service;

import com.qs.gzb.mvp.model.entity.BaseJson;
import com.qs.gzb.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 *
 * @author 华清松
 */
public interface UserService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    /**
     * 获得用户列表
     *
     * @param lastIdQueried lastIdQueried
     * @param perPage       perPage
     * @return Observable
     */
    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    /**
     * 用户登录
     *
     * @param phone    phone
     * @param password password
     * @return Observable
     */
    @Headers({HEADER_API_VERSION})
    @POST("/login")
    Observable<BaseJson<User>> login(@Query("phone") int phone, @Query("password") String password);

}
