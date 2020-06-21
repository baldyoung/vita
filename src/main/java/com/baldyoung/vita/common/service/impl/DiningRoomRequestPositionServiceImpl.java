package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.dao.DiningRoomDao;
import com.baldyoung.vita.common.dao.DiningRoomPositionDao;
import com.baldyoung.vita.common.pojo.entity.DiningRoomEntity;
import com.baldyoung.vita.common.pojo.entity.DiningRoomPositionEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.utility.QRcodeModule;
import com.baldyoung.vita.common.utility.RandomStringModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.ROOM_NOT_FOUND;

/**
 * 就餐位的访问映射服务
 * 顾客点餐，需要提供该服务获取对应的访问数据
 */
@Service
public class DiningRoomRequestPositionServiceImpl {

    @Autowired
    private DiningRoomDao diningRoomDao;

    @Autowired
    private DiningRoomPositionDao diningRoomPositionDao;

    @Autowired
    private SystemInfoServiceImpl systemInfoService;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private DiningRoomServiceImpl diningRoomService;

    @Autowired
    private InvoiceServiceImpl invoiceService;

    private Map<String, Integer> keyMap;

    private Map<Integer, DiningRoomPositionEntity> positionMap;

    @PostConstruct
    public void init() throws Exception {
        keyMap = new HashMap();
        positionMap = new HashMap();
        List<DiningRoomEntity> diningRoomEntities = diningRoomDao.selectAll();
        List<DiningRoomPositionEntity> diningRoomPositionEntities = diningRoomPositionDao.selectAll();
        for (DiningRoomPositionEntity positionEntity : diningRoomPositionEntities) {
            positionMap.put(positionEntity.getDiningRoomId(), positionEntity);
            keyMap.put(positionEntity.getPositionKey(), positionEntity.getDiningRoomId());
        }
        for (DiningRoomEntity diningRoomEntity : diningRoomEntities) {
            Integer roomId = diningRoomEntity.getDiningRoomId();
            DiningRoomPositionEntity diningRoomPositionEntity = positionMap.get(roomId);
            if (null == diningRoomPositionEntity || null == diningRoomPositionEntity.getPositionKey()) {
                createNewPosition(roomId);
            }
        }
    }

    /**
     * 获取指定就餐位的映射key
     * @param diningRoomId
     * @return
     */
    public String getDiningRoomKey(Integer diningRoomId) {
        DiningRoomPositionEntity entity = positionMap.get(diningRoomId);
        if (null == entity || null == entity.getPositionKey()) {
            return null;
        }
        return entity.getPositionKey();
    }

    /**
     * 获取指定key所映射的就餐位Id
     * @param key
     * @return
     */
    public Integer getDiningRoomId(String key) {
        return keyMap.get(key);
    }

    /**
     * 新增一个就餐位的访问映射
     * @param diningRoomId
     * @return
     * @throws Exception
     */
    public String createNewPosition(Integer diningRoomId) throws Exception {
        ifExistsPositionDelete(diningRoomId);
        DiningRoomEntity diningRoomEntity = diningRoomService.getDiningRoom(diningRoomId);
        if (null == diningRoomEntity || null == diningRoomEntity.getDiningRoomId()) {
            throw new ServiceException(ROOM_NOT_FOUND);
        }
        // 获取唯一的一个映射key，并与现有的key进行匹配，确保没有重复的key出现
        String key = null;
        while(true) {
            key = RandomStringModule.getRandomString(12);
            DiningRoomPositionEntity diningRoomPositionEntity = diningRoomPositionDao.selectByPositionKey(key);
            if (null == diningRoomPositionEntity || null == diningRoomPositionEntity.getDiningRoomId()) {
                break;
            }
        }
        DiningRoomPositionEntity entity = new DiningRoomPositionEntity();
        entity.setDiningRoomId(diningRoomId);
        entity.setPositionKey(key);
        // 生成点餐二维码
        FileInputStream fileInputStream = new FileInputStream(systemInfoService.getQRcodeImgPath()+"default.jpg");
        String url = commonConfig.serveAddress + commonConfig.positionAddress + key;
        System.out.println("###:"+url);
        BufferedImage bufferedImage = QRcodeModule.createWithLogoAndText(url, fileInputStream, diningRoomEntity.getDiningRoomName());
        QRcodeModule.saveBufferedImageAsFile(bufferedImage, systemInfoService.getQRcodeImgPath()+key+".jpg");
        // 将新增二维码的映射数据同步到数据库和缓存中
        diningRoomPositionDao.insertPosition(entity);
        keyMap.put(key, diningRoomId);
        positionMap.put(diningRoomId, entity);
        return key;
    }

    /**
     * 如果指定就餐位的访问映射存在则删除其相关数据
     * @param diningRoomId
     */
    public void ifExistsPositionDelete(Integer diningRoomId) {
        DiningRoomPositionEntity entity = positionMap.get(diningRoomId);
        if (null == entity || null == entity.getPositionKey()) {
            return;
        }
        invoiceService.removeInvoiceKeyValue(entity.getPositionKey());
        String key = entity.getPositionKey();
        keyMap.put(key, null);
        diningRoomPositionDao.deletePositionByDiningRoomId(diningRoomId);
        File file = new File(systemInfoService.getQRcodeImgPath()+key+".jpg");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }


}
