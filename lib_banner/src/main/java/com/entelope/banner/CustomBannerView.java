package com.entelope.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.entelope.banner.holder.HolderCreator;
import com.entelope.banner.holder.ViewHolderInterface;
import com.entelope.banner.transformer.CoverModeTransformer;
import com.entelope.banner.transformer.ScaleTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CustomBannerView<T> extends RelativeLayout {
    private static final String TAG = "MyBannerView";
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private List<T> mDatas;
    private boolean mIsAutoPlay = false;
    private int mCurrentItem = 0;
    private Handler mHandler = new Handler();
    private int mDelayedTime = 3000;
    private ViewPagerScroller mViewPagerScroller;
    private boolean mIsOpenScale = true;
    private boolean mIsCanLoop = true;
    private LinearLayout mIndicatorContainer;
    private ArrayList<ImageView> mIndicators = new ArrayList();
    private int[] mIndicatorRes;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private BannerPageClickListener mBannerPageClickListener;
    private boolean mIsMiddlePageCover;
    private Runnable mLoopRunnable;

    public CustomBannerView(@NonNull Context context) {
        super(context);
        initProperties();
        this.init();
    }

    public CustomBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initProperties();
        this.readAttrs(context, attrs);
        this.init();
    }


    public void initProperties() {
        this.mIndicatorRes = new int[]{R.drawable.indicator_normal, R.drawable.indicator_selected};
        this.mIsMiddlePageCover = true;
        this.mLoopRunnable = new Runnable() {
            public void run() {
                if (CustomBannerView.this.mIsAutoPlay) {
                    CustomBannerView.this.mCurrentItem = CustomBannerView.this.mViewPager.getCurrentItem();
                    CustomBannerView.this.mCurrentItem++;
                    if (CustomBannerView.this.mCurrentItem == CustomBannerView.this.mAdapter.getCount() - 1) {
                        CustomBannerView.this.mCurrentItem = 0;
                        CustomBannerView.this.mViewPager.setCurrentItem(CustomBannerView.this.mCurrentItem, false);
                        CustomBannerView.this.mHandler.postDelayed(this, (long) CustomBannerView.this.mDelayedTime);
                    } else {
                        CustomBannerView.this.mViewPager.setCurrentItem(CustomBannerView.this.mCurrentItem);
                        CustomBannerView.this.mHandler.postDelayed(this, (long) CustomBannerView.this.mDelayedTime);
                    }
                }
            }
        };
    }

    public CustomBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperties();
        this.readAttrs(context, attrs);
        this.init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProperties();
        this.readAttrs(context, attrs);
        this.init();
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        this.mIsOpenScale = typedArray.getBoolean(R.styleable.BannerView_open_scale_mode, true);
        this.mIsMiddlePageCover = typedArray.getBoolean(R.styleable.BannerView_middle_page_cover, true);
        this.mIsCanLoop = typedArray.getBoolean(R.styleable.BannerView_canLoop, true);
        this.mIsAutoPlay = typedArray.getBoolean(R.styleable.BannerView_autoPlay, false);
        typedArray.recycle();
    }

    private void init() {
        View view;
        if (this.mIsOpenScale) {
            view = LayoutInflater.from(this.getContext()).inflate(R.layout.banner_scale_layout, this, true);
        } else {
            view = LayoutInflater.from(this.getContext()).inflate(R.layout.banner_normal_layout, this, true);
        }

        this.mIndicatorContainer = view.findViewById(R.id.banner_indicator_container);
        this.mViewPager = view.findViewById(R.id.viewPager);
        this.initViewPagerScroll();
    }

    private void setOpenScale() {
        if (this.mIsOpenScale) {
            if (this.mIsMiddlePageCover) {
                this.mViewPager.setPageTransformer(true, new CoverModeTransformer(this.mViewPager));
            } else {
                this.mViewPager.setPageTransformer(false, new ScaleTransformer(0.88f));
            }
        }

    }

    private void initViewPagerScroll() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            this.mViewPagerScroller = new ViewPagerScroller(this.mViewPager.getContext());
            mScroller.set(this.mViewPager, this.mViewPagerScroller);
        } catch (NoSuchFieldException var2) {
            var2.printStackTrace();
        } catch (IllegalArgumentException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }

    }

    private void initIndicator() {
        this.mIndicatorContainer.removeAllViews();
        this.mIndicators.clear();

        for (int i = 0; i < this.mDatas.size(); ++i) {
            ImageView imageView = new ImageView(this.getContext());
            imageView.setPadding(6, 0, 6, 0);
            if (i == this.mCurrentItem % this.mDatas.size()) {
                imageView.setImageResource(this.mIndicatorRes[1]);
            } else {
                imageView.setImageResource(this.mIndicatorRes[0]);
            }
            this.mIndicators.add(imageView);
            this.mIndicatorContainer.addView(imageView);
        }

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!this.mIsCanLoop) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    this.pause();
                    int paddingLeft = this.mViewPager.getLeft();
                    float touchX = ev.getRawX();
                    if (touchX >= (float) paddingLeft && touchX < (float) (getScreenWidth(this.getContext()) - paddingLeft)) {
                        this.pause();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    this.start();

            }
            return super.dispatchTouchEvent(ev);
        }
    }

    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    public void start() {
        if (this.mAdapter != null) {
            if (this.mIsCanLoop) {
                this.pause();
                this.mHandler.postDelayed(this.mLoopRunnable, (long) this.mDelayedTime);
            }

        }
    }

    public void pause() {
        this.mHandler.removeCallbacks(this.mLoopRunnable);
    }

    public void setAutoPlay(boolean mIsAutoPlay) {
        this.mIsAutoPlay = mIsAutoPlay;
    }

    public void setCanLoop(boolean canLoop) {
        this.mIsCanLoop = canLoop;
        if (!canLoop) {
            this.pause();
        }

    }

    public void setDelayedTime(int delayedTime) {
        this.mDelayedTime = delayedTime;
    }

    public void addPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setBannerPageClickListener(CustomBannerView.BannerPageClickListener bannerPageClickListener) {
        this.mBannerPageClickListener = bannerPageClickListener;
    }

    public void setIndicatorVisible(boolean visible) {
        if (visible) {
            this.mIndicatorContainer.setVisibility(View.VISIBLE);
        } else {
            this.mIndicatorContainer.setVisibility(View.GONE);
        }

    }

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    public void setIndicatorRes(@DrawableRes int unSelectRes, @DrawableRes int selectRes) {
        this.mIndicatorRes[0] = unSelectRes;
        this.mIndicatorRes[1] = selectRes;
    }

    public void setPages(List<T> datas, HolderCreator holderCreator) {
        if (datas != null && holderCreator != null) {
            this.mDatas = datas;
            this.pause();
            this.setOpenScale();
            this.initIndicator();
            this.mAdapter = new MyPagerAdapter(datas, holderCreator, this.mIsCanLoop);
            this.mAdapter.setUpViewViewPager(this.mViewPager);
            this.mAdapter.setPageClickListener(this.mBannerPageClickListener);
            this.mViewPager.clearOnPageChangeListeners();
            this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int realPosition = position % CustomBannerView.this.mIndicators.size();
                    if (CustomBannerView.this.mOnPageChangeListener != null) {
                        CustomBannerView.this.mOnPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                    }
                }

                public void onPageSelected(int position) {
                    CustomBannerView.this.mCurrentItem = position;
                    int realSelectPosition = CustomBannerView.this.mCurrentItem % CustomBannerView.this.mIndicators.size();

                    for (int i = 0; i < CustomBannerView.this.mDatas.size(); ++i) {
                        if (i == realSelectPosition) {
                            CustomBannerView.this.mIndicators.get(i).setImageResource(CustomBannerView.this.mIndicatorRes[1]);
                        } else {
                            CustomBannerView.this.mIndicators.get(i).setImageResource(CustomBannerView.this.mIndicatorRes[0]);
                        }
                    }

                    if (CustomBannerView.this.mOnPageChangeListener != null) {
                        CustomBannerView.this.mOnPageChangeListener.onPageSelected(realSelectPosition);
                    }

                }

                public void onPageScrollStateChanged(int state) {
                    if (CustomBannerView.this.mOnPageChangeListener != null) {
                        CustomBannerView.this.mOnPageChangeListener.onPageScrollStateChanged(state);
                    }

                }
            });
        }
    }

    public LinearLayout getIndicatorContainer() {
        return this.mIndicatorContainer;
    }

    public void setDuration(int duration) {
        this.mViewPagerScroller.setDuration(duration);
    }

    public void setUseDefaultDuration(boolean useDefaultDuration) {
        this.mViewPagerScroller.setUseDefaultDuration(useDefaultDuration);
    }

    public int getDuration() {
        return this.mViewPagerScroller.getScrollDuration();
    }


    public interface BannerPageClickListener {
        void onPageClick(View var1, int var2);
    }

    public static class ViewPagerScroller extends Scroller {
        private int mDuration = 800;
        private boolean mIsUseDefaultDuration = false;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.mIsUseDefaultDuration ? duration : this.mDuration);
        }

        public void setUseDefaultDuration(boolean useDefaultDuration) {
            this.mIsUseDefaultDuration = useDefaultDuration;
        }

        public boolean isUseDefaultDuration() {
            return this.mIsUseDefaultDuration;
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }

        public int getScrollDuration() {
            return this.mDuration;
        }
    }

    public static class MyPagerAdapter<T> extends PagerAdapter {
        private List<T> mDatas;
        private HolderCreator mHolderCreator;
        private ViewPager mViewPager;
        private boolean canLoop;
        private BannerPageClickListener mPageClickListener;

        public MyPagerAdapter(List<T> datas, HolderCreator holderCreator, boolean canLoop) {
            if (this.mDatas == null) {
                this.mDatas = new ArrayList();
            }
            this.mDatas.addAll(datas);
            this.mHolderCreator = holderCreator;
            this.canLoop = canLoop;
        }


        public void setPageClickListener(BannerPageClickListener pageClickListener) {
            this.mPageClickListener = pageClickListener;
        }

        public void setUpViewViewPager(ViewPager viewPager) {
            this.mViewPager = viewPager;
            this.mViewPager.setAdapter(this);
            this.mViewPager.getAdapter().notifyDataSetChanged();
            int currentItem = this.canLoop ? this.getStartSelectItem() : 0;
            this.mViewPager.setCurrentItem(currentItem);
        }

        private int getStartSelectItem() {
            if (this.getRealCount() == 0) {
                return 0;
            } else {
                int currentItem = this.getRealCount() * 500 / 2;
                if (currentItem % this.getRealCount() == 0) {
                    return currentItem;
                } else {
                    while (currentItem % this.getRealCount() != 0) {
                        ++currentItem;
                    }

                    return currentItem;
                }
            }
        }

        public int getCount() {
            return this.canLoop ? this.getRealCount() * 500 : this.getRealCount();
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View view = this.getView(position, container);
            container.addView(view);
            return view;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void finishUpdate(ViewGroup container) {
            if (this.canLoop) {
                int position = this.mViewPager.getCurrentItem();
                if (position == this.getCount() - 1) {
                    position = 0;
                    this.setCurrentItem(position);
                }
            }

        }

        private void setCurrentItem(int position) {
            try {
                this.mViewPager.setCurrentItem(position, false);
            } catch (IllegalStateException var3) {
                var3.printStackTrace();
            }

        }

        private int getRealCount() {
            return this.mDatas == null ? 0 : this.mDatas.size();
        }

        private View getView(int position, ViewGroup container) {
            final int realPosition = position % this.getRealCount();
            ViewHolderInterface holder;
            holder = this.mHolderCreator.createViewHolder();
            if (holder == null) {
                throw new RuntimeException("can not return a null holder");
            } else {
                View view = holder.createView(container.getContext());
                if (this.mDatas != null && this.mDatas.size() > 0) {
                    holder.onBind(container.getContext(), realPosition, this.mDatas.get(realPosition));
                }

                view.setOnClickListener(v -> {
                    if (MyPagerAdapter.this.mPageClickListener != null) {
                        MyPagerAdapter.this.mPageClickListener.onPageClick(v, realPosition);
                    }

                });
                return view;
            }
        }
    }

}
