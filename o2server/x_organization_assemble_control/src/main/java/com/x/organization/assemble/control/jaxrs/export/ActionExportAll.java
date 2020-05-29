package com.x.organization.assemble.control.jaxrs.export;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import com.x.base.core.project.tools.DateTools;
import com.x.base.core.project.tools.ListTools;
import com.x.organization.assemble.control.Business;
import com.x.organization.core.entity.Group;
import com.x.organization.core.entity.Identity;
import com.x.organization.core.entity.Person;
import com.x.organization.core.entity.PersonAttribute;
import com.x.organization.core.entity.PersonAttribute_;
import com.x.organization.core.entity.Unit;
import com.x.organization.core.entity.UnitDuty;
import com.x.organization.core.entity.UnitDuty_;
import com.x.organization.core.entity.Unit_;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoFile;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;

/**
 * 导入的文件没有用到文件存储器，是直接放在数据库中的BLOB列
 */
public class ActionExportAll extends BaseAction {
	
	private static  Logger logger = LoggerFactory.getLogger(ActionExportAll.class);
	List<Unit> allUnitList = new ArrayList<>();
	List<Person> allPersonList = new ArrayList<>();
	List<Identity> allIdentityList = new ArrayList<>();
	List<UnitDuty> allDutyList = new ArrayList<>();
	List<Group> allGroupList = new ArrayList<>();
	Workbook wb = new HSSFWorkbook();
	
	protected ActionResult<Wo> execute( HttpServletRequest request, EffectivePerson effectivePerson, Boolean stream ) throws Exception {
		ActionResult<Wo> result = new ActionResult<>();
		//Workbook wb = null;
		Wo wo = null;
		String fileName = null;
		Business business = null;
		
		
		// 先获取需要导出的数据
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			 business = new Business(emc);
			this.listUnit(business);
			this.listPerson(business);
			this.listIdentity(business);
		} catch (Exception e) {
			logger.info("系统在查询所有组织人员信息时发生异常。" );
			e.printStackTrace();
		}
		
		fileName = "person_export_" + DateTools.formatDate(new Date()) + ".xls";
		//创建说明sheet
		this.createNoticeSheet();
		
		// 将组织信息结果组织成EXCEL		
		if( ListTools.isNotEmpty(allUnitList) ) {
			this.composeUnit( business, "组织信息", allUnitList );
		}
		
		// 将人员基础信息结果组织成EXCEL		
		if( ListTools.isNotEmpty(allPersonList) ) {
			this.composePerson( business, "人员基本信息", allPersonList );
		}

		// 将人员身份信息结果组织成EXCEL		
		if( ListTools.isNotEmpty(allPersonList) ) {
			this.composePerson( business, "人员身份信息", allPersonList );
		}
		
