package com.dangdang.struts3.core.abs;

import java.net.URL;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.dangdang.struts3.action.BaseAction;

public interface AbsStruts3Context {

	abstract Class getAction(String path);

	abstract BaseAction setValue(Class bs, ServletRequest req,
			ServletResponse resp);

	void initAction(URL rootPath);

}
