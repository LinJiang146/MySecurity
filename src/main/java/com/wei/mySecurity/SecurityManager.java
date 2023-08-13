package com.wei.mySecurity;


import com.wei.mySecurity.Annotation.AuthenticatedByPermission;
import com.wei.mySecurity.Annotation.AuthenticatedByRole;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SecurityManager {



    private SecureDataSource dataSource;

    public void setDataSource(SecureDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private HashMap<String,String> tokens;

    //登录时进行认证身份
    public boolean AuthenticatedIdentity(){
        return true;
    }




    //执行方法时进行权限认证
    public boolean AuthenticatedInClass(Class clazz){

        boolean res = true;
        if (clazz.isAnnotationPresent(AuthenticatedByRole.class)){
            res= res && AuthenticatedRole((AuthenticatedByRole) clazz.getDeclaredAnnotation(AuthenticatedByRole.class));
        }
        if (clazz.isAnnotationPresent(AuthenticatedByPermission.class)){
            res= res && AuthenticatedPermission((AuthenticatedByPermission) clazz.getDeclaredAnnotation(AuthenticatedByPermission.class));
        }

        return res;
    }

    public boolean AuthenticatedInMethod(Method method){
        boolean res = true;
        if (method.isAnnotationPresent(AuthenticatedByRole.class)){
            res= res && AuthenticatedRole(method.getDeclaredAnnotation(AuthenticatedByRole.class));
        }
        if (method.isAnnotationPresent(AuthenticatedByPermission.class)){
            res= res && AuthenticatedPermission(method.getDeclaredAnnotation(AuthenticatedByPermission.class));
        }
        return res;
    }

    public boolean AuthenticatedRole(AuthenticatedByRole authenticatedByRole){
        //todo 优先处理value，value为空则处理operationValue


        if (authenticatedByRole.value()!=null&&authenticatedByRole.value().length>0){

            String[] needRole = authenticatedByRole.value();
            //获取当前登录用户的权限列表
            List<String> roleDate = dataSource.getRoleDate();
            //防止空指针异常
            if (roleDate==null) roleDate = new ArrayList<>();

            //判断是否拥有相应角色权限
            boolean res = true;
            for (String s : needRole) {
                res = res && roleDate.contains(s);
            }
            return res;
        }
        return false;


    }
    public boolean AuthenticatedPermission(AuthenticatedByPermission authenticatedByPermission){
        //todo 优先处理value，value为空则处理operationValue
        if (authenticatedByPermission.value()!=null&&authenticatedByPermission.value().length>0){

            String[] needRole = authenticatedByPermission.value();
            //获取当前登录用户的权限列表
            List<String> permissionDate = dataSource.getPermissionDate();
            permissionDate.addAll(dataSource.getPermissionByRole());
            //防止空指针异常
            if (permissionDate==null) permissionDate = new ArrayList<>();
            //判断是否拥有相应角色权限
            boolean res = true;
            for (String s : needRole) {
                res = res && permissionDate.contains(s);
            }
            return res;
        }
        return false;
    }


    public boolean login(){
        return true;
    }

    public boolean logout(){
        return true;
    }


}
