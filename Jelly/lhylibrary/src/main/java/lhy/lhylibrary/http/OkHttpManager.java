package lhy.lhylibrary.http;


import androidx.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import lhy.lhylibrary.base.LhyApplication;
import lhy.lhylibrary.http.interceptor.CacheIntercepter;
import lhy.lhylibrary.http.interceptor.HeadInterceptor;
import lhy.lhylibrary.utils.FileUtils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp管理类，可添加缓存，添加公共请求参数
 */
public class OkHttpManager {

    private static OkHttpManager instance;
    private final OkHttpClient.Builder mOkHttpBuilder;

    private static final int CONNECT_TIME_OUT = 3000;
    private static final int READ_TIME_OUT = 3000;

    private OkHttpManager() {

        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeadInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));

    }

    public static OkHttpManager getInstance() {
        if (instance == null) {
            instance = new OkHttpManager();
        }
        return instance;
    }


    public OkHttpClient getOKhttp() {
        return mOkHttpBuilder.build();
    }

    public OkHttpClient getCacheOKhttp() {
        return mOkHttpBuilder
                .cache(new Cache(FileUtils.getCacheFile(LhyApplication.getContext(), "file_cache"), 1024 * 1024 * 100))
                .addInterceptor(new CacheIntercepter(LhyApplication.getContext()))
                .build();
    }
//
//    //提交单文件表单示例
//    public void oneFormFileSample(String imgPath) {
//        File file = new File(imgPath);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
//        String descriptionString = "This is a description";
//    }
//
//
//    public MultipartBody getMultipartBody(List<String> imgPaths) {
//        final MultipartBody.Builder builder = new MultipartBody.Builder();
//        if (imgPaths != null && imgPaths.size() > 0) {
//            for (int i = 0; i < imgPaths.size(); i++) {
//                File file = new File(imgPaths.get(i));
//                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
////            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//                String des = "photo" + i + 1;
//                builder.addFormDataPart(des, file.getName(), requestBody);
//            }
//        }
//        builder.setType(MultipartBody.FORM);
//        return builder.build();
//    }
//
//    public List<MultipartBody.Part> getMultipartBodyPartList(List<String> imgPaths) {
//        List<MultipartBody.Part> list = new ArrayList<>();
//        if (imgPaths != null && imgPaths.size() > 0) {
//            for (int i = 0; i < imgPaths.size(); i++) {
//                File file = new File(imgPaths.get(i));
//                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
////            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//                String des = "aFile";
//                MultipartBody.Part part = MultipartBody.Part.createFormData(des, file.getName(), requestBody);
//                list.add(part);
//            }
//        }
//        return list;
//    }


    /**
     *   读取证书文件
     */
    public static InputStream getCertificate(@RawRes int rawId) {
        return LhyApplication.getInstance().getResources().openRawResource(rawId);
    }

    /**
     * 设置SSL证书
     * @param certificates 证书输入流
     * @return SSLSocketFactory
     */
    public SSLSocketFactory setCertificates(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /**
     * 与服务端进行双向证书认证
     * @param certificates  证书输入流
     * @return SSLSocketFactory
     */
    public SSLSocketFactory setDoubleCertificates(String keyPwd,InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //初始化keystore
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(LhyApplication.getInstance().getAssets().open("keystore.bks"), keyPwd.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, keyPwd.toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }
}