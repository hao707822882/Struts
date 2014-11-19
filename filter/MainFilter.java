package com.dangdang.struts3.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.log4j.Logger;

import com.dangdang.struts3.action.BaseAction;
import com.dangdang.struts3.core.impl.SimpleActionContext;

/**
 * 
 * @return action不在过滤链中，所以需要重定向，要不然系统会发出404错误
 * 
 * ***/
public class MainFilter implements Filter {

	Logger log = Logger.getLogger(getClass());
	SimpleActionContext context;

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		context = new SimpleActionContext();
		context.initAction(getClass().getClassLoader().getResource(""));
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {
		RequestFacade req = (RequestFacade) paramServletRequest;
		ResponseFacade resp = (ResponseFacade) paramServletResponse;
		String path = null;
		String topath = null;
		path = req.getRequestURI();
		// System.out.println("请求的地址是" + path);
		System.out.println("回话ID" + req.getSession().getId());
		Class actionclass = context.getAction(path);
		if (actionclass != null) {// 请求的是个action 则进入执行 要不然直接执行默认的
			BaseAction bs = context.setValue(actionclass, req, resp);
			// System.out.println("找到了一个需要使用action的   URL是" + path);
			topath = bs.exec();
			if (topath != null) {// 可能是验证码生成什么的
				// System.out.println("我要跳转到" + topath + "中了");
				if (topath.contains(".action"))// 如果是要跳转到action，则需要重定向，
					resp.sendRedirect(topath);
				else
					// 否则重新发送request请求，为什么需要这样 action不在过滤链中
					req.getRequestDispatcher(topath).forward(
							paramServletRequest, paramServletResponse);

			}
		} else {
			paramFilterChain
					.doFilter(paramServletRequest, paramServletResponse);
		}
	}

	@Override
	public void destroy() {

	}

}
