package com.qs.arm.config;

import android.content.Context;

import com.google.gson.GsonBuilder;

/**
 * @author 华清松
 * @email 821034742@qq.com
 * @weixin hslooooooool
 * @doc 类说明：Gson 配置类
 */
public class GsonConfig {

    public interface GsonConfiguration {
        /**
         * 提供接口，自定义配置 RoomDatabase
         *
         * @param context Context
         * @param builder RoomDatabase.Builder
         */
        void configGson(Context context, GsonBuilder builder);

        GsonConfig.GsonConfiguration EMPTY = new GsonConfig.GsonConfiguration() {
            @Override
            public void configGson(Context context, GsonBuilder builder) {

            }
        };
    }
}
