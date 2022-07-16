package com.mmt.record.mvp.model.mvp.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.deep.dpwork.util.DisplayUtil;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.mmt.record.R;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.LocalMediaFolder;
import com.mmt.record.mvp.model.mvp.ui.Glide.PicassoImageConfig;
import com.mmt.record.mvp.model.mvp.util.ViewUtil;

import org.raphets.roundimageview.RoundImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class VideoFileAdapters extends DefaultAdapter<LocalMediaFolder> {
   ImageLoader mImageLoader;
    public VideoFileAdapters(List<LocalMediaFolder> infos, ImageLoader mImageLoader) {
        super(infos);
        this.mImageLoader=mImageLoader;
    }


    @NonNull
    @Override
    public BaseHolder<LocalMediaFolder> getHolder(@NonNull View v, int viewType) {

        return new BaseHolders(v);
    }

    @Override
    public int getLayoutId(int viewType) {

        return R.layout.video_files_item;
    }

    public class BaseHolders extends BaseHolder<LocalMediaFolder> {

        @BindView(R.id.file_name)
        TextView file_name;

        public BaseHolders(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(@NonNull LocalMediaFolder videoEntity, int i) {
            file_name.setText(videoEntity.getFolderName());

        }


    }




}
