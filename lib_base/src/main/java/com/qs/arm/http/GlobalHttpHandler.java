package com.qs.arm.http;

import com.qs.arm.di.module.ConfigModule;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link ConfigModule.Builder#globalHttpHandler(GlobalHttpHandler)} 方法配置
 *
 * @author 华清松
 */
public interface GlobalHttpHandler {
    /**
     * 请求返回拦截器
     *
     * @param httpResult httpResult
     * @param chain      chain
     * @param response   response
     * @return Response
     */
    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    /**
     * 请求前拦截器
     *
     * @param chain   chain
     * @param request request
     * @return Request
     */
    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    /**
     * 空实现
     */
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            // 不管是否处理,都必须将response返回出去
            return response;
        }

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            // 不管是否处理,都必须将request返回出去
            return request;
        }
    };

}
