package tech.hongjian.oa.service;

public interface TokenService {
    String CANCEL_LIST_PREFIX = "CancelToken:";
    /**
     * 吊销token
     * @param token 需要被吊销的token
     */
    void cancelToken(String token);

    /**
     * 判断token是否在吊销列表中
     * @param token 需要检查的token
     * @return 在吊销列表中返回true,否则返回false
     */
    boolean inCancelList(String token);
}
