package com.mmt.record.mvp.model.mvp.util;

import androidx.annotation.IntDef;

@IntDef({ToolbarMode.NORMAL, ToolbarMode.SEARCH, ToolbarMode.TITLE})
public @interface ToolbarMode {
        int NORMAL=1;
        int SEARCH=2;
        int TITLE=3;
}
