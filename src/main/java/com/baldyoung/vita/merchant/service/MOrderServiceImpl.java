package com.baldyoung.vita.merchant.service;

import com.baldyoung.vita.common.dao.OrderDao;
import com.baldyoung.vita.common.dao.OrderItemDao;
import com.baldyoung.vita.common.dao.ProductDao;
import com.baldyoung.vita.common.pojo.dto.order.MOrderDto;
import com.baldyoung.vita.common.pojo.dto.orderItem.MOrderItemDto;
import com.baldyoung.vita.common.pojo.entity.OrderEntity;
import com.baldyoung.vita.common.pojo.entity.OrderItemEntity;
import com.baldyoung.vita.common.pojo.entity.ProductEntity;
import com.baldyoung.vita.common.pojo.exception.serviceException.ServiceException;
import com.baldyoung.vita.common.service.impl.BillServiceImpl;
import com.baldyoung.vita.common.service.impl.OrderServiceImpl;
import com.baldyoung.vita.common.service.impl.SystemMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.baldyoung.vita.common.pojo.enums.serviceEnums.ServiceExceptionEnum.*;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmpty;
import static com.baldyoung.vita.common.utility.CommonMethod.isEmptyCollection;

@Service
public class MOrderServiceImpl {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private SystemMessageServiceImpl systemMessageService;

    /**
     * 一次性订单已读时，不能变更为已读状态的商品项状态
     */
    private static List<Integer> refuseOrderItemStatusWhenSetOrderRead;

    @PostConstruct
    public void init() {
        Integer[] refuseStatus = {1, 4};
        refuseOrderItemStatusWhenSetOrderRead = new ArrayList(refuseStatus.length);
        for (Integer status : refuseStatus) {
            refuseOrderItemStatusWhenSetOrderRead.add(status);
        }
    }

    /**
     * 获取指定就餐位的所有订单
     * @param billNumber
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(String billNumber) {
        List<OrderEntity> entityList = orderDao.selectOrderByBillNumber(billNumber);
        if (isEmptyCollection(entityList)) {
            return new ArrayList(0);
        }
        List<Integer> orderIds = new ArrayList(entityList.size());
        for (OrderEntity entity : entityList) {
            orderIds.add(entity.getOrderId());
        }
        List<MOrderDto> result = new ArrayList(entityList.size());
        List<OrderItemEntity> itemEntityList = orderItemDao.selectOrderItemListWithOrderIdList(orderIds);
        for (OrderEntity entity : entityList) {
            MOrderDto dto = new MOrderDto();
            Integer orderId = entity.getOrderId();
            dto.setBillNumber(entity.getBillNumber());
            dto.setOrderCreateDateTime(entity.getOrderCreateDateTime());
            dto.setOrderId(entity.getOrderId());
            dto.setOrderInitiatorFlag(entity.getOrderInitiatorFlag());
            dto.setOrderPresetTime(entity.getOrderPresetTime());
            dto.setOrderTypeFlag(entity.getOrderTypeFlag());
            List<MOrderItemDto> itemList = new LinkedList();
            for (OrderItemEntity itemEntity : itemEntityList) {
                Integer itemOrderId = itemEntity.getOrderId();
                if (itemOrderId.equals(orderId)) {
                    MOrderItemDto itemDto = new MOrderItemDto();
                    itemDto.setOrderProductId(itemEntity.getOrderProductId());
                    itemDto.setOrderProductImg(itemEntity.getOrderProductImg());
                    itemDto.setOrderProductItemId(itemEntity.getOrderProductItemId());
                    itemDto.setOrderProductItemStatusDesc(itemEntity.getOrderProductItemStatusDesc());
                    itemDto.setOrderProductItemStatusFlag(itemEntity.getOrderProductItemStatusFlag());
                    itemDto.setOrderProductName(itemEntity.getOrderProductName());
                    itemDto.setOrderProductPrice(itemEntity.getOrderProductPrice());
                    itemDto.setOrderProductQuantity(itemEntity.getOrderProductQuantity());
                    itemDto.setOrderProductRemarks(itemEntity.getOrderProductRemarks());
                    itemList.add(itemDto);
                }
            }
            dto.setItemList(itemList);
            result.add(dto);
        }
        return result;
    }
    /**
     * 获取指定就餐位的所有订单
     * @param roomId
     * @return
     */
    public List<MOrderDto> getAllOrderInRoom(Integer roomId) {
        String billNumber = billService.getRoomBillNumberWithoutCreate(roomId);
        if (isEmpty(billNumber)) {
            return new ArrayList(0);
        }
        return getAllOrderInRoom(billNumber);
    }

