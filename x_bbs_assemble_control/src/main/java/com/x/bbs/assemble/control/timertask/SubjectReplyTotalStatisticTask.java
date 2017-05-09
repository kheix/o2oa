package com.x.bbs.assemble.control.timertask;

import java.util.List;

import com.x.base.core.logger.Logger;
import com.x.base.core.logger.LoggerFactory;
import com.x.base.core.project.Context;
import com.x.base.core.project.clock.ClockScheduleTask;
import com.x.bbs.assemble.control.service.BBSForumSubjectStatisticService;
import com.x.bbs.assemble.control.service.BBSSectionInfoServiceAdv;
import com.x.bbs.assemble.control.service.BBSSubjectInfoService;
import com.x.bbs.entity.BBSSectionInfo;
import com.x.bbs.entity.BBSSubjectInfo;

/**
 * 定时代理，对所有的论坛以及版块进行主题和回贴数、参与人数的统计。 
 * 1、遍历所有论坛 
 * 2、对每一个论坛，每一个主版块以及子版块进行数据统计
 * 
 * @author LIYI
 *
 */
public class SubjectReplyTotalStatisticTask extends ClockScheduleTask {

	private Logger logger = LoggerFactory.getLogger(SubjectReplyTotalStatisticTask.class);
	private BBSSubjectInfoService subjectInfoService = new BBSSubjectInfoService();
	private BBSSectionInfoServiceAdv sectionInfoServiceAdv = new BBSSectionInfoServiceAdv();
	private BBSForumSubjectStatisticService forumSubjectStatisticService = new BBSForumSubjectStatisticService();

	public SubjectReplyTotalStatisticTask(Context context) {
		super( context );
	}
	
	public void execute() {
		List<BBSSectionInfo> sectionList = null;
		try {
			sectionList = sectionInfoServiceAdv.listAll();
			if (sectionList != null && !sectionList.isEmpty()) {
				sectionList.parallelStream().forEach( s -> {
					try {
						statisticExecute( s );
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			logger.info("Timertask[SubjectTotalStatisticTask] completed and excute success.");
		} catch (Exception e) {
			logger.warn("SubjectReplyTotalStatisticTask got an exception.");
			logger.error(e);
		}
	}

	private void statisticExecute( BBSSectionInfo section ) throws Exception {
		List<BBSSubjectInfo> subjectList = subjectInfoService.listSubjectIdsBySection( section.getId() );
		if (subjectList != null && !subjectList.isEmpty()) {
			forumSubjectStatisticService.statisticReplyTotalForSubjects( subjectList );
		}
	}
}