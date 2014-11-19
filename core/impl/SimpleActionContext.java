package com.dangdang.struts3.core.impl;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dangdang.springbate.SpringContext;
import com.dangdang.springbate.annotation.Value;
import com.dangdang.springbate.util.reflectUtil.ReflectUtil;
import com.dangdang.struts3.action.BaseAction;
import com.dangdang.struts3.core.abs.AbsStruts3Context;
import com.dangdang.struts3.core.exception.NotFoundActionException;

public class SimpleActionContext implements AbsStruts3Context {
	Logger log = Logger.getLogger(getClass());
	Map actionClassFacory;
	Map beanPool;

	@Override
	public void initAction(URL rootPath) {
		log.info("加载的地址是" + rootPath);
		SpringContext context = new SpringContext(rootPath);
		actionClassFacory = context.getClassPool();
		beanPool = context.getPool();
	}

	@Override
	public Class getAction(String path) {
		Class clazz = (Class) actionClassFacory.get(path);
		return clazz;

	}

	@Override
	public BaseAction setValue(Class bs, ServletRequest req,
			ServletResponse resp) {
		Object obj = null;
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			obj = bs.newInstance();
			System.out.println("创建的实例是" + obj.getClass().getName());
			Field[] f = bs.getFields();// 获取class中的所有属性
			System.out.println(f.length);
			for (Field field : f) {
				System.out.println("或获取的属性是" + field.getName());
				Annotation a = field.getAnnotation(Value.class);// 获取属性上的注解
				if (a != null) {// 有註解
					String name = ((Value) a).value();// 获得注解的名字
					System.out.println("需要注入" + name + "属性");
					if (name.equals("request"))
						ReflectUtil.invokeSet(field, bs, obj, req);
					else if (name.equals("response"))
						ReflectUtil.invokeSet(field, bs, obj, resp);
					else if (name.equals("beanPool"))
						ReflectUtil.invokeSet(field, bs, obj, beanPool);
					else
						ReflectUtil.invokeSet(field, bs, obj,
								req.getParameter(name));
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println("注入的field是"+e.getCause());
			e.printStackTrace();
		}
		return (BaseAction) obj;
	}
}
