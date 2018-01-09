package api.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import util.LoggerUtil;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    public static Logger log = LoggerUtil.getLog(LoggingInterceptor.class);
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        log.info("==============Request===========================================================");
        log.info(String.format("Sending request %s on %s%nMethod: %s%n%s%n",
                request.url(), chain.connection(), request.method(),request.headers()));
//        System.out.println("==============Request===========================================================");
//        System.out.println(String.format("Sending request %s on %s%nMethod: %s%n%s%n",
//                request.url(), chain.connection(), request.method(),request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        log.info("==============Response=========================================================");
        log.info(String.format("Received response for %s in %.1fms%n%s%n",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        log.info("Response-status: "+response.code()+" / "+response.message()+"\n");

        log.info("===============================================================================");
//        System.out.println("==============Response=========================================================");
//        System.out.println(String.format("Received response for %s in %.1fms%n%s%n",
//                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//
//        System.out.println("Response-status: "+response.code()+" / "+response.message()+"\n");
////        System.out.println("Response-Body: \n"+response.body().string());
//        System.out.println("===============================================================================");
        return response;
    }
}