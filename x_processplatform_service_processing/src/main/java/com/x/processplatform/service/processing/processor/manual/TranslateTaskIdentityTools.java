package com.x.processplatform.service.processing.processor.manual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.x.base.core.gson.XGsonBuilder;
import com.x.base.core.utils.ListTools;
import com.x.base.core.utils.MapTools;
import com.x.organization.core.express.wrap.WrapCompanyDuty;
import com.x.organization.core.express.wrap.WrapDepartmentDuty;
import com.x.organization.core.express.wrap.WrapIdentity;
import com.x.processplatform.core.entity.content.Data;
import com.x.processplatform.core.entity.content.Work;
import com.x.processplatform.core.entity.element.Manual;
import com.x.processplatform.service.processing.Business;
import com.x.processplatform.service.processing.ProcessingAttributes;
import com.x.processplatform.service.processing.ScriptHelper;
import com.x.processplatform.service.processing.ScriptHelperFactory;

/**
 * 在Manual环节计算所有的待办人的Identity
 * 
 * @author Rui
 *
 */
class TranslateTaskIdentityTools {

	private static Logger logger = LoggerFactory.getLogger(TranslateTaskIdentityTools.class);

	/* 计算manual节点中所有的待办，全部翻译成Identity */
	static List<String> translate(Business business, ProcessingAttributes attributes, Work work, Data data,
			Manual manual) throws Exception {
		List<String> identities = SetUniqueList.setUniqueList(new ArrayList<String>());
		/* 指定的身份 */
		identities.addAll(identity(manual));
		/* 指定data数据路径值 */
		identities.addAll(data(business, data, manual));
		/* 指定处理部门 */
		identities.addAll(department(business, manual));
		/* 使用脚本计算 */
		identities.addAll(script(business, attributes, work, data, manual));
		/* 选择了Task职务 */
		identities.addAll(duty(business, attributes, work, data, manual));
		identities = ListTools.trim(identities, true, true);
		logger.debug("work title:{}, id:{}, manual name:{}, id:{}, translate people:{}", work.getTitle(), work.getId(),
				manual.getName(), manual.getId(), XGsonBuilder.toJson(identities));
		return identities;
	}

	/* 取到指定职务的identity */
	private static List<String> duty(Business business, ProcessingAttributes attributes, Work work, Data data,
			Manual manual) throws Exception {
		List<String> list = new ArrayList<>();
		if (StringUtils.isNotEmpty(manual.getTaskDuty())) {
			JsonArray array = XGsonBuilder.instance().fromJson(manual.getTaskDuty(), JsonArray.class);
			Iterator<JsonElement> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonObject o = iterator.next().getAsJsonObject();
				String name = o.get("name").getAsString();
				ScriptHelper scriptHelper = ScriptHelperFactory.create(business, attributes, work, data, manual);
				String str = scriptHelper.evalAsString(work.getApplication(), null, o.get("code").getAsString());
				if (StringUtils.isNotEmpty(str)) {
					/* 先尝试去取公司职务 */
					WrapCompanyDuty wrapCompanyDuty = business.organization().companyDuty().getWithNameWithCompany(name,
							str);
					if (null != wrapCompanyDuty) {
						list.addAll(wrapCompanyDuty.getIdentityList());
					} else {
						/* 再尝试取部门职务 */
						WrapDepartmentDuty wrapDepartmentDuty = business.organization().departmentDuty()
								.getWithNameWithDepartment(name, str);
						if (null != wrapDepartmentDuty) {
							list.addAll(wrapDepartmentDuty.getIdentityList());
						}
					}
				}
			}
		}
		return list;
	}

	/* 取到script指定的identity */
	private static List<String> script(Business business, ProcessingAttributes attributes, Work work, Data data,
			Manual manual) throws Exception {
		final List<String> list = new ArrayList<>();
		if ((StringUtils.isNotEmpty(manual.getTaskScript())) || (StringUtils.isNotEmpty(manual.getTaskScriptText()))) {
			ScriptHelper scriptHelper = ScriptHelperFactory.create(business, attributes, work, data, manual);
			List<String> os = scriptHelper.evalAsStringList(work.getApplication(), manual.getTaskScript(),
					manual.getTaskScriptText());
			for (String str : os) {
				if (StringUtils.isNotEmpty(str)) {
					list.add(str);
				}
			}
		}
		return list;
	}

	/* 取得指定部门的identity */
	private static List<String> department(Business business, Manual manual) {
		final List<String> list = new ArrayList<>();
		ListTools.trim(manual.getTaskDepartmentList(), true, true).forEach(o -> {
			try {
				if (StringUtils.isNotEmpty(o)) {
					for (WrapIdentity w : business.organization().identity().listWithDepartmentSubDirect(o)) {
						list.add(w.getName());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	/* 取得通过路径指定的identity */
	private static List<String> data(Business business, Data data, Manual manual) {
		final List<String> list = new ArrayList<>();
		ListTools.trim(manual.getTaskDataPathList(), true, true).forEach(o -> {
			try {
				if (StringUtils.isNotEmpty(o)) {
					list.addAll(MapTools.extractStringList(data, o, true, true));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	private static List<String> identity(Manual manual) throws Exception {
		final List<String> list = SetUniqueList.setUniqueList(new ArrayList<String>());
		/* 指定的身份 */
		ListTools.trim(manual.getTaskIdentityList(), true, true).forEach(o -> {
			if (StringUtils.isNotEmpty(o)) {
				list.add(o);
			}
		});
		return list;
	}

}
