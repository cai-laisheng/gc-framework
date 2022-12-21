package com.allen.sys.filter;

import com.allen.sys.model.po.SysUser;
import com.allen.sys.service.SystemService;
import com.allen.sys.utils.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Configuration
public class CheckFilter implements HandlerInterceptor {
    private Logger LOGGER = LoggerFactory.getLogger(CheckFilter.class);
    private static  Map<String,Long> userLoginMap = new HashMap<String,Long>();

    @Autowired
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        LOGGER.info("---------jinlai---"+request.getRequestURL());
        LOGGER.info("---------getRequestURI---"+request.getRequestURI());
        LOGGER.info("---------getServletPath---"+request.getServletPath());
        if (request.getMethod().equals(RequestMethod.OPTIONS.name()) ) {
            return true;
        }
        if(request.getServletPath().equals("/admin/auth/ssoLogin")){
            return true;
        }else{
//            获取用户数据
            String token = request.getHeader("Authorization");
            if (StringUtils.isBlank(token)){
                return true;
            }
            //验证token格式
//            if(!TokenUtils.verify(token)){
//                System.out.println("token格式不准确");
//                return false;
//            }
            if(userLoginMap.get(token)==null){
                userLoginMap.put(token,System.currentTimeMillis());
                SysUser userMsgByToken = systemService.getUserMsgByToken(token);
                LOGGER.info("用户信息:{}",userMsgByToken);
                // 放入本地线程
                ThreadLocalUtil.set(userMsgByToken);
            }else{
                Long time = userLoginMap.get(token);
                if(time<System.currentTimeMillis()-30*60*1000){
//                    删除map数据
                    Iterator<String> iter = userLoginMap.keySet().iterator();
                    while(iter.hasNext()) {
                        String key = iter.next();
                        if(key!=null){
                            if (key == token || key.equals(token)) {
                                iter.remove();
                            }
                        }
                    }
//                    去登录页
                    return false;
                }else{
                    Iterator<String> iter = userLoginMap.keySet().iterator();
                    while(iter.hasNext()) {
                        String key = iter.next();
                        if(key!=null) {
                            if (key == token || key.equals(token)) {
                                iter.remove();  // OK
                            }
                        }
                    }
                    userLoginMap.put(token,System.currentTimeMillis());
                    return true;
                }
            }
        }
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
    @Scheduled(cron = "0 0 0 * * ? ")
    private void configureTasks() {
        userLoginMap.clear();
    }


}
