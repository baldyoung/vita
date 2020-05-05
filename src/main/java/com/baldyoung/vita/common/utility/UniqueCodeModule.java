package com.baldyoung.vita.common.utility;


import java.util.HashMap;
import java.util.Map;

/**
 * 一个可以生成唯一标识的工厂模块，它用于保证，在该架构下每次生成的标识码都是唯一的，即使同时存在多个工厂模块。
 * 每一个UniqueCodeModule都需要指定两个头尾标识，它们将会是该工厂模块生成的唯一标识的一部分。
 * 工厂模块的生成调用将会检查每次生成的工厂模块的头部标识是否具有唯一性，对于已存在的头部标识，将拒绝二次生成。
 * 每个工厂模块在创建时都会获取一个系统当前的时间戳作为标识初始值，并在每次生成唯一标识时对其进行递增操作。
 * @author baldyoung
 */
public class UniqueCodeModule {
    //标记队列，记录所有已经注册的工厂模块
    private static Map<String, String> SignQueue = new HashMap<>();
    private long initialValue;
    private String header, tail;
    /**
     * 向标记队列尝试新增一个工厂模块，其为线程安全的调用
     * 线程安全
     * @param tHeader
     * @param tTail
     * @return false：已存在；true：注册成功
     */
    private static boolean addModule(String tHeader, String tTail){
        if(null == tHeader || null == tTail) return false;
        synchronized(SignQueue) {
            if (null != SignQueue.get(tHeader)) return false;
            SignQueue.put(tHeader, tTail);
        }
        return true;
    }
    /**
     * 尝试删除一个指定的工厂模块，其为线程安全的调用
     * 线程安全
     * @param tHeader
     */
    private static void delModule(String tHeader){
        if(null != tHeader)
            synchronized (SignQueue) {
                SignQueue.remove(tHeader);
            }
    }

    /**
     * 生产工厂模块的方法，其为线程安全的调用
     * 线程安全
     * @param tHeader
     * @param tTail
     * @return
     */
    public static UniqueCodeModule getInstance(String tHeader, String tTail){
        if(addModule(tHeader, tTail))
            return new UniqueCodeModule(tHeader, tTail);
        return null;
    }


    /**
     * 工厂模块的私有构造函数
     * @param tHeader
     * @param tTail
     */
    private UniqueCodeModule(String tHeader, String tTail){
        this.header = tHeader;
        this.tail = tTail;
        //this.initialValue = System.nanoTime();
        this.initialValue = System.currentTimeMillis();
    }

    /**
     * 获取下一个递进值，其为线程安全的调用
     * @return
     */
    private long nextValue(){
        long theNext;
        synchronized(this.header){
            theNext = this.initialValue++;
        }
        return theNext;
    }

    /**
     * 获取当前工厂模块所生产的一个唯一的标识，其为线程安全调用
     * @return
     */
    public String getUniqueCode(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.header);
        builder.append(nextValue());
        builder.append(this.tail);
        return builder.toString();
    }

    public String getUniqueCode(int maxLength) {
        String k = getUniqueCode();
        if (maxLength <= 0 || maxLength > k.length()) {
            maxLength = k.length();
        }
        return k.substring(0, maxLength);
    }


}
