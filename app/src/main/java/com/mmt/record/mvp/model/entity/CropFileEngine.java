package com.mmt.record.mvp.model.entity;


import android.net.Uri;

import androidx.fragment.app.Fragment;


import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;


public interface CropFileEngine {


    void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode);

}
