//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mmt.record.mvp.model.mvp.ui.Glide;

import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jess.arms.http.imageloader.ImageConfig;

import java.net.URL;

public class PicassoImageConfig extends ImageConfig {
    private int cacheStrategy;
    private int fallback;
    private int imageRadius;
    private int blurValue;
    private Uri urls;
    /** @deprecated */
    @Deprecated
    private BitmapTransformation transformation;
    private ImageView[] imageViews;
    private boolean isCrossFade;
    private boolean isCenterCrop;
    private boolean isCircle;
    private boolean isClearMemory;
    private boolean isClearDiskCache;

    private PicassoImageConfig(PicassoImageConfig.Builder builder) {
        this.url = builder.url;
        this.urls = builder.urls;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.fallback = builder.fallback;
        this.cacheStrategy = builder.cacheStrategy;
        this.imageRadius = builder.imageRadius;
        this.blurValue = builder.blurValue;
        this.transformation = builder.transformation;
        this.imageViews = builder.imageViews;
        this.isCrossFade = builder.isCrossFade;
        this.isCenterCrop = builder.isCenterCrop;
        this.isCircle = builder.isCircle;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public int getCacheStrategy() {
        return this.cacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return this.transformation;
    }

    public ImageView[] getImageViews() {
        return this.imageViews;
    }

    public boolean isClearMemory() {
        return this.isClearMemory;
    }

    public boolean isClearDiskCache() {
        return this.isClearDiskCache;
    }

    public int getFallback() {
        return this.fallback;
    }

    public int getBlurValue() {
        return this.blurValue;
    }

    public boolean isBlurImage() {
        return this.blurValue > 0;
    }

    public int getImageRadius() {
        return this.imageRadius;
    }

    public boolean isImageRadius() {
        return this.imageRadius > 0;
    }

    public boolean isCrossFade() {
        return this.isCrossFade;
    }

    public boolean isCenterCrop() {
        return this.isCenterCrop;
    }

    public boolean isCircle() {
        return this.isCircle;
    }

    public static PicassoImageConfig.Builder builder() {
        return new PicassoImageConfig.Builder();
    }

    public Uri getUrls() {
        return urls;
    }

    public static final class Builder {
        private String url;
        private Uri urls ;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int fallback;
        private int cacheStrategy;
        private int imageRadius;
        private int blurValue;
        /** @deprecated */
        @Deprecated
        private BitmapTransformation transformation;
        private ImageView[] imageViews;
        private boolean isCrossFade;
        private boolean isCenterCrop;
        private boolean isCircle;
        private boolean isClearMemory;
        private boolean isClearDiskCache;

        private Builder() {
        }

        public PicassoImageConfig.Builder url(String url) {
            this.url = url;
            return this;
        }
        public PicassoImageConfig.Builder url(Uri urls) {
            this.urls = urls;
            return this;
        }

        public PicassoImageConfig.Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public PicassoImageConfig.Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public PicassoImageConfig.Builder fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public PicassoImageConfig.Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public PicassoImageConfig.Builder cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public PicassoImageConfig.Builder imageRadius(int imageRadius) {
            this.imageRadius = imageRadius;
            return this;
        }

        public PicassoImageConfig.Builder blurValue(int blurValue) {
            this.blurValue = blurValue;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public PicassoImageConfig.Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public PicassoImageConfig.Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public PicassoImageConfig.Builder isCrossFade(boolean isCrossFade) {
            this.isCrossFade = isCrossFade;
            return this;
        }

        public PicassoImageConfig.Builder isCenterCrop(boolean isCenterCrop) {
            this.isCenterCrop = isCenterCrop;
            return this;
        }

        public PicassoImageConfig.Builder isCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        public PicassoImageConfig.Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public PicassoImageConfig.Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public PicassoImageConfig build() {
            return new PicassoImageConfig(this);
        }
    }
}
