package com.sn.online.common;

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
    public interface orderStats {
        public static final Integer initial =0;
        public static final Integer error=-1;
        public static final Integer finish=1;
    }

}
