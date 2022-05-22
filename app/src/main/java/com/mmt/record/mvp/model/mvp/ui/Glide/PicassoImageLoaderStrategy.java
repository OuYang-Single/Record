package com.mmt.record.mvp.model.mvp.ui.Glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jess.arms.http.imageloader.BaseImageLoaderStrategy;
import com.jess.arms.http.imageloader.glide.BlurTransformation;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.http.imageloader.glide.GlideRequest;
import com.jess.arms.http.imageloader.glide.GlideRequests;
import com.jess.arms.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class PicassoImageLoaderStrategy implements BaseImageLoaderStrategy<PicassoImageConfig> {
	 @Override
    public void loadImage(Context ctx, PicassoImageConfig config) {
         Preconditions.checkNotNull(ctx, "Context is required");
         Preconditions.checkNotNull(config, "ImageConfigImpl is required");
         Preconditions.checkNotNull(config.getImageView(), "ImageView is required");
         GlideRequests requests = GlideArms.with(ctx);
         GlideRequest<Drawable> glideRequest = requests.load(null==config.getUrl()?config.getUrls():config.getUrl());
         switch(config.getCacheStrategy()) {
             case 0:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                 break;
             case 1:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                 break;
             case 2:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                 break;
             case 3:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                 break;
             case 4:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                 break;
             default:
                 glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
         }

         if (config.isCrossFade()) {
             glideRequest.transition(DrawableTransitionOptions.withCrossFade());
         }

         if (config.isCenterCrop()) {
             glideRequest.centerCrop();
         }

         if (config.isCircle()) {
             glideRequest.circleCrop();
         }

         if (config.isImageRadius()) {
             glideRequest.transform(new RoundedCorners(config.getImageRadius()));
         }

         if (config.isBlurImage()) {
             glideRequest.transform(new BlurTransformation(config.getBlurValue()));
         }

         if (config.getTransformation() != null) {
             glideRequest.transform(config.getTransformation());
         }

         if (config.getPlaceholder() != 0) {
             glideRequest.placeholder(config.getPlaceholder());
         }

         if (config.getErrorPic() != 0) {
             glideRequest.error(config.getErrorPic());
         }

         if (config.getFallback() != 0) {
             glideRequest.fallback(config.getFallback());
         }

         glideRequest.into(config.getImageView());
     }

    @Override
    public void clear(@Nullable Context ctx, @Nullable PicassoImageConfig config) {
        Preconditions.checkNotNull(ctx, "Context is required");
        Preconditions.checkNotNull(config, "ImageConfigImpl is required");
        if (config.getImageView() != null) {
            GlideArms.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.getImageView());
        }

        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            ImageView[] var3 = config.getImageViews();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                ImageView imageView = var3[var5];
                GlideArms.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        if (config.isClearDiskCache()) {
            Completable.fromAction(new Action() {
                public void run() throws Exception {
                    Glide.get(ctx).clearDiskCache();
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }

        if (config.isClearMemory()) {
            Completable.fromAction(new Action() {
                public void run() throws Exception {
                    Glide.get(ctx).clearMemory();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }
}