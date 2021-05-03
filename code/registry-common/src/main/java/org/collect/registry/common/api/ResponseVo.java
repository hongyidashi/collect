package org.collect.registry.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Optional;

/**
 * 描述: 统一API响应VO
 * 作者: panhongtong
 * 创建时间: 2021-01-21 15:51
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ResponseVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private boolean success;
    private T data;
    private String msg;

    private ResponseVo(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private ResponseVo(IResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private ResponseVo(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private ResponseVo(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private ResponseVo(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(@Nullable ResponseVo<?> result) {
        return Optional.ofNullable(result)
                .map(x -> ObjectUtils.nullSafeEquals(ResultCode.SUCCESS.code, x.code))
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isNotSuccess(@Nullable ResponseVo<?> result) {
        return !ResponseVo.isSuccess(result);
    }

    /**
     * 返回ResponseVo
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> data(T data) {
        return data(data, "操作成功");
    }

    /**
     * 返回ResponseVo
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> data(T data, String msg) {
        return data(HttpServletResponse.SC_OK, data, msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param code 状态码
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> data(int code, T data, String msg) {
        return new ResponseVo<>(code, data, data == null ? "操作成功" : msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> success(String msg) {
        return new ResponseVo<>(ResultCode.SUCCESS, msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> success(IResultCode resultCode) {
        return new ResponseVo<>(resultCode);
    }

    /**
     * 返回ResponseVo
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> success(IResultCode resultCode, String msg) {
        return new ResponseVo<>(resultCode, msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> fail(String msg) {
        return new ResponseVo<>(ResultCode.FAILURE, msg);
    }


    /**
     * 返回ResponseVo
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> fail(int code, String msg) {
        return new ResponseVo<>(code, null, msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> fail(IResultCode resultCode) {
        return new ResponseVo<>(resultCode);
    }

    /**
     * 返回ResponseVo
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> ResponseVo<T> fail(IResultCode resultCode, String msg) {
        return new ResponseVo<>(resultCode, msg);
    }

    /**
     * 返回ResponseVo
     *
     * @param flag 成功状态
     * @return R
     */
    public static <T> ResponseVo<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }
}
