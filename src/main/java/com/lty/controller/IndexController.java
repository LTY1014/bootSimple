package com.lty.controller;

import com.lty.common.BaseResponse;
import com.lty.common.ErrorCode;
import com.lty.common.ResultUtils;
import com.lty.exception.BusinessException;
import com.lty.service.UserService;
import com.lty.utils.IpInfoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lty
 */
@Slf4j
@RestController
public class IndexController {
    @Value("${server.port}")
    Integer port;

    @Resource
    private UserService userService;


    @ApiOperation(value = "默认")
    @GetMapping(value = "/")
    public BaseResponse<String> index() {
        log.info("index() called with parameters => ");
        return ResultUtils.success("success");
    }

    @ApiOperation(value = "失败",response = ResultUtils.class)
    @GetMapping( "/fail")
    public BaseResponse error() {
        log.info("error() called with parameters => ");
        return ResultUtils.error(ErrorCode.PARAMS_ERROR,"参数失败");
    }

    @ApiOperation(value = "你好")
    @GetMapping( "/hello")
    public String hello(@RequestParam(value = "name",required = false) String name) {
        String str="hello"+name;
        log.info("hello() called with parameters => [name = {}]", name);
        return str;
    }

    @ApiOperation(value = "资源页")
    @GetMapping("/resource")
    public String resource(HttpServletRequest request) {
        if(userService.getLoginUser(request).getId()<0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return "<br />"
                + "<h1 style=text-align:center;>资源页 (登录后才可进入页面)"
                + "<hr />"
                + "<p style=text-align:center;>resource page</p>";
    }

    @ApiOperation(value = "管理员资源页")
    @GetMapping("/adminResource")
    public String adminResource(HttpServletRequest request) {
        if(!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return "<br />"
                + "<h1 style=text-align:center;>管理员资源页 (管理员登录后才可进入页面)"
                + "<hr />"
                + "<p style=text-align:center;>admin-resource page</p>";
    }

    @ApiOperation(value = "获取端口号")
    @GetMapping( "/getPort")
    public String getPort(){
        String str="当前端口为: "+port;
        log.info(str);
        return str;
    }

    @ApiOperation(value = "获取IP")
    @GetMapping("/getIp")
    public String getIp(HttpServletRequest request){
        return IpInfoUtil.getIpAddress(request);
    }
}
