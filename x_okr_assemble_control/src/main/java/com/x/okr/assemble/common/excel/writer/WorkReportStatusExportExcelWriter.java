package com.x.okr.assemble.common.excel.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.x.okr.assemble.common.date.DateOperation;
import com.x.okr.assemble.control.jaxrs.statistic.WrapOutOkrStatisticReportStatusEntity;
import com.x.okr.assemble.control.jaxrs.statistic.WrapOutOkrStatisticReportStatusHeader;
import com.x.okr.assemble.control.jaxrs.statistic.WrapOutOkrStatisticReportStatusTable;

public class WorkReportStatusExportExcelWriter {
	
	private Integer currentRowNumber = 0;
	
	/**
	 * 写入excel并填充内容,一个sheet只能写65536行以下，超出会报异常，写入时建议大量数据时使用AbstractExcel2007Writer
	 * @param fileOut
	 * @throws IOException
	 */
	public String writeExcel( WrapOutOkrStatisticReportStatusTable wrapOutOkrStatisticReportStatusTable ) {		
		String organizationName = null;
		String centerTitle = null;
		Integer centerCount = 0;
		Integer workCount = 0;
		List<WrapOutOkrStatisticReportStatusHeader> headers = null;
		List<WrapOutOkrStatisticReportStatusEntity> contents = null;
		Workbook wb = new HSSFWorkbook();// 创建excel2003对象
		Sheet sheet = wb.createSheet( "工作汇报情况统计表" );// 创建新的工作表
		Row row = null;
		Cell cell = null;
		
		if( wrapOutOkrStatisticReportStatusTable != null ){

			headers = wrapOutOkrStatisticReportStatusTable.getHeader();
			contents = wrapOutOkrStatisticReportStatusTable.getContent();
			
			//表头//////////////////////////////////////////////////////////////
			row = sheet.createRow( currentRowNumber );
//			row.createCell(0).setCellValue( "组织名称" );
//			row.createCell(1).setCellValue( "中心工作" );
//			row.createCell(2).setCellValue( "工作内容" );
			if( headers != null  && !headers.isEmpty() ){
				for( int i = 0; i<headers.size(); i++ ){
					row.createCell( i ).setCellValue( headers.get( i ).getTitle() );//具体工作内容
				}
			}
			currentRowNumber++;//表头占一行
			
			if( contents != null && !contents.isEmpty() ){
				for( WrapOutOkrStatisticReportStatusEntity organizationEntity : contents ){
					
					organizationName = organizationEntity.getTitle();
					centerCount = organizationEntity.getRowCount();

					//logger.info( "|------------"+ currentRowNumber + " --> " + ( currentRowNumber + centerCount -1 )+ ": 组织["+ organizationName +"]" );
					sheet.addMergedRegion( new CellRangeAddress( currentRowNumber, ( currentRowNumber + centerCount -1 ), 0, 0 ));
					
					for( WrapOutOkrStatisticReportStatusEntity centerEntity : organizationEntity.getArray() ){
						centerTitle = centerEntity.getTitle();
						workCount = centerEntity.getRowCount();
						
						//logger.info( "--|------" + currentRowNumber + " --> " + ( currentRowNumber + workCount -1 ) + ": 中心工作["+ centerTitle +"]");
						sheet.addMergedRegion( new CellRangeAddress( currentRowNumber, ( currentRowNumber + workCount -1 ), 1, 1 ));
						
						for( WrapOutOkrStatisticReportStatusEntity workEntity : centerEntity.getArray() ){				
							row = sheet.createRow( currentRowNumber );
							row.createCell(0).setCellValue( organizationName );
							row.createCell(1).setCellValue( centerTitle );
							
							//logger.info( "----|--"+ currentRowNumber +": 具体工作["+ workEntity.getTitle() +"]" );
							row.createCell(2).setCellValue( workEntity.getTitle() );
							if( workEntity.getFields() != null && !workEntity.getFields().isEmpty() ){
								for( int i = 0; i<workEntity.getFields().size(); i++ ){
									if( workEntity.getFields().get( i ).getReportStatus() == 0 ){
										row.createCell( i+3 ).setCellValue("无内容");
									}else if( workEntity.getFields().get( i ).getReportStatus() == 1 ){
										row.createCell( i+3 ).setCellValue("有内容");
									}else if( workEntity.getFields().get( i ).getReportStatus() == -1 ){
										row.createCell( i+3 ).setCellValue("'--");
									}else{
										row.createCell( i+3 ).setCellValue("'--");
									}
								}
							}
							currentRowNumber++;
						}
					}
				}
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////                                单元格样式设计                                                                                       //////////////////
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				sheet.setDefaultColumnWidth( 20 );  
				sheet.setDefaultRowHeightInPoints( 20 );
				
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.setWrapText(true);
				cellStyle.setAlignment( CellStyle.ALIGN_CENTER ); // 居中
				cellStyle.setVerticalAlignment( CellStyle.VERTICAL_CENTER ); // 居中
				
				if( headers != null  && !headers.isEmpty() ){
					for( int i = 0; i<headers.size(); i++ ){
						if( i == 0 ){
							sheet.setColumnWidth( 0, 10*256 ); 
						}else if( i == 1 ){
							sheet.setColumnWidth( 1, 50*256 );
						}else if( i == 2 ){
							sheet.setColumnWidth( 2, 50*256 ); 
						}else{
							sheet.setColumnWidth( i, 10*256 );
						}
					}
				}
				
				Iterator<Row> rowIterator = sheet.rowIterator();
				Iterator<Cell> cellIterator = null;
				
				while( rowIterator.hasNext() ){
					row = rowIterator.next();
					row.setHeight( (short) ( 40 * 20 ) );
					
					cellIterator = row.cellIterator();
					while( cellIterator.hasNext() ){
						cell = cellIterator.next();
						cell.setCellStyle( cellStyle );
					}
				}
			}
			try {
				String timeFlag = new DateOperation().getNowTimeChar();
				File dir = new File("download/temp");
				if( !dir.exists() ){
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream("download/temp/export_"+timeFlag+".xls");
				wb.write( fos );
				fos.close();
			    wb.close();
			    return timeFlag;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "none";
	}
}
