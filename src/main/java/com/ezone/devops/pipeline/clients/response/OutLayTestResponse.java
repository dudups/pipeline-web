package com.ezone.devops.pipeline.clients.response;

import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.ezone.galaxy.framework.common.config.HttpCodeContextHolder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

public class OutLayTestResponse<T> implements Serializable {
    private int rstCode;
    private Boolean success;
    @JsonInclude(Include.NON_NULL)
    private String rstMsg;
    private T data;


    public OutLayTestResponse() {
    }

    public OutLayTestResponse(int rstCode) {
        this(rstCode, (T) null, HttpCodeContextHolder.getDefaultMessage(HttpCode.valueOf(rstCode)));
    }

    public OutLayTestResponse(HttpCode httpCode) {
        this(httpCode, (T) null, HttpCodeContextHolder.getDefaultMessage(httpCode));
    }

    public OutLayTestResponse(int rstCode, T data) {
        this(rstCode, data, (String)null);
    }

    public OutLayTestResponse(HttpCode httpCode, T data) {
        this(httpCode, data, (String)null);
    }

    public OutLayTestResponse(HttpCode httpCode, T data, String rstMsg) {
        this(httpCode.value(), data, rstMsg);
    }

    public OutLayTestResponse(int rstCode, T data, String rstMsg) {
        this();
        this.rstCode = rstCode;
        this.data = data;
        this.rstMsg = rstMsg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.rstCode == HttpCode.SUCCESS.value();
    }

    @JsonIgnore
    public boolean isError() {
        return this.rstCode != HttpCode.SUCCESS.value();
    }

    public int getRstCode() {
        return rstCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getRstMsg() {
        return rstMsg;
    }

    public T getData() {
        return data;
    }

    public void setRstCode(int rstCode) {
        this.rstCode = rstCode;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setRstMsg(String rstMsg) {
        this.rstMsg = rstMsg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof OutLayTestResponse)) {
            return false;
        } else {
            OutLayTestResponse<?> other = (OutLayTestResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getRstCode() != other.getRstCode()) {
                return false;
            } else {
                Object this$message = this.getRstMsg();
                Object other$message = other.getRstMsg();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof OutLayTestResponse;
    }

    public String toString() {
        return "OutLayTestResponse(code=" + this.getRstCode() + ", message=" + this.getRstMsg() + ", data=" + this.getData() + ")";
    }
}
