package ar.com.norrmann.financiera.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Report {
	private static final String PATH_TO_REPORTS = "ar/com/norrmann/financiera/report/";
	
	private static InputStream getReportInputStream(String reportName) {
		return Report.class.getClassLoader().getResourceAsStream(
				PATH_TO_REPORTS + reportName + ".jasper");
	}
	
	public static byte[] getReport(String reportName,List reportRows,Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;

		parameters.put("PATH_TO_REPORTS", PATH_TO_REPORTS);

		try {
			jasperPrint = JasperFillManager.fillReport(
					getReportInputStream(reportName), parameters,
					new JRBeanCollectionDataSource(reportRows));
		} catch (JRException ex) {
			ex.printStackTrace();
		}
		byte[] pdfBytes = null;
		try {
			pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return pdfBytes;
	}

	public static JasperPrint getJasperPrint(String reportName,List reportRows,Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;

		parameters.put("PATH_TO_REPORTS", PATH_TO_REPORTS);

		try {
			jasperPrint = JasperFillManager.fillReport(
					getReportInputStream(reportName), parameters,
					new JRBeanCollectionDataSource(reportRows));
		} catch (JRException ex) {
			ex.printStackTrace();
		}
		return jasperPrint;
	}

}
