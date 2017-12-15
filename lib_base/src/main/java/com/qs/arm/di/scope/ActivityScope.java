package com.qs.arm.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 一个范围的注释，允许对象的生命周期与活动的生命相一致，以便在正确的组件中记住。
 *
 * @author 华清松
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
