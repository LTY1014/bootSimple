package com.lty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.lty.annotation.AuthCheck;
import com.lty.common.BaseResponse;
import com.lty.common.DeleteRequest;
import com.lty.common.ErrorCode;
import com.lty.common.ResultUtils;
import com.lty.constant.UserConstant;
import com.lty.exception.BusinessException;
import com.lty.model.dto.user.UserLoginRequest;
import com.lty.model.dto.user.UserQueryRequest;
import com.lty.model.dto.user.UserRegisterRequest;
import com.lty.model.dto.user.UserUpdateRequest;
import com.lty.model.entity.User;
import com.lty.model.vo.UserVO;
import com.lty.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lty
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    public UserService userService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userLoginRequest, request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    @ApiOperation(value = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "获取当前用户")
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    @ApiOperation(value = "用户修改")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateStudent(@RequestBody UserUpdateRequest userUpdateRequest,HttpServletRequest request) {
        // 个人或管理员才可修改
        if (!(userService.getLoginUser(request).getId().equals(userUpdateRequest.getId())||userService.getLoginUser(request).getUserRole().equals(UserConstant.ADMIN_ROLE))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        boolean result = userService.updateUser(userUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "id获取")
    @GetMapping("/get")
    public BaseResponse<UserVO> getById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "id不存在");
        }
        UserVO userVo = new UserVO();
        BeanUtils.copyProperties(user, userVo);
        return ResultUtils.success(userVo);
    }

    @AuthCheck(mustRole = "admin")
    @ApiOperation(value = "列表获取")
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest) {

        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
        }
        QueryWrapper<User> qw = new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(qw);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    @ApiOperation(value = "分页获取")
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        User userQuery=new User();
        BeanUtils.copyProperties(userQueryRequest,userQuery);
        //分页基本字段
        long current=userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        // todo 添加字段支持搜索
        String userAccount=userQuery.getUserAccount();
        String userName=userQuery.getUserName();
        // 反爬虫
        if(size>50){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"查询数量过多");
        }
        QueryWrapper<User> qw=new QueryWrapper<>();
        // todo 添加查询条件
        qw.like(StringUtils.isNoneBlank(userAccount),"userAccount",userAccount);
        qw.like(StringUtils.isNoneBlank(userName),"userName",userName);

        qw.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals("asc"), sortField);
        Page<User> userPage = userService.page(new Page<>(current,size),qw);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    @ApiOperation(value = "用户删除")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        //管理员可删除
        User loginUser = userService.getLoginUser(request);
        if (!UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //endregion
        long id =deleteRequest.getId();
        User oldUser=userService.getById(id);
        if (oldUser==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }
}