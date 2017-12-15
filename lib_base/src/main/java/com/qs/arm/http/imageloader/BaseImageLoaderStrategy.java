package com.qs.arm.http.imageloader;

import android.content.Context;

/**
 * 图片加载策略,实现 {@link BaseImageLoaderStrategy}
 * 并通过 {@link ImageLoader#setLoadImgStrategy(BaseImageLoaderStrategy)} 配置后,才可进行图片请求
 *
 * @author 华清松
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    /**
     * 加载图片
     *
     * @param context context
     * @param config  config
     */
    void loadImage(Context context, T config);

    /**
     * 停止加载
     *
     * @param context context
     * @param config  config
     */
    void clear(Context context, T config);
}
