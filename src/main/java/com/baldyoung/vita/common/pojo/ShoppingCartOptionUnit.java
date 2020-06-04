package com.baldyoung.vita.common.pojo;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;

/**
 * 购物车控制单元
 */
public class ShoppingCartOptionUnit {
    /**
     * 购物车读写锁
     * 用于保证并发情况下的购物车操作安全
     */
    private ReadWriteLock readWriteLock;
    /**
     * 购物车提交标识
     * true:购物车正在提交中，不允许所有对购物车的修改操作
     * false:购物车非提交状态，可以对购物车进行修改
     */
    private boolean isReadyForOrder;
    /**
     * 最近心跳时间
     * 客户端对购物车的提交需要以心跳的方式维持购物车的下单状态，直至下单完成或者取消购物车提交。
     * 这里记录的是最近一次与客户端的心跳时间。
     */
    private Date lastHeartBeat;
    /**
     * 心跳密钥
     * 客户端与对应购物车进行心跳交互，需要核验该密钥是否一致。
     * 密钥不一致，拒绝心跳更新。
     */
    private String heartBeatKey;

    /**
     * 心跳次数
     */
    private int heartBeatTimes = 0;

    @Override
    public String toString() {
        return "ShoppingCartOptionUnit{" +
                "readWriteLock=" + readWriteLock +
                ", isReadyForOrder=" + isReadyForOrder +
                ", lastHeartBeat=" + lastHeartBeat +
                ", heartBeatKey='" + heartBeatKey + '\'' +
                '}';
    }

    public ShoppingCartOptionUnit() {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.isReadyForOrder = false;
        this.lastHeartBeat = null;
        this.heartBeatKey = null;
    }

    /**【非线程安全】
     * 判断当前心跳交互是否失效
     * @return
     */
    public boolean isHeartBeatInvalid() {
        if (null == this.lastHeartBeat) {
            return true;
        }
        Date date = new Date();
        long second = (date.getTime() - this.lastHeartBeat.getTime()) / 1000;
        if (second > 3 || ++this.heartBeatTimes > CommonConfig.customerHeartBeatMaxTimes) {
            this.lastHeartBeat = null;
            this.heartBeatTimes = 0;
            return true;
        }
        return false;
    }

    /**【线程安全】
     * 尝试修改购物车的前置准备
     * @return
     * @throws ServiceException
     */
    public Lock readyWriteAction() throws ServiceException {
        /*out.println("########");
        out.println("current time:"+new Date().toString());
        out.println("record time:"+this.lastHeartBeat);
        out.println(this.toString());*/
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        if (true == isReadyForOrder && !isHeartBeatInvalid()) {
            // 购物车处于提交状态中，拒绝修改操作
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_PRE_ORDER);
        }
        return lock;
    }

    /**【线程安全】
     * 尝试读取购物车的前置准备
     * @return
     */
    public Lock readyReadAction() {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        return lock;
    }

    /**【线程安全】
     * 尝试提交购物车的前置准备
     * @param heartBeatKey
     */
    public Lock readySubmit(String heartBeatKey) throws ServiceException {
        Lock lock = readyWriteAction();
        this.isReadyForOrder = true;
        this.heartBeatKey = heartBeatKey;
        this.lastHeartBeat = new Date();
        return lock;
    }

    /**【线程安全】
     * 尝试心跳交互的前置准备
     * @param heartBeatKey
     * @return
     * @throws ServiceException
     */
    public Lock readyDoHeartBeat(String heartBeatKey) throws ServiceException {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        if (!heartBeatKey.equals(this.heartBeatKey)) {
            // 密钥不一致，拒绝更新心跳时间
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_NO_POWER);
        }
        if (null != this.lastHeartBeat && isHeartBeatInvalid()) {
            lock.unlock();
            throw new ServiceException(SHOPPING_CART_TIME_OUT);
        }
        this.lastHeartBeat = new Date();
        return lock;
    }

}
