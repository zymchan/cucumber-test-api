package api;

import okhttp3.*;
import util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import static java.lang.Class.forName;

public class ApiTool {

    final static String CONFIG_PATH="src/test/resources/config.properties";
    final static Properties configs = StringUtil.readPropertiesFile(CONFIG_PATH);

    public Response response;
    public String responseBody;

    private String uri ="";
    private RequestBody requestBody;


    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    Request.Builder requestBuilder = new Request.Builder();
    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);


    public void initInterceptor(){
        for (String key : configs.stringPropertyNames()) {
            if (key.contains("interceptor")) {
                String config = configs.getProperty(key);
                String type = config.split("@")[0];
                String className = "api.interceptor." + config.split("@")[1];
                if ("Application".equalsIgnoreCase(type)) {
                    try {
                        Class<?> t = forName(className);
                        clientBuilder.addInterceptor((Interceptor) t.newInstance());
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                } else if ("Network ".equalsIgnoreCase(type)) {
                    try {
                        Class<?> t = forName(className);
                        clientBuilder.addNetworkInterceptor((Interceptor) t.newInstance());
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void initAuthorization(){
        if(configs.containsKey("Authorization")){
            clientBuilder.authenticator(new Authenticator() {
                @Override public Request authenticate(Route route, Response response) throws IOException {
                    if (response.request().header("Authorization") != null) {
                        return null; // Give up, we've already attempted to authenticate.
                    }
//                    System.out.println("Authenticating for response: " + response);
//                    System.out.println("Challenges: " + response.challenges());
                    String credential = configs.getProperty("Authorization");
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                }
            });
        }
    }


    //set value to the header, if the header had an old value, it will be replaced
    public void setHeader(String key, String value){
        requestBuilder.header(key,value);
    }

    //add value to the header, do not remove the old one
    public void addHeader(String key, String... values){
        for(String value : values){
            requestBuilder.addHeader(key,value);
        }
    }

    public void setUrl(String url){
        if(!url.startsWith("http")){
            url=configs.getProperty("host")+url;
        }
        uri = url;
    }

    public void setParams(String params){
        uri= uri+"?"+params;
    }

    public void sendGetRequest() throws IOException {
        Request request = requestBuilder.url(uri).get().build();
        response = clientBuilder.build().newCall(request).execute();
        responseBody =response.body().string();
    }


    //post string smaller than 1MB
    public void setPostBody(String postString){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, postString);
    }

    //post string bigger than 1MB
    public void setPostBody(byte[] content){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, content);
    }

    //post a file
    public void setPostBody(File file){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
    }

    //post form data
    public void setPostBody(HashMap<String,String> map){
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keys = map.keySet();
        for(String key : keys){
            builder.add(key,map.get(key));
        }
        requestBody=builder.build();
    }

    public void sendPostRequest() throws IOException {
        if(requestBody==null){
            requestBody = multipartBuilder.build();
        }
        Request request = requestBuilder.url(uri).post(requestBody).build();
        response = clientBuilder.build().newCall(request).execute();
        responseBody =response.body().string();
    }


    public void addStringPart(String name,String value){
        multipartBuilder = multipartBuilder.addFormDataPart(name,value);
    }

    public void addFilePart(FilePart filePart){
        multipartBuilder = multipartBuilder.addFormDataPart(filePart.getName(),filePart.getFileName(),filePart.getBody());
    }



    public static class FilePart{
        private String name;
        private String fileName;
        private RequestBody body;

        public FilePart(){}

        public FilePart(String name,String fileName, RequestBody body){
            this.name=name;
            this.fileName=fileName;
            this.body=body;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public RequestBody getBody() {
            return body;
        }

        public void setBody(RequestBody body) {
            this.body = body;
        }
    }
}
