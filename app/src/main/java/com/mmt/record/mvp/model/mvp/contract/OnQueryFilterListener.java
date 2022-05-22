package com.mmt.record.mvp.model.mvp.contract;

/**
 * @author：luck
 * @date：2022/3/12 10:09 上午
 * @describe：OnQueryFilterListener
 */
public interface OnQueryFilterListener {
    /**
     * You need to filter out what doesn't meet the standards
     *
     * @return the boolean result
     */
    boolean onFilter(String absolutePath);
}
