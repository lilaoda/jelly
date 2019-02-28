package lhy.lhylibrary.http;


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
import lhy.lhylibrary.http.interceptor.HeadIntercepter;
import lhy.lhylibrary.utils.FileUtils;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp管理类，可添加缓存，添加公共请求参数
 */
public class OkhttpManager {

    private static OkhttpManager instance;
    private final OkHttpClient.Builder mOkHttpBuilder;

    private static final int CONNECTIMEOUT = 10000;
    private static final int READTIMEOUT = 10000;

    private OkhttpManager() {

        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeadIntercepter())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));

    }

    public static OkhttpManager getInstance() {
        if (instance == null) {
            instance = new OkhttpManager();
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

//读取证书文件
//public static InputStream getCertificate() {
//    InputStream inputStream = null;
//    try {
////            inputStream = BaseApplication.getInstance().getAssets().open("1730424__sxjdfreight.com.pem");
////        inputStream = BaseApplication.getInstance().getResources().openRawResource(R.raw.sxjd);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return inputStream;
//}

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

    public SSLSocketFactory setDoubleCertificates(InputStream... certificates) {
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
            clientKeyStore.load(LhyApplication.getInstance().getAssets().open("keystore.bks"), "brucegao".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, "brucegao".toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }
}