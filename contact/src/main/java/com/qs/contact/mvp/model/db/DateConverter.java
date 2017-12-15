package com.qs.contact.mvp.model.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * @author 华清松
 * @since 日期转换
 */
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
