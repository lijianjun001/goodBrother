package com.nirvana.zmkj.base.holder;

public interface HolderCreator<VH extends IViewHolder> {
    int getLayout();

    VH createViewHolder();
}
