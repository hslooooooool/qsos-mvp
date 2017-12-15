package com.qs.arm.base;
/**
 * @author 华清松
 * @email 821034742@qq.com
 * @weixin hslooooooool
 * @doc 类说明：
 */

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * @author 华清松
 * @since Dao层示例
 */
public interface BaseDao<T> {
    /**
     * 插入
     *
     * @param t T
     */
    @Insert
    void insert(T t);

    /**
     * 删除
     *
     * @param t T
     */
    @Delete
    void delete(T t);

    /**
     * 更新
     *
     * @param t T
     */
    @Update
    void update(T t);

    /**
     * 通过ID查询
     *
     * @param uid uid
     */
    @Query("SELECT * From T where id = :uid")
    void selectById(int uid);
}
