package com.baldyoung.vita.common.utility;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成模块  version 0.9
 * 依赖：core.jar(测试版本3.4.0)
 * @author baldyoung
 */
public class QRcodeModule {
    public static final String DEFAULT_CHARACTER_SET = "utf-8";
    public static final ErrorCorrectionLevel DEFAULT_ERRORCORRECTIONLEVEL = ErrorCorrectionLevel.H;
    public static final int WIDTH = 400, HEIGHT = 400, BRUSHCOLOR = 0xFF000000, BACKGROUNDCOLOR = 0xFFFFFFFF;
    public static final int DEFAULT_SENTENCE_HEIGHT = 40;
    public static final Color DEFAULT_SENTENCE_COLOR = Color.BLACK, DEFAULT_SENTENCE_BACKGROUND_COLOR = Color.lightGray;
    public static final Font DEFAULT_SENTENCE_FONT = new Font("宋体", Font.BOLD, 30);

    /**
     * 将给定的BufferedImage对象存储到指定的文件里，并且函数会自动解析filePath参数中的文件类型
     * @param bufferedImage 要存储的BufferedIamge对象
     * @param filePath 可以打开的文件名或者加上其相对路径，亦或者加上其绝对路径
     * @throws Exception 注意捕获函数抛出的异常，如果没有异常抛出则代表存储成功，否则存储失败
     */
    public static void saveBufferedImageAsFile(BufferedImage bufferedImage, String filePath) throws Exception{
        File file = new File(filePath);
        if(!file.exists()) file.createNewFile();
        if(file.isDirectory()) throw new Exception("filepath参数必须指定为文件");
        if(!file.canWrite()) throw new Exception("filepath指定的文件无法写入");
        //从filepath中解析出要存储文件类型
        String[] temp = filePath.split(".");
        String fileType = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
        if(!ImageIO.write(bufferedImage, fileType, file)) throw new Exception("写入失败");
    }

