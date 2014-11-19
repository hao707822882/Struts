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
 * @return action���ڹ������У�������Ҫ�ض���Ҫ��Ȼϵͳ�ᷢ��404����
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
		// System.out.println("����ĵ�ַ��" + path);
		System.out.println("�ػ�ID" + req.getSession().getId());
		Class actionclass = context.getAction(path);
		if (actionclass != null) {// ������Ǹ�action �����ִ�� Ҫ��Ȼֱ��ִ��Ĭ�ϵ�
			BaseAction bs = context.setValue(actionclass, req, resp);
			// System.out.println("�ҵ���һ����Ҫʹ��action��   URL��" + path);
			topath = bs.exec();
			if (topath != null) {// ��������֤������ʲô��
				// System.out.println("��Ҫ��ת��" + topath + "����");
				if (topath.contains(".action"))// �����Ҫ��ת��action������Ҫ�ض���
					resp.sendRedirect(topath);
				else
					// �������·���request����Ϊʲô��Ҫ���� action���ڹ�������
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
