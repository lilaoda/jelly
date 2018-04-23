package lhy.zxinglibrary.zxing.decode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/4/8 下午11:22
 * 描述:解析二维码图片
 */
public class QRCodeDecoder {
    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

    static {
        List<BarcodeFormat> allFormats = new ArrayList<BarcodeFormat>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);

        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    }

    private QRCodeDecoder() {
    }

    /**
     * 解析二维码图片
     *
     * @param bitmap   要解析的二维码图片
     * @param delegate 解析二位码图片的代理
     */
    public static void decodeQRCode(final Bitmap bitmap, final Delegate delegate) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
                    return result.getText();
//                    LuminanceSource source1 = new PlanarYUVLuminanceSource(
//                            rgb2YUV(bitmap), bitmap.getWidth(),
//                            bitmap.getHeight(), 0, 0, bitmap.getWidth(), bitmap.getHeight(), false);
//                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source1));
//                    Result decode = new MultiFormatReader().decode(binaryBitmap, HINTS);
//                    return decode.getText();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (delegate != null) {
                    if (TextUtils.isEmpty(result)) {
                        delegate.onDecodeQRCodeFailure();
                    } else {
                        delegate.onDecodeQRCodeSuccess(result);
                    }
                }
            }
        }.execute();
    }

    public static String decodeQRCodeSync(final Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface Delegate {
        /**
         * 解析二维码成功
         *
         * @param result 从二维码中解析的文本，如果该方法有被调用，result不会为空
         */
        void onDecodeQRCodeSuccess(String result);

        /**
         * 解析二维码失败
         */
        void onDecodeQRCodeFailure();
    }

    private static byte[] rgb2YUV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    public static Bitmap drawableToBitmap(Bitmap sourceBitmap) {
        Canvas canvas = new Canvas(sourceBitmap.copy(sourceBitmap.getConfig(), true));
        // 白色底色   应对透明图
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return sourceBitmap;
    }
}