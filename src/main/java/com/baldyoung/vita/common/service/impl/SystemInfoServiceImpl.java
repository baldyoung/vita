package com.baldyoung.vita.common.service.impl;

import com.baldyoung.vita.common.CommonConfig;
import com.baldyoung.vita.common.utility.FileDataSaveModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.net.URL;

/**
 * 系统服务
 */
@Service
public class SystemInfoServiceImpl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonConfig commonConfig;

    private String productImgPath;

    private String qrcodeImgPath;

    /**
     * 获取商品图片的存储路径
     * @return
     */
    public String getProductImgPath() {
        if (null == productImgPath) {
            URL url = ClassUtils.getDefaultClassLoader().getResource("static");
            System.out.println("当前项目的位置："+(url != null ? url.getPath() : ""));
            if (url != null && -1 == url.getPath().indexOf(".jar")) {
                // 如果是idea中开发运行，请使用下面这两行代码生成存储路径
                String projectPath = url.getPath();
                productImgPath = projectPath + File.separator + commonConfig.productImgPath.replace(".", File.separator) + File.separator;
            } else {
                // 如果是生产环境运行，请将jar中的“static目录”复制到jar同级的目录下
                productImgPath = "static"+File.separator+commonConfig.productImgPath.replace(".", File.separator) + File.separator;
            }
            productImgPath = FileDataSaveModule.adjustPathNameSeparator(productImgPath);
            logger.info("商品图片存储路径初始化:" + productImgPath);
        }
        return productImgPath;
    }

    /**
     * 获取二维码存储路径
     * @return
     */
    public String getQRcodeImgPath() {
        if (null == qrcodeImgPath) {
            URL url = ClassUtils.getDefaultClassLoader().getResource("static");
            System.out.println("当前项目的位置："+(url != null ? url.getPath() : ""));
            if (url != null && -1 == url.getPath().indexOf(".jar")) {
                // 如果是idea中开发运行，请使用下面这两行代码生成存储路径
                String projectPath = url.getPath();
                qrcodeImgPath = projectPath + File.separator + commonConfig.qrcodeImgPath.replace(".", File.separator) + File.separator;
            } else {
                // 如果是生产环境运行，请将jar中的“static目录”复制到jar同级的目录下
                qrcodeImgPath = "static"+File.separator+commonConfig.qrcodeImgPath.replace(".", File.separator) + File.separator;
            }
            qrcodeImgPath = FileDataSaveModule.adjustPathNameSeparator(qrcodeImgPath);
            logger.info("二维码存储路径初始化:" + qrcodeImgPath);
        }
        return qrcodeImgPath;
    }

}
