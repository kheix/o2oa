package com.x.processplatform.assemble.surface.jaxrs.attachment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.http.ActionResult;
import com.x.base.core.http.EffectivePerson;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.processplatform.assemble.surface.Business;
import com.x.processplatform.assemble.surface.Control;
import com.x.processplatform.core.entity.content.Attachment;
import com.x.processplatform.core.entity.content.WorkCompleted;

class ActionListWithWorkCompleted extends ActionBase {
	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String workCompletedId) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			WorkCompleted workCompleted = emc.find(workCompletedId, WorkCompleted.class);
			if (null == workCompleted) {
				throw new WorkCompletedNotExistedException(workCompletedId);
			}
			Control control = business.getControlOfWorkCompleted(effectivePerson, workCompleted);
			if (BooleanUtils.isNotTrue(control.getAllowVisit())) {
				throw new WorkCompletedAccessDeniedException(effectivePerson.getName(), workCompleted.getTitle(),
						workCompleted.getId());
			}
			List<Attachment> os = emc.list(Attachment.class, workCompleted.getAttachmentList());
			List<Wo> wos = Wo.copier.copy(os);
			wos = wos.stream().sorted(Comparator.comparing(Wo::getCreateTime)).collect(Collectors.toList());
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Attachment {

		private static final long serialVersionUID = 1954637399762611493L;

		static WrapCopier<Attachment, Wo> copier = WrapCopierFactory.wo(Attachment.class, Wo.class, null, Wo.Excludes);

		public static List<String> Excludes = new ArrayList<>(JpaObject.FieldsInvisible);

		private Long rank;

		public Long getRank() {
			return rank;
		}

		public void setRank(Long rank) {
			this.rank = rank;
		}

	}
}