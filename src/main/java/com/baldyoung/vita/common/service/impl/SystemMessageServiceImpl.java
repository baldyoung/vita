package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.pojo.dto.diningRoom.MDiningRoomNewsDto;
import com.baldyoung.vita.merchant.serverEndpoint.MerchantSystemMessageServerPoint;
import com.baldyoung.vita.merchant.service.MSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 系统消息服务
 */
@Service
public class SystemMessageServiceImpl {
    // 线程池
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private MSystemServiceImpl mSystemService;


    @PostConstruct
    void init() {
        threadPoolExecutor = new ThreadPoolExecutor(
                5,
                50,
                120,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(124),
                new ThreadPoolExecutor.DiscardPolicy());

    }

    private void refrushLastNews() {
        List<MDiningRoomNewsDto> list = mSystemService.getCurrentDiningRoomNews();
        MerchantSystemMessageServerPoint.newsOption(list);
    }

    public void pullMerchantUnreadMessage() {
        threadPoolExecutor.submit(()->{
            refrushLastNews();
        });
    }



}
