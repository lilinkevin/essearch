package com.yimayhd.api.vo;

/**
 * PageVo
 *
 * @author lilin
 * @date 16/9/28
 */
public class PageVo {

    private int pageSize;
    private int pageIndex;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public PageVo(int pageSize, int pageIndex) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;


    }
}