    /**
     * 修改指定订单项的内容
     * @param entity
     */
    public void updateOrderItem(OrderItemEntity entity) {
        orderItemDao.updateOrderItem(entity);
        systemMessageService.pullMerchantUnreadMessage();
    }


    /**
     * 替换订单商品
     * @param itemId
     * @param productId
     * @throws ServiceException
     */
    public void changeOrderItemProduct(Integer itemId, Integer productId) throws ServiceException {
        ProductEntity productEntity = productDao.findProductByProductId(productId);
        if (null == productEntity || null == productEntity.getProductId()) {
            throw new ServiceException(PRODUCT_NOT_FOUND);
        }
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrderProductItemId(itemId);
        entity.setOrderProductId(productId);
        entity.setOrderProductName(productEntity.getProductName());
        entity.setOrderProductImg(productEntity.getProductImgName());
        entity.setOrderProductPrice(productEntity.getProductPrice());
        orderItemDao.updateOrderItem(entity);
    }

    /**
     * 新增订单项
     * @param roomId
     * @param diningTypeFlag
     * @param item
     */
    public void addOrderItem(Integer roomId, Integer diningTypeFlag, OrderItemEntity item) throws ServiceException {
        List<OrderItemEntity> resultList = orderService.doOrder(roomId, diningTypeFlag, "", 0, Arrays.asList(item));
        OrderItemEntity result = resultList.get(0);
        if (1 == result.getOrderProductItemStatusFlag().intValue()) {
            throw new ServiceException(PRODUCT_UNDERSTOCK);
        }
    }

    /**
     * 批量将指定商品项变成已完成状态
     * @param itemIdList
     */
    public void setOrderProductItemToFinish(List<Integer> itemIdList) {
        orderItemDao.updateOrderItemListStatus(itemIdList, 3);
    }

    /**
     * 获取指定订单的数据
     * 订单信息 + 相应商品项
     * @param orderId
     * @return
     * @throws ServiceException
     */
    public MOrderDto getOrderInfo(Integer orderId) throws ServiceException {
        OrderEntity orderEntity = orderDao.selectOrderByOrderId(orderId);
        if (null == orderEntity || null == orderEntity.getOrderId()) {
            throw new ServiceException(ORDER_NOT_EXISTS);
        }
        MOrderDto mOrderDto = new MOrderDto();
        mOrderDto.setOrderTypeFlag(orderEntity.getOrderTypeFlag());
        mOrderDto.setOrderPresetTime(orderEntity.getOrderPresetTime());
        mOrderDto.setOrderInitiatorFlag(orderEntity.getOrderInitiatorFlag());
        mOrderDto.setOrderCreateDateTime(orderEntity.getOrderCreateDateTime());
        List<OrderItemEntity> orderItemEntityList = orderItemDao.selectOrderItemList(orderId);
        if (isEmptyCollection(orderItemEntityList)) {
            return mOrderDto;
        }
        List<MOrderItemDto> orderItemList = new ArrayList(orderItemEntityList.size());
        for (OrderItemEntity item : orderItemEntityList) {
            MOrderItemDto mItem = new MOrderItemDto();
            mItem.setOrderProductName(item.getOrderProductName());
            mItem.setOrderProductImg(item.getOrderProductImg());
            mItem.setOrderProductPrice(item.getOrderProductPrice());
            mItem.setOrderProductQuantity(item.getOrderProductQuantity());
            mItem.setOrderProductItemStatusFlag(item.getOrderProductItemStatusFlag());
            mItem.setOrderProductItemStatusDesc(item.getOrderProductItemStatusDesc());
            mItem.setOrderProductRemarks(item.getOrderProductRemarks());
            orderItemList.add(mItem);
        }
        mOrderDto.setItemList(orderItemList);
        return mOrderDto;
    }

    /**
     * 对指定订单进行一次性已读操作
     * @param orderId
     */
    public void setOrderWithReadFlag(Integer orderId) {
        orderItemDao.setOrderItemStatusWithCondition(orderId, refuseOrderItemStatusWhenSetOrderRead, 2);
    }

}
