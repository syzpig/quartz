package com.task.quartz.quartz.enums;

import java.util.Map;

public interface CodeList {

    Map<String, String> getMap(String bizType);
    
    Map<String, String> toMap();    
}
