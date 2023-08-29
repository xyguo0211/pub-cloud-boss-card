package com.pub.core.common;

public interface OnlineConstants {

    public interface blockStatus {
        public static final Integer block_no=9;
        public static final Integer block=-1;
    }
    public interface onlineRole {
        public static final Integer system_no=2;
        public static final Integer system=1;
    }
    public interface deleteStats {
        public static final Integer delete_no=9;
        public static final Integer delete=-1;
    }


    /**
     * 状态  9成功  -1失败  0 取消  1 初始化
     */
    public interface DrawStats {
        public static final Integer initial =1;
        public static final Integer cancel =0;
        public static final Integer error=-1;
        public static final Integer finish=9;
    }

}
