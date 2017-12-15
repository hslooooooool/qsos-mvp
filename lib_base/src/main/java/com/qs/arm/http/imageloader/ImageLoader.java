package com.qs.arm.http.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link ImageLoader} 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 {@link ImageLoader} 是为了屏蔽这个风险
 *
 * @author 华清松
 */
@Singleton
public final class ImageLoader {
    private BaseImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader(BaseImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    /**
     * 加载图片
     *
     * @param context context
     * @param config  config
     * @param <T>     T
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context context
     * @param config  config
     * @param <T>     T
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        mStrategy.clear(context, config);
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy}
     *
     * @param strategy strategy
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }
}
