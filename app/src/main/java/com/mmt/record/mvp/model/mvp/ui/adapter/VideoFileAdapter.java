package com.mmt.record.mvp.model.mvp.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deep.dpwork.util.DisplayUtil;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.mmt.record.R;

import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.VideoEntity;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.ui.Glide.PicassoImageConfig;
import com.mmt.record.mvp.model.mvp.util.ViewUtil;

import org.raphets.roundimageview.RoundImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class VideoFileAdapter extends DefaultAdapter<LocalMedia> {
   ImageLoader mImageLoader;
    public VideoFileAdapter(List<LocalMedia> infos, ImageLoader mImageLoader) {
        super(infos);
        this.mImageLoader=mImageLoader;
    }


    @NonNull
    @Override
    public BaseHolder<LocalMedia> getHolder(@NonNull View v, int viewType) {

        return new BaseHolders(v);
    }

    @Override
    public int getLayoutId(int viewType) {

        return R.layout.video_file_item;
    }

    public class BaseHolders extends BaseHolder<LocalMedia> {
        @BindView(R.id.video_img)
        RoundImageView video_img;
        @BindView(R.id.video_news)
        TextView video_news;
        @BindView(R.id.img_type)
        ImageView img_type;
        public BaseHolders(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(@NonNull LocalMedia videoEntity, int i) {
            video_news.setText(videoEntity.getFileName());
           //Glide.with(img_type.getContext()).load().into(video_img);
            ArmsUtils.obtainAppComponentFromContext(img_type.getContext())
                    .imageLoader()
                    .loadImage(img_type.getContext(), PicassoImageConfig
                            .builder()
                            .url(Uri.fromFile( new File( videoEntity.getRealPath()  )))
                            .imageView(video_img)
                            .build());
          //  mImageLoader.loadImage(img_type.getContext(), ImageConfigImpl.builder().imageView(video_img).url("sf").build());
           int h= (DisplayUtil.getMobileWidth(img_type.getContext())- ViewUtil.Dp2Px(img_type.getContext(),10))/4;
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) video_img.getLayoutParams();
            layoutParams.height=h;
            video_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoEntity.getRealPath()), "video/mp4");
                    img_type.getContext().startActivity(intent);
                }
            });
        }


    }




}
