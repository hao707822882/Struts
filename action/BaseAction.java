package com.dangdang.struts3.action;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;

import com.dangdang.springbate.annotation.Value;

public abstract class BaseAction {

	@Value(value = "request")
	public RequestFacade req;

	@Value(value = "response")
	public ResponseFacade resp;

	@Value(value = "beanPool")
	public Map beanPool;

	public RequestFacade getReq() {
		return req;
	}

	public void setReq(RequestFacade req) {

		System.out.println("---------------------װ������RequestFacade");
		System.out.println(req);
		this.req = req;
	}

	public ResponseFacade getResp() {
		return resp;
	}

	public void setResp(ResponseFacade resp) {
		System.out.println("---------------------װ������response");
		this.resp = resp;
	}

	public Map getBeanPool() {
		return beanPool;
	}

	public void setBeanPool(Map beanPool) {

		System.out.println("---------------------װ������BeanPool");
		this.beanPool = beanPool;
	}

	public abstract String exec();

}
