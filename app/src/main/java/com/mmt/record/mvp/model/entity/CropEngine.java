package com.mmt.record.mvp.model.entity;


import androidx.fragment.app.Fragment;


import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;


@Deprecated
public interface CropEngine {


    void onStartCrop(Fragment fragment, LocalMedia currentLocalMedia, ArrayList<LocalMedia> dataSource, int requestCode);

}
