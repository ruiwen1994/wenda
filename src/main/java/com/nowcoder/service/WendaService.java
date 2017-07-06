package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * Created by ruiwen on 2017/7/5.
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message: " + String.valueOf(userId);
    }
}
