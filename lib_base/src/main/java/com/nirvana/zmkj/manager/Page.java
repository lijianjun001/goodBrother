package com.nirvana.zmkj.manager;

public class Page {

    public static int PAGE_INIT=1;
    private int pageIndex = PAGE_INIT;
    private int totalPage;

    public Page(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean canLoad() {
        if (pageIndex <= totalPage) {
            return true;
        }
        return false;
    }

    public void loadNextPage(OnLoadListener onLoadListener) {
        if (canLoad()){
            pageIndex++;
            if (onLoadListener!=null){
                onLoadListener.OnLoad();
            }
        }
    }
    public boolean hasNextPage(){
        if (pageIndex < totalPage) {
            return true;
        }
        return false;
    }
    public interface OnLoadListener{
        void OnLoad();
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
