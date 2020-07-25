package com.baldyoung.vita.merchant.serverEndpoint;

import com.alibaba.fastjson.JSON;
import com.baldyoung.vita.common.pojo.dto.diningRoom.MDiningRoomNewsDto;
import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import com.baldyoung.vita.merchant.service.MSystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@ServerEndpoint(value = "/mSystemMessage")
@Component
public class MerchantSystemMessageServerPoint {
    private static Logger logger = LoggerFactory.getLogger(MerchantSystemMessageServerPoint.class);
    // 统计当前在线人数
    private static Integer currentUserNumber = 0;
    // 当前在线用户的映射图
    private static ConcurrentHashMap<Integer, Session> sessionPools = new ConcurrentHashMap<>();
    @Autowired
    private MSystemServiceImpl mSystemService;


    // 添加在线人数
    private static void addUserNumber(int number) {
        synchronized (currentUserNumber) {
            currentUserNumber += number;
        }
    }

    // 获取当前在线人数
    private static int getUserNumber() {
        synchronized (currentUserNumber) {
            return currentUserNumber.intValue();
        }
    }

    // 获取最新数据，并发送给在线商家端
    // @Scheduled(fixedRate = 2000)
    public static void newsOption(List<MDiningRoomNewsDto> news) {
        System.out.println("kkk");
        Set<Map.Entry<Integer, Session>> entries = sessionPools.entrySet();
        System.out.println("用户队列："+entries.size());
        if (null == entries || 0 == entries.size()) {
            return;
        }
        System.out.println("新消息："+news.toString());
        if (isEmptyCollection(news)) {
            return;
        }
        String newsString = JSON.toJSONString(news);
        for (Map.Entry<Integer, Session> entry : entries) {
            try {
                sendMessage(entry.getValue(), newsString);
            } catch (IOException e) {
                logger.error("userId:{} -> {}", entry.getKey(), e.getMessage());
            }
        }
    }

    // 给指定session发送消息
    public static void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    // 给指定用户Id发送信息
    public static void sendMessage(Integer userId, String message) throws IOException {
        Session session = sessionPools.get(userId);
        sendMessage(session, message);
    }

    // 成功建立连接后的调用
    @OnOpen
    public void onOpen(Session websocketSession) throws UtilityException {

        sessionPools.put(0, websocketSession);
        addUserNumber(1);
        // 启动客户端定时器，定期获取最新的数据
    }

    // 连接关闭后的调用
    @OnClose
    public void onClose(Session websocketSession) throws UtilityException {
        System.out.println("关闭");
        // sessionPools.remove(userId);
        addUserNumber(-1);
    }

    // 接收到客户端的消息后调用
    @OnMessage
    public void onMessage(String message) throws IOException {
        System.out.println(message);
        // newsOption();
    }

    // 出现异常后被调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

}

