package com.tungbt.apigateway.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import com.tungbt.apigateway.utils.ComparatorUtility;
import javax.annotation.PostConstruct;
import static com.tungbt.apigateway.configurations.ApplicationConfiguration.Routes.PREFIX_REPLACE_PATH;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationConfiguration {

     Map<String, Object> payment;
     Map<String, Object> acquiring;
     Map<String, Object> coreservice;
     Map<String, Routes> routes;

     @PostConstruct
     public void initData(){
          routes.forEach((api,r) ->
               r.getRouteInfos().forEach((method,list) -> {
                            list.forEach(map -> {
                                 for (String k : map.keySet())
                                      map.put(k, map.remove(k).replace(k, PREFIX_REPLACE_PATH));
                            });
                            list.sort(ComparatorUtility.mapComparator().reversed());
                       })
               );
     }

     @Data
     public static class Routes {
          public static final String PREFIX_REPLACE_PATH = "#PATH#";
          private String clientId;
          Map<String,Object> credentials;
          private Map<HttpMethod,List<Map<String, String>>> routeInfos;
     }

}
