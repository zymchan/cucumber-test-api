package util;

import java.io.File;

public class CreateFileUtil {

    public static void createDir(String dir){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
        }
    }


}
