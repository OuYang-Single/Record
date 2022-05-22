package com.mmt.record.mvp.model.mvp.contract;

import java.util.List;


public interface OnQueryAllAlbumListener<T> {

    void onComplete(List<T> result);
}
