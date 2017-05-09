package com.x.okr.assemble.control.jaxrs.okrconfigsystem.exception;

import com.x.base.core.exception.PromptException;

public class SystemConfigQueryByCodeException extends PromptException {

	private static final long serialVersionUID = 1859164370743532895L;

	public SystemConfigQueryByCodeException( Throwable e, String code ) {
		super("根据指定的Code查询系统配置时发生异常。Code:" + code, e );
	}
}
