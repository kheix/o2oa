package com.x.bbs.assemble.control.jaxrs.foruminfo.exception;

import com.x.base.core.exception.PromptException;

public class ForumTypeCategoryEmptyException extends PromptException {

	private static final long serialVersionUID = 1859164370743532895L;

	public ForumTypeCategoryEmptyException() {
		super("论坛分区主题分类typeCategory为空， 无法进行查询或者保存." );
	}
}
