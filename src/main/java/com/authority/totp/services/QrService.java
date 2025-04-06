package com.authority.totp.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 生成二维码服务类
 */
@Service
public class QrService {

    /**
     * 图像格式
     */
    final static String IMG_FORMAT = "png";
    /**
     * 图像大小
     */
    final static Integer SIZE = 300;

    /**
     * 根据数据生成二维码
     * @param data 数据
     * @return 二维码图片的Base64编码字符串
     */
    public String generateQr(String data) {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        try {
            // 编码数据生成二维码矩阵
            BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.QR_CODE, SIZE, SIZE);
            // 将二维码矩阵转换为BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // 创建ByteArrayOutputStream用于存储图像数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 将BufferedImage写入ByteArrayOutputStream
            ImageIO.write(bufferedImage, IMG_FORMAT, baos);
            // 将图像数据转换为Base64编码字符串
            return "data:image/png;base64, ".concat(Base64.encodeBase64String(baos.toByteArray()));
        } catch (Exception e) {
            // 如果发生异常，抛出RuntimeException
            throw new RuntimeException(e);
        }
    }
}
