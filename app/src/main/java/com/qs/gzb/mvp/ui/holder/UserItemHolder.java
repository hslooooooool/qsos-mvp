package com.qs.gzb.mvp.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qs.arm.base.BaseHolder;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.http.imageloader.ImageLoader;
import com.qs.arm.http.imageloader.glide.ImageConfigImpl;
import com.qs.arm.utils.ArmsUtils;
import com.qs.gzb.R;
import com.qs.gzb.mvp.model.entity.User;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 展示 {@link BaseHolder} 的用法
 *
 * @author 华清松
 */
public class UserItemHolder extends BaseHolder<User> {

    @BindView(R.id.iv_avatar)
    ImageView mAvatar;
    @BindView(R.id.tv_name)
    TextView mName;

    private AppComponent mAppComponent;
    private Context context;

    private ImageLoader mImageLoader;

    public UserItemHolder(View itemView) {
        super(itemView);
        // 可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        context = itemView.getContext();
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(context);
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(User data, int position) {
        Observable.just(data.getLogin()).subscribe(s -> mName.setText(s));

        mImageLoader.loadImage(mAppComponent.appManager().getTopActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getTopActivity()
                , ImageConfigImpl.builder()
                        .url(data.getAvatarUrl())
                        .imageView(mAvatar)
                        .build());

        mName.setOnClickListener(view -> {
            Toast.makeText(context, "正在下载..." + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onRelease() {
        super.onRelease();
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder().imageViews(mAvatar).build());
    }
}
