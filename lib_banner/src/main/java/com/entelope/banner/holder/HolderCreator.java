package com.entelope.banner.holder;

public interface HolderCreator<VH extends ViewHolderInterface> {
    VH createViewHolder();
}
