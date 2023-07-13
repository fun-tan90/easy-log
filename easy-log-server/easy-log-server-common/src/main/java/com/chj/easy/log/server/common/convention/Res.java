package com.chj.easy.log.server.common.convention;

import com.chj.easy.log.server.common.convention.enums.IErrorCode;
import com.chj.easy.log.server.common.convention.page.FullPage;
import com.chj.easy.log.server.common.convention.page.SimplePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/1/31 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Res<T> implements Serializable {

    private static final long serialVersionUID = -3243475556181265119L;

    private String code;

    private String msg;

    private T data;

    public static Res<Void> ok() {
        return new Res<>(IErrorCode.OK.getCode(), IErrorCode.OK.getMessage(), null);
    }

    public static <T> Res<T> ok(T data) {
        return new Res<>(IErrorCode.OK.getCode(), IErrorCode.OK.getMessage(), data);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return new Res<>(IErrorCode.OK.getCode(), msg, data);
    }

    public static Res<Void> error() {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), IErrorCode.SYS_2000001.getMessage(), null);
    }

    public static <T> Res<T> error(T data) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), IErrorCode.SYS_2000001.getMessage(), data);
    }

    public static Res<Void> error(IErrorCode iErrorCode) {
        return new Res<>(iErrorCode.getCode(), iErrorCode.getMessage(), null);
    }

    public static <T> Res<T> error(IErrorCode iErrorCode, T data) {
        return new Res<>(iErrorCode.getCode(), iErrorCode.getMessage(), data);
    }

    public static Res<Void> errorMsg(String msg) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), msg, null);
    }

    public static Res<Void> errorCodeAndMsg(String code, String msg) {
        return new Res<>(code, msg, null);
    }

    public static <T> Res<T> errorMsgAndData(String msg, T data) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), msg, data);
    }

    /**
     * @param currentPage 当前页
     * @param totalCount  总记录数
     * @param records     获取每页数据的list集合
     * @return
     */
    public static <U> Res<SimplePage<U>> pageOf(int currentPage, long totalCount, List<U> records) {
        return Res.ok(new SimplePage<>(currentPage, totalCount, records));
    }

    /**
     * @param currentPage 当前页
     * @param totalCount  总记录数
     * @param totalPage   总页数
     * @param previous    是否有上一页
     * @param next        是否有下一页
     * @param records     获取每页数据的list集合
     * @return
     */
    public static <U> Res<FullPage<U>> pageOf(int currentPage, long totalCount, List<U> records, Long totalPage, boolean previous, boolean next) {
        return Res.ok(new FullPage<>(currentPage, totalCount, records, totalPage, previous, next));
    }
}
