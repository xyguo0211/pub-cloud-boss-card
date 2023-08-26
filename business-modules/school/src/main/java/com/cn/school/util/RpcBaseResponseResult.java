package com.cn.school.util;
import java.io.Serializable;

/**
 *
 * @ClassName: RpcBaseResponseResult
 * @Description: (基础响应结果)
 * @date 2016年9月21日 下午2:06:37
 */
public class RpcBaseResponseResult implements Serializable{

    private static final long serialVersionUID = 1L;

    //0表示成功，1表示失败状态编码

    private int  status  = 0;

    public final static Integer STATUS_SUCCESS = 0;

    public final static Integer STATUS_ERROR = 1;

    private String message = "";

    private Object data;

    public RpcBaseResponseResult() { }

    public RpcBaseResponseResult(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcBaseResponseResult [data=" + data + "]";
    }

}
