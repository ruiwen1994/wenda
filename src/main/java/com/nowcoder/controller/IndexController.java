package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Array;
import java.util.*;

/**
 * Created by ruiwen on 2017/7/3.
 */
@Controller
public class IndexController {
    //其实是试验页控制层

    private static final Logger logger = LoggerFactory.getLogger(com.nowcoder.aspect.LogAspect.class);

    @RequestMapping(path = {"/home"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        logger.info("VISIT HOME");
        if(httpSession.getAttribute("msg") != null){
            return "Hello Nowcoder "+ httpSession.getAttribute("msg");

        }else
        return "Hello Nowcoder ";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userID,
                          @PathVariable("groupId") String groupID,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key",defaultValue = "key",required = false) String key){
        return String.format("Profile Page of %s / %d, t:%d k:%s", groupID, userID, type, key);
    }

    @RequestMapping(path = {"/vm" }, method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","vvvv1");
        List<String> colors = Arrays.asList(new String[]{"Red","Green","Blue"});
        model.addAttribute("Colors",colors);

        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; ++i){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);
        model.addAttribute("user", new User("zrw"));

        return "home";
    }

    @RequestMapping(path = {"/request" }, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,
                          HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionID){
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE: " + sessionID);
        Enumeration<String> headNames = request.getHeaderNames();
        while(headNames.hasMoreElements()){
            String name = headNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if(request.getCookies() != null){
            for (Cookie cookie: request.getCookies())
            sb.append("Cookie:" + cookie.getName() + " value: " +  cookie.getValue()+"<br>");
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");

        response.addHeader("homemadeHeaderID","nosense");
        response.addCookie(new Cookie (" nowcodercookieID","nonsense"));
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code ,
                                 HttpSession httpSession){
        httpSession.setAttribute("msg", "jump from redirect.");
        RedirectView redirectView = new RedirectView("/",true);
        if(code == 301){
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("错误参数");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error" + e.getMessage();
    }

}
