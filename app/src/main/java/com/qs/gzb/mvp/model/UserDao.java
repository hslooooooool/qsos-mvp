package com.qs.gzb.mvp.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.qs.arm.base.BaseDao;
import com.qs.gzb.mvp.model.entity.User;

import java.util.List;

/**
 * @author 华清松
 * @see BaseDao
 * @since 会员数据库操作层
 */
@Dao
public interface UserDao {

    /**
     * 添加一个会员
     *
     * @param user user
     */
    @Insert
    void insert(User user);

    /**
     * 添加一组会员
     *
     * @param users User...
     */
    @Insert
    void insertAll(User... users);

    /**
     * 删除会员
     *
     * @param user User
     */
    @Delete
    void delete(User user);

    /**
     * 获取所有用户
     *
     * @return List<User>
     */
    @Query("SELECT * FROM user")
    List<User> getAll();

    /**
     * 通过一组会员ID查询一组会员
     *
     * @param userIds int[]
     * @return List<User>
     */
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    /**
     * 通过姓名查询会员
     *
     * @param first 姓
     * @param last  名
     * @return User
     */
    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    /**
     * 通过电话查询会员
     *
     * @param phone 电话
     * @return User
     */
    @Query("SELECT * FROM user WHERE phone = :phone LIMIT 1")
    User checkNullByPhone(int phone);

    /**
     * 通过电话和密码查询会员
     *
     * @param phone    电话
     * @param password 密码
     * @return User
     */
    @Query("SELECT * FROM user WHERE phone = :phone AND password = :password LIMIT 1")
    User login(int phone, String password);

}

