package com.mmt.record.mvp.model.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deep.dpwork.util.DisplayUtil;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.mmt.record.R;

import com.mmt.record.mvp.model.entity.VideoEntity;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.util.ViewUtil;

import org.raphets.roundimageview.RoundImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class VideoFileAdapter extends DefaultAdapter<VideoInfo> {

    public VideoFileAdapter(List<VideoInfo> infos) {
        super(infos);
    }


    @NonNull
    @Override
    public BaseHolder<VideoInfo> getHolder(@NonNull View v, int viewType) {

        return new BaseHolders(v);
    }

    @Override
    public int getLayoutId(int viewType) {

        return R.layout.video_file_item;
    }

    public class BaseHolders extends BaseHolder<VideoInfo> {
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
        public void setData(@NonNull VideoInfo videoEntity, int i) {
           int h= (DisplayUtil.getMobileWidth(img_type.getContext())- ViewUtil.Dp2Px(img_type.getContext(),10))/4;
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) video_img.getLayoutParams();
            layoutParams.height=h;
        }


    }




}