		if( wb != null ) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
			    wb.write(bos);
			    wo = new Wo(bos.toByteArray(), 
						this.contentType(stream, fileName), 
						this.contentDisposition(stream, fileName));
			} finally {
			    bos.close();
			}
		}		
		result.setData(wo);
		return result;
	}
	
	private void listUnit(Business business) throws Exception {
		List<Unit> topUnitList = new ArrayList<>();
		topUnitList = this.listTopUnit(business);
		if(ListTools.isNotEmpty(topUnitList)){
			allUnitList.addAll(topUnitList);
			for (Unit unitItem : topUnitList) {
				List<Unit> ulist= this.listSubNested(business, unitItem);
				if(ListTools.isNotEmpty(ulist)){
					allUnitList.addAll(ulist);
				}
			}
		}
		
		if(ListTools.isNotEmpty(allUnitList)){
			allUnitList = business.unit().sort(allUnitList);
		}
	}
	
	private void listPerson(Business business) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Person.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		Root<Person> root = cq.from(Person.class);
		allPersonList = em.createQuery(cq.select(root)).getResultList();
	}
	
	private void listIdentity(Business business) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Identity.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Identity> cq = cb.createQuery(Identity.class);
		Root<Identity> root = cq.from(Identity.class);
		allIdentityList = em.createQuery(cq.select(root)).getResultList();
	}

	private List<Unit> listTopUnit(Business business) throws Exception {
		EntityManager em = business.entityManagerContainer().get(Unit.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Unit> cq = cb.createQuery(Unit.class);
		Root<Unit> root = cq.from(Unit.class);
		Predicate p = cb.equal(root.get(Unit_.level), 1);
		List<Unit> os = em.createQuery(cq.select(root).where(p)).getResultList();
		return os;
	}
	
	private List<Unit> listSubNested(Business business,Unit unit) throws Exception {
		List<Unit> os = business.unit().listSubNestedObject(unit);
		return os;
	}
	
	private void createNoticeSheet() throws Exception {
		
			Row row = null;
			// 创建新的表格
			Sheet sheet = wb.createSheet("注意事项");
			
			// 先创建表头
			row = sheet.createRow(2);
			row.createCell(0).setCellValue("注意事项:");

			row = sheet.createRow(4);
			row.createCell(0).setCellValue("1. 表格内不要做合并(拆分)单元格操作，各列顺序不能变动，更不能删除，否则会造成数据混乱；");
			
			row = sheet.createRow(6);
			row.createCell(0).setCellValue("2. * 为必填项");
			
			row = sheet.createRow(8);
			row.createCell(0).setCellValue("3. 如有特殊要求的格式，详见列名批注；");
			
			row = sheet.createRow(10);
			row.createCell(0).setCellValue("4. 表中示例数据用于示范，实际导入时需删除；");
			
			row = sheet.createRow(12);
			row.createCell(0).setCellValue("5. 每个Sheet页顺序不能变动，本Sheet页不能删除。");

	}
	
	private void composeUnit(Business business, String sheetName, List<Unit> unitList) throws Exception {
		Unit unit = null;
		EntityManagerContainer em = business.entityManagerContainer();
		
		Row row = null;
		if (ListTools.isNotEmpty(unitList) ) {
			// 创建新的表格
			Sheet sheet = wb.createSheet(sheetName);
			
			// 先创建表头
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("组织名称 *");
			row.createCell(1).setCellValue("组织编号 *");
			row.createCell(2).setCellValue("组织级别名称 *");
			row.createCell(3).setCellValue("上级组织编号");
			row.createCell(4).setCellValue("组织代字");
			row.createCell(5).setCellValue("描述");
			row.createCell(6).setCellValue("排序号");

			for (int i = 0; i < unitList.size(); i++) {
				unit = unitList.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(unit.getName());
				row.createCell(1).setCellValue(unit.getUnique());
				if(ListTools.isNotEmpty(unit.getTypeList())){
					row.createCell(2).setCellValue(unit.getTypeList().get(0));
				}else{
					row.createCell(2).setCellValue("");
				}
				
				String superior = unit.getSuperior();
				if(StringUtils.isEmpty(superior)){
					row.createCell(3).setCellValue("");
				}else{
					Unit u = null;
					u = em.flag(unit.getSuperior(), Unit.class);
					if(u != null){
						row.createCell(3).setCellValue(u.getUnique());
					}else{
						row.createCell(3).setCellValue(unit.getSuperior());
					}
					
				}
				row.createCell(4).setCellValue(unit.getShortName());
				row.createCell(5).setCellValue(unit.getDescription());
				row.createCell(6).setCellValue(unit.getOrderNumber()+"");
				
			}
		}
	}
	
	private void composePerson(Business business, String sheetName, List<Person> personList) throws Exception {
		Person person = null;
		
		Row row = null;
		if (ListTools.isNotEmpty(personList) ) {
			// 创建新的表格
			Sheet sheet = wb.createSheet(sheetName);
			
			// 先创建表头
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("人员行姓名 *");
			row.createCell(1).setCellValue("人员编号 *");
			row.createCell(2).setCellValue("登录账号 *");
			row.createCell(3).setCellValue("手机号码 *");
			row.createCell(4).setCellValue("身份证号 *");
			row.createCell(5).setCellValue("性别");
			row.createCell(6).setCellValue("邮件");

			for (int i = 0; i < personList.size(); i++) {
				person = personList.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(person.getName());
				row.createCell(1).setCellValue(person.getUnique());
				row.createCell(2).setCellValue(person.getEmployee());
				row.createCell(3).setCellValue(person.getMobile());
				
				if(StringUtils.isNotEmpty(person.getId())){
					List<PersonAttribute> personAttributeList = this.listAttributeWithPerson(business,person.getId());
					if(ListTools.isNotEmpty(personAttributeList)){
						for(PersonAttribute o : personAttributeList){
							if("idNumber".equals(o.getName())){
								row.createCell(4).setCellValue(o.getAttributeList().get(0));
							}
						}
					}
				}
				row.createCell(5).setCellValue(person.getGenderType().toString());
				row.createCell(6).setCellValue(person.getMail());
				
			}
		}
	}
	
	private void composeIdentity(Business business, String sheetName, List<Identity> identityList) throws Exception {
		Identity identity = null;
		EntityManagerContainer emc = business.entityManagerContainer();
		Row row = null;
		if (ListTools.isNotEmpty(identityList) ) {
			// 创建新的表格
			Sheet sheet = wb.createSheet(sheetName);
			
			// 先创建表头
			row = sheet.createRow(0);
			row.createCell(0).setCellValue("人员编号 *");
			row.createCell(1).setCellValue("组织编号 *");
			row.createCell(2).setCellValue("登录账号 *");
			row.createCell(3).setCellValue("职务编号");
			row.createCell(4).setCellValue("主兼职");

			for (int i = 0; i < identityList.size(); i++) {
				identity = identityList.get(i);
				row = sheet.createRow(i + 1);
				Person person = emc.flag(identity.getPerson(), Person.class);
				Unit unit = emc.flag(identity.getUnit(), Unit.class);
				if(person != null){
					row.createCell(0).setCellValue(person.getUnique());
					if(unit != null){
						row.createCell(1).setCellValue(unit.getUnique());
						List<UnitDuty> unitDutyList =  this.listDutyWithIdentity(business,identity.getId());
						if(ListTools.isNotEmpty(unitDutyList)){
							row.createCell(2).setCellValue("");
						}
						row.createCell(3).setCellValue("");
					}
				}	
				
			}
		}
	}
	
	private List<PersonAttribute> listAttributeWithPerson(Business business,String personId) throws Exception{
		EntityManager em = business.entityManagerContainer().get(PersonAttribute.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PersonAttribute> cq = cb.createQuery(PersonAttribute.class);
		Root<PersonAttribute> root = cq.from(PersonAttribute.class);
		Predicate p = cb.equal(root.get(PersonAttribute_.person), personId);
		List<PersonAttribute> os = em.createQuery(cq.select(root).where(p)).getResultList();
		return os;
	}
	
	private List<UnitDuty> listDutyWithIdentity(Business business,String identityId) throws Exception{
		EntityManager em = business.entityManagerContainer().get(UnitDuty.class);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UnitDuty> cq = cb.createQuery(UnitDuty.class);
		Root<UnitDuty> root = cq.from(UnitDuty.class);
		Predicate p = cb.isMember(identityId, root.get(UnitDuty_.identityList));
		List<UnitDuty> os = em.createQuery(cq.select(root).where(p)).getResultList();
		return os;
	}

	public static class Wo extends WoFile {
		public Wo(byte[] bytes, String contentType, String contentDisposition) {
			super(bytes, contentType, contentDisposition);
		}
	}

}