    /**
     * 将给定的BufferedImage对象数据存储到内存，并返回其对应的InputStream对象
     * @param bufferedImage 要被转换的BufferedImage对象
     * @param ImgType BufferedImage对象图片数据存储的格式
     * @return
     * @throws IOException
     */
    public static InputStream transformBufferedToInputStream(BufferedImage bufferedImage, String ImgType) throws IOException {
        InputStream result = null;
        ByteArrayOutputStream osBuf = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, ImgType, osBuf);
        result = new ByteArrayInputStream(osBuf.toByteArray());
        return result;
    }
    /**
     * 获取默认的二维码格式参数集（其包含这些内容：{EncodeHintType.ERROR_CORRECTION:二维码的校错级别, EncodeHintType.CHARACTER_SET:编码格式}）
     *
     * @return 返回一个包含默认键值对的map，其内包含了生成一个二维码所需要的必要格式参数及其默认值
     */
    public static Map<EncodeHintType, Object> getDefaultArgsModule() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, DEFAULT_ERRORCORRECTIONLEVEL);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARACTER_SET);
        return hints;
    }

    /**
     * 生成指定字符串的二维码数据，其中数据以BufferedImage对象的形式返回。
     *
     * @param url 需要转化为二维码的字符串
     * @param width 要生成的二维码图片的宽度
     * @param height 要生成的二维码图片的高度
     * @param qrcolor 要生成的二维码图片中二维码的颜色
     * @param bgcolor 要生成的二维码图片中二维码的背景颜色
     * @return
     * @throws WriterException
     * @throws NullPointerException
     */
    public static BufferedImage create(String url, int width, int height, int qrcolor, int bgcolor) throws WriterException, NullPointerException {
        if (null == url) throw new NullPointerException("url is null");
        //获取默认的二维码格式参数集
        Map<EncodeHintType, Object> hints = getDefaultArgsModule();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，类型格式参数
        BitMatrix bm = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        int w = bm.getWidth();
        int h = bm.getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // 用二维码数据创建Bitmap图片
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                image.setRGB(x, y, bm.get(x, y) ? qrcolor : bgcolor);
            }
        }
        return image;
    }

    /**
     * 生成指定字符串的二维码，并且使用模块的默认参数，参数简介请看其重载函数
     * @param url 需要转化为二维码的字符串
     * @return
     * @throws WriterException
     */
    public static BufferedImage create(String url) throws WriterException {
        return create(url, WIDTH, HEIGHT, BRUSHCOLOR, BACKGROUNDCOLOR);
    }

    /**
     * s生成指定url且带有logo的二维码
     *
     * @param url 需要转化为二维码的字符串
     * @param logoInputStream logo的input stream对象
     * @param width 要生成的二维码图片的宽度
     * @param height 要生成的二维码图片的高度
     * @param brushColor 要生成的二维码图片中二维码的颜色
     * @param backgroundColor 要生成的二维码图片中二维码的背景颜色
     * @return
     * @throws WriterException
     * @throws NullPointerException
     * @throws IOException
     */
    public static BufferedImage createWithLogo(String url, InputStream logoInputStream, int width, int height, int brushColor, int backgroundColor) throws WriterException, NullPointerException, IOException {
        if (null == logoInputStream) throw new NullPointerException("logoInputStream is null");
        //创建指定url所对应的二维码BufferedImage对象
        BufferedImage image = create(url, width, height, brushColor, backgroundColor);
        // 构建绘图对象
        Graphics2D g = image.createGraphics();
        // 读取Logo图片中的数据
        BufferedImage logo = ImageIO.read(logoInputStream);
        if (null == logo) throw new IOException("给定文件的数据格式读取失败：可能你指定的文件后缀是正确的，但其内部数据格式可能是不正确的");
        //设置logo的大小,这里设置为二维码图片的20%,过大会盖掉二维码
        int widthLogo = logo.getWidth(null) > image.getWidth() * 3 / 10 ? (image.getWidth() * 3 / 10) : logo.getWidth(null),
                heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10) : logo.getWidth(null);
        // logo放在中心
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        // logo放在右下角
        // int x = (image.getWidth() - widthLogo);
        // int y = (image.getHeight() - heightLogo);
        //开始绘制图片
        g.drawImage(logo, x, y, widthLogo, heightLogo, null);
        g.dispose();
        logo.flush();
        image.flush();
        return image;
    }

    /**
     * 使用默认参数生成指定url和logo的二维码， 参数简介请看其重载函数
     * @param url
     * @param logoInputStream
     * @return
     * @throws WriterException
     * @throws NullPointerException
     * @throws IOException
     */
    public static BufferedImage createWithLogo(String url, InputStream logoInputStream) throws WriterException, NullPointerException, IOException {
        return createWithLogo(url, logoInputStream, WIDTH, HEIGHT, BRUSHCOLOR, BACKGROUNDCOLOR);
    }

    /**
     * 生成指定url，并且带有logo和文字的二维码。最终生成的图片分成两部分，上部分是带logo的二维码，下部分是附带的文字。
     * @param url 需要转化为二维码的字符串
     * @param logoInputStream logo的input stream对象
     * @param sentence 二维码所要附带的文字，如果文字过多，超出部分将被隐藏
     * @param width 要生成的二维码图片的宽度
     * @param height 要生成的二维码图片的高度
     * @param brushColor 要生成的二维码图片中二维码的颜色
     * @param backgroundColor 要生成的二维码图片中二维码的背景颜色
     * @param sentenceHeight 文字部分的高度
     * @param sentenceColor 文字颜色
     * @param sentenceBackGroundColor 文字的背景颜色
     * @param sentenceFont 文字的字体
     * @return
     * @throws WriterException
     * @throws NullPointerException
     * @throws IOException
     */
    public static BufferedImage createWithLogoAndText(String url, InputStream logoInputStream, String sentence, int width, int height, int brushColor, int backgroundColor, int sentenceHeight, Color sentenceColor, Color sentenceBackGroundColor, Font sentenceFont) throws WriterException, NullPointerException, IOException {
        if (null == sentence) throw new NullPointerException("text is null");
        //创建指定url和logo所对应的二维码BufferedImage对象
        BufferedImage image = createWithLogo(url, logoInputStream, width, height, brushColor, backgroundColor);
        //新建一个图片，把带logo的二维码复制到里面后再绘制文字
        BufferedImage outImage = new BufferedImage(width, height+sentenceHeight,  BufferedImage.TYPE_INT_RGB   );
        //创建新图片的画板
        Graphics2D outg = outImage.createGraphics();
        //绘制背景颜色（间接也成为文字的背景颜色）
        outg.setColor(sentenceBackGroundColor);
        outg.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
        //将二维码图片数据复制到新的BufferedImage对象上
        outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        //绘制文字
        //设置字体颜色
        outg.setColor(sentenceColor);
        //设置字体

        outg.setFont(sentenceFont);
        int strWidth = outg.getFontMetrics().stringWidth(sentence);
        int x = 0;
        if (strWidth < outImage.getWidth()) {
            x = (outImage.getWidth() - strWidth)/ 2;
        }
        //绘制文字
        outg.drawString(sentence, x, outImage.getHeight()-10 );
        outg.dispose();
        outImage.flush();
        image = outImage;
        return image;
    }

    /**
     * 使用默认参数，生成带有logo和文字的二维码。参数简介请看其重载函数。
     * @param url
     * @param logoInputStream
     * @param sentence
     * @return
     * @throws WriterException
     * @throws NullPointerException
     * @throws IOException
     */
    public static BufferedImage createWithLogoAndText(String url, InputStream logoInputStream, String sentence) throws WriterException, NullPointerException, IOException {
        return createWithLogoAndText(url, logoInputStream, sentence, WIDTH, HEIGHT, BRUSHCOLOR, BACKGROUNDCOLOR, DEFAULT_SENTENCE_HEIGHT, DEFAULT_SENTENCE_COLOR, DEFAULT_SENTENCE_BACKGROUND_COLOR, DEFAULT_SENTENCE_FONT);
    }

}
