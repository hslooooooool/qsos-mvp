package com.qs.arm.config;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * @author 华清松
 * @email 821034742@qq.com
 * @weixin hslooooooool
 * @doc 类说明：
 */
public class DatabaseConfig {

    public interface RoomConfiguration<DB extends RoomDatabase> {
        /**
         * 提供接口，自定义配置 RoomDatabase
         *
         * @param context Context
         * @param builder RoomDatabase.Builder
         */
        void configRoom(Context context, RoomDatabase.Builder<DB> builder);

        RoomConfiguration EMPTY = new RoomConfiguration() {
            @Override
            public void configRoom(Context context, RoomDatabase.Builder builder) {

            }
        };
    }
}
