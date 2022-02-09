package api.utils;

import api.testcase.ut.LoginTest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.*;

public class YamlUtil {

//    private static Map <String, Object> yamlfile;
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    private static  Map <String, Object> load(String yamlName){
        String yamlFileName = "interfaceDefinition/" + yamlName + ".yml";
        logger.info("current load yamle file path is {}", yamlFileName);
        InputStream in = null;
        Map <String, Object> yamlfile = null;
        try{
            Yaml yaml = new Yaml();
            in = YamlUtil.class.getClassLoader().getResourceAsStream(yamlFileName);;
            yamlfile = yaml.load(in);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                in.close();
                return yamlfile;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
       return yamlfile;
    }

    public static String getConfig(String yamlName, String prefix){
        if(prefix==null || "".equals(prefix.trim())){
            logger.warn("input yaml file 's prefix is empty");
            return "";
        }
        //加载yaml文件
        Map <String, Object> yamlfile = load(yamlName);
        //获取key值的层级关系current
        String[] keys = prefix.split("\\.");
        Object temp = yamlfile;
        for (String k : keys){
            if (temp instanceof Map){
                boolean containsKey = ((Map) temp).containsKey(k);
                if (!containsKey) {
                    throw new RuntimeException(String.format("当前Yaml文件不存在对应的key值：[%s]",k));
                }
                temp = ((Map)temp).get(k);
            } else if(temp instanceof List){
                if (isNumeric(k)) {
                    int size = ((List<?>) temp).size();
                    int intK = Integer.parseInt(k);
                    if (intK >=size ) {
                        throw new RuntimeException(String.format("当前层级类型为List, [%s]获取List数据下标越界",k));
                    }
                    temp= ((List) temp).get(Integer.parseInt(k));
                }else{
                    throw new RuntimeException(String.format("当前层级类型为List,不能使用[%s]获取子集数据",k));
                }
            }else{
                throw new RuntimeException("暂时没有解析该类型或不支持再次解析");
            }
        }
        logger.info("the yaml file name is : {}, prefix is : {}, value is : {}", yamlName, prefix, temp.toString());
        return temp.toString();
    }

    private static boolean isNumeric(final CharSequence cs){
        if (cs == null || cs.length() == 0) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
