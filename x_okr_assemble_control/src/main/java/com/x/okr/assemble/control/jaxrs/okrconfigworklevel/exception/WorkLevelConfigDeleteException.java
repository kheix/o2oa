package com.x.okr.assemble.control.jaxrs.okrconfigworklevel.exception;

import com.x.base.core.exception.PromptException;

public class WorkLevelConfigDeleteException extends PromptException {

	private static final long serialVersionUID = 1859164370743532895L;

	public WorkLevelConfigDeleteException( Throwable e, String id ) {
		super("系统根据ID查询指定的工作级别配置时发生异常。ID:" + id, e);
	}
}
