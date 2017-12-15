
package com.qs.arm.http;

import android.support.annotation.Nullable;

import com.qs.arm.di.module.ConfigModule;
import com.qs.arm.utils.CharacterHandler;
import com.qs.arm.utils.ZipHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;


/**
 * 解析框架中的网络请求和响应结果,并以日志形式输出,调试神器
 * 可使用 {@link ConfigModule.Builder#printHttpLogLevel(Level)} 控制或关闭日志
 * @author 华清松
 */
@Singleton
public class RequestInterceptor implements Interceptor {
    private GlobalHttpHandler mHandler;
    private final Level printLevel;

    public enum Level {
        /**
         * 不打印log
         */
        NONE,
        /**
         * 只打印请求信息
         */
        REQUEST,
        /**
         * 只打印响应信息
         */
        RESPONSE,
        /**
         * 所有数据全部打印
         */
        ALL
    }

    @Inject
    public RequestInterceptor(@Nullable GlobalHttpHandler handler, @Nullable Level level) {
        this.mHandler = handler;
        if (level == null) {
            printLevel = Level.ALL;
        } else {
            printLevel = level;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean logRequest = printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.REQUEST);

        if (logRequest) {
            boolean hasRequestBody = request.body() != null;
            // 打印请求信息
            Timber.tag(getTag(request, "Request_Info")).w("Params : 「 %s 」%nConnection : 「 %s 」%nHeaders : %n「 %s 」"
                    , hasRequestBody ? parseParams(request.newBuilder().build().body()) : "Null"
                    , chain.connection()
                    , request.headers());
        }

        boolean logResponse = printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.RESPONSE);

        long t1 = logResponse ? System.nanoTime() : 0;
        Response originalResponse;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Timber.w("Http Error: " + e);
            throw e;
        }
        long t2 = logResponse ? System.nanoTime() : 0;

        if (logResponse) {
            String bodySize = originalResponse.body().contentLength() != -1 ? originalResponse.body().contentLength() + "-byte" : "unknown-length";
            // 打印响应时间以及响应头
            Timber.tag(getTag(request, "Response_Info")).w("Received response in [ %d-ms ] , [ %s ]%n%s"
                    , TimeUnit.NANOSECONDS.toMillis(t2 - t1), bodySize, originalResponse.headers());
        }

        // 打印响应结果
        String bodyString = printResult(request, originalResponse.newBuilder().build(), logResponse);

        // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
        if (mHandler != null) {
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param response
     * @param logResponse
     * @return
     * @throws IOException
     */
    @Nullable
    private String printResult(Request request, Response response, boolean logResponse) throws IOException {
        // 读取服务器返回的结果
        ResponseBody responseBody = response.body();
        String bodyString = null;
        if (isParseAble(responseBody.contentType())) {
            try {
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                // 获取content的压缩类型
                String encoding = response
                        .headers()
                        .get("Content-Encoding");

                Buffer clone = buffer.clone();


                // 解析response content
                bodyString = parseContent(responseBody, encoding, clone);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (logResponse) {
                Timber.tag(getTag(request, "Response_Result")).w(isJson(responseBody.contentType()) ?
                        CharacterHandler.jsonFormat(bodyString) : isXml(responseBody.contentType()) ?
                        CharacterHandler.xmlFormat(bodyString) : bodyString);
            }

        } else {
            if (logResponse) {
                Timber.tag(getTag(request, "Response_Result")).w("This result isn't parsed");
            }
        }
        return bodyString;
    }


    private String getTag(Request request, String tag) {
        return String.format(" [%s] 「 %s 」>>> %s", request.method(), request.url().toString(), tag);
    }


    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        // content使用gzip压缩
        String gZip = "gzip";
        String zLip = "zlib";
        if (encoding != null && gZip.equalsIgnoreCase(encoding)) {
            // 解压
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));
            // content使用ZLib压缩
        } else if (encoding != null && zLip.equalsIgnoreCase(encoding)) {
            // 解压
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));
            // content没有被压缩
        } else {
            return clone.readString(charset);
        }
    }

    /**
     * 解析请求服务器的请求参数
     *
     * @param body
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String parseParams(RequestBody body) throws UnsupportedEncodingException {
        if (isParseAble(body.contentType())) {
            try {
                Buffer requestBuffer = new Buffer();
                body.writeTo(requestBuffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                if (charset == null) {
                    throw new NullPointerException("charset is not be null");
                }
                return URLDecoder.decode(requestBuffer.readString(charset), convertCharset(charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "This params isn't parsed";
    }

    /**
     * 是否可以解析
     *
     * @param mediaType
     * @return
     */
    public static boolean isParseAble(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        return mediaType.toString().toLowerCase().contains("text")
                || isJson(mediaType) || isForm(mediaType)
                || isHtml(mediaType) || isXml(mediaType);
    }

    public static boolean isJson(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("json");
    }

    public static boolean isXml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("xml");
    }

    public static boolean isHtml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("html");
    }

    public static boolean isForm(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("x-www-form-urlencoded");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1) {
            return s;
        }
        return s.substring(i + 1, s.length() - 1);
    }
}
