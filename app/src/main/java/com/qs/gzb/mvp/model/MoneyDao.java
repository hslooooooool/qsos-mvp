package com.qs.gzb.mvp.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.qs.arm.base.BaseDao;

import java.util.List;

import com.qs.gzb.mvp.model.entity.Money;

/**
 * @author 华清松
 * @see BaseDao
 * @since 金钱数据库操作层
 */
@Dao
public interface MoneyDao {

    /**
     * 添加一组金钱
     *
     * @param monies Money...
     */
    @Insert
    void insertAll(Money... monies);

    /**
     * 删除金钱
     *
     * @param money Money
     */
    @Delete
    void delete(Money money);

    /**
     * 获取所有金钱
     *
     * @return List<Money>
     */
    @Query("SELECT * FROM money")
    List<Money> getAll();

    /**
     * 通过一组会员ID查询一组会员
     *
     * @param monies int[]
     * @return List<Money>
     */
    @Query("SELECT * FROM money WHERE mid IN (:monies)")
    List<Money> loadAllByIds(int[] monies);

}
