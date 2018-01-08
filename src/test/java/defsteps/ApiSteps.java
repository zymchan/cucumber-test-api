package defsteps;

import api.ApiTool;
import com.jayway.jsonpath.JsonPath;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiSteps {

    private ApiTool apiTool = new ApiTool();
    private HashMap<String,String> testData = new HashMap<String,String>();




    @Before
    public void initApiTool(){
        apiTool.initInterceptor();
        apiTool.initAuthorization();
    }


    @Given("^initialize test data keys \"([^\"]*)\" values \"([^\"]*)\":$")
    public void initializeTestDataKeysValues(String keys, String values) throws Throwable {
        testData.clear();
        Pattern pattern=Pattern.compile("<.*?>");
        Matcher keysMatcher=pattern.matcher(keys);
        Matcher valuesMatcher=pattern.matcher(values);
        List<String> keyLists = new ArrayList<String>();
        List<String> valueLists = new ArrayList<String>();
        while (keysMatcher.find()){
            keyLists.add(keysMatcher.group());
        }
        while (valuesMatcher.find()){
            valueLists.add(valuesMatcher.group());
        }
        for(int i=0;i<keyLists.size();i++){
            String k = keyLists.get(i).substring(1,keyLists.get(i).length()-1);
            String v = valueLists.get(i).substring(1,valueLists.get(i).length()-1);
            testData.put(k,v);
        }
    }

    @And("^the headers are:$")
    public void theHeadersAre(DataTable table) throws Throwable {
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++){
            String name = lists.get(i).get(0);
            String value = lists.get(i).get(1);
            apiTool.setHeader(expressionToString(name),expressionToString(value));
        }
    }

    @And("^add header value:$")
    public void addHeaderValue(DataTable table) throws Throwable {
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++){
            String name = lists.get(i).get(0);
            String value = lists.get(i).get(1);
            apiTool.addHeader(expressionToString(name),expressionToString(value));
        }
    }

    @And("^the request url is :$")
    public void theRequestUrlIs(String url) throws Throwable {
        url = replaceExpressionInString(url);
        apiTool.setUrl(url);
    }

    @And("^the request params are:$")
    public void theRequestParamsAre(DataTable table) throws Throwable {
        String params ="";
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++){
            String k = lists.get(i).get(0);
            String v = lists.get(i).get(1);
            params+="&"+expressionToString(k)+"="+expressionToString(v);
        }
        apiTool.setParams(params.substring(1));
    }

    @When("^I send a GET request$")
    public void iSendAGETRequest() throws Throwable {
        apiTool.sendGetRequest();
    }

    @Then("^the response status should be \"([^\"]*)\"$")
    public void theResponseStatusShouldBe(String code) throws Throwable {
        Assert.assertEquals(code,apiTool.response.code()+"");
    }

    @And("^the JSON response \"([^\"]*)\" should equals \"([^\"]*)\"$")
    public void theJSONResponseShouldEquals(String xpath, String expect) throws Throwable {
        String json = apiTool.responseBody;
        String actual = JsonPath.read(json, expressionToString(xpath));
        Assert.assertEquals(expressionToString(expect),actual);
    }

    @And("^the JSON response \"([^\"]*)\" should contain \"([^\"]*)\"$")
    public void theJSONResponseShouldContain(String xpath, String expect) throws Throwable {
        String json = apiTool.responseBody;
        String actual = JsonPath.read(json, expressionToString(xpath));
        Assert.assertTrue(actual.contains(expressionToString(expect)));
    }

    @And("^the JSON response \"([^\"]*)\" should startWith \"([^\"]*)\"$")
    public void theJSONResponseShouldStartWith(String xpath, String expect) throws Throwable {
        String json = apiTool.responseBody;
        String actual = JsonPath.read(json, expressionToString(xpath));
        Assert.assertTrue(actual.startsWith(expressionToString(expect)));
    }

    @And("^the JSON response \"([^\"]*)\" should endWith \"([^\"]*)\"$")
    public void theJSONResponseShouldEndWith(String xpath, String expect) throws Throwable {
        String json = apiTool.responseBody;
        String actual = JsonPath.read(json, expressionToString(xpath));
        Assert.assertTrue(actual.endsWith(expressionToString(expect)));
    }

    @And("^the postString is:$")
    public void thePostBodyIs(String postString) throws Throwable {
       postString = replaceExpressionInString(postString);
       apiTool.setPostBody(postString);
    }

    @When("^I send a POST request$")
    public void iSendAPOSTRequest() throws Throwable {
        apiTool.sendPostRequest();
    }

    @And("^the path of postStream file is \"([^\"]*)\"$")
    public void thePostStreamFileIs(String filePath) throws Throwable {
        filePath = replaceExpressionInString(filePath);
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        apiTool.setPostBody(bytes);
    }

    @And("^the path of file is \"([^\"]*)\"$")
    public void thePathOfFileIs(String filePath) throws Throwable {
        filePath = replaceExpressionInString(filePath);
        File file = new File(filePath);
        apiTool.setPostBody(file);
    }

    @And("^the form data is:$")
    public void theFormDataIs(DataTable table) throws Throwable {
        HashMap<String,String> map = new HashMap<>();
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++){
            String k = lists.get(i).get(0);
            String v = lists.get(i).get(1);
            map.put(expressionToString(k),expressionToString(v));
        }
        apiTool.setPostBody(map);
    }







    //replace the expression in string,like abc/{exp}/cde
    private String replaceExpressionInString(String str){
        Set<String> keys = testData.keySet();
        for(String key : keys){
            String var = "{"+key+"}";
            if(str.contains(var)){
                String regex = "\\"+var;
                str = str.replaceAll(regex,testData.get(key));
            }
        }
        return str;
    }

    //change the expression to string, like {exp}
    private String expressionToString(String str){
        String formattedStr = str;
        if(str.startsWith("{")&&str.endsWith("}")){
            String k = str.substring(1,str.length()-1);
            if(testData.containsKey(k)){
                formattedStr = testData.get(k);
            }
        }
        return formattedStr;
    }

    @And("^the string part is :$")
    public void theStringPartIs(DataTable table) throws Throwable {
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++){
            String k = lists.get(i).get(0);
            String v = lists.get(i).get(1);
            apiTool.addStringPart(expressionToString(k),expressionToString(v));
        }
    }


    @And("^the file part is :$")
    public void theFilePartIs(DataTable table) throws Throwable {
        List<List<String>> lists = table.raw();
        for(int i =1; i<lists.size();i++) {
            String name = lists.get(i).get(0);
            String mediaType = lists.get(i).get(1);
            String filePath = lists.get(i).get(2);
            name = expressionToString(name);
            mediaType = expressionToString(mediaType);
            filePath = replaceExpressionInString(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType),new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            ApiTool.FilePart filePart = new ApiTool.FilePart(name,fileName,requestBody);
            apiTool.addFilePart(filePart);
        }
    }


}
