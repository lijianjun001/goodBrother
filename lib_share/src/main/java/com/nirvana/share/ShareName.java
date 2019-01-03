package com.nirvana.share;

/**
 * Created by Administrator on 2017/9/21.
 */
public enum ShareName {

    DAT_SHARE() {
        @Override
        public String getName() {
            return "shareday";
        }
    },

    RECOMMEND_FRIEND() {
        @Override
        public String getName() {
            return "recommendfriend";
        }
    },
    RECOMMEND_CIRCLE() {
        @Override
        public String getName() {
            return "recommendcircle";
        }
    },
    WEI_BO() {
        @Override
        public String getName() {
            return "weibo";
        }
    },
    FRESHER_TUTORIALS() {
        @Override
        public String getName() {
            return "FresherTutorials";
        }
    },

    RECOMMEND() {
        @Override
        public String getName() {
            return "recommend";
        }
    };

    public abstract String getName();

}
