package com.tungbt.apigateway.utils;

import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import com.tungbt.apigateway.exception.PathNotFoundException;
import static com.tungbt.apigateway.configurations.ApplicationConfiguration.Routes.PREFIX_REPLACE_PATH;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PathMapUtility {

    private static final String PREFIX_SLASH = "/";

    public static String findMatchedPath(Map<HttpMethod,List<Map<String, String>>> configPath, String method, String path){
        try {
            path = convertPath(path);
            List<Map<String, String>> listPath = configPath.get(HttpMethod.valueOf(method));
            return fitsUrlsTemplate(path, listPath);
        } catch(NullPointerException e){
            throw new PathNotFoundException();
        }
    }

    private static String fitsUrlsTemplate(String path, List<Map<String, String>> configList) {
        if(!CollectionUtils.isEmpty(configList))
            for(Map<String, String> configPath : configList){
                for(String key: configPath.keySet()) {
                    if (fitsUrlTemplate(path, "/" + key)) return configPath.get(key).replace(PREFIX_REPLACE_PATH, path);
                }
            }
        throw new PathNotFoundException();
    }

    private static boolean fitsUrlTemplate(String path, String template) {
        return FileSystems.getDefault()
                .getPathMatcher("glob:" + template)
                .matches(Paths.get(path));
    }

    private static String convertPath(String path){
        if (path.endsWith("/")){
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }

    private Integer countPrefix(String path){
        return path.length() - path.replace(PREFIX_SLASH, "").length();
    }

}
