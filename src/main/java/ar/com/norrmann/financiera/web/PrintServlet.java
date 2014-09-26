package ar.com.norrmann.financiera.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import ar.com.norrmann.financiera.model.Cliente;
import ar.com.norrmann.financiera.model.Credito;
import ar.com.norrmann.financiera.model.Cuota;
import ar.com.norrmann.financiera.model.Pago;
import ar.com.norrmann.financiera.report.DateUtils;
import ar.com.norrmann.financiera.report.NumeroALetras;
import ar.com.norrmann.financiera.report.Report;

/**
 * Servlet implementation class PrintServlet
 */
@WebServlet("/PrintServlet")
public class PrintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PrintServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String reportName = request.getParameter("reportName");
		if (reportName==null)return;
		if (reportName.equals("credito")){
			printCreditoReport(request, response, reportName);
		} 
		if (reportName.equals("recibo")){
			PagoBean pagoBean = (PagoBean)request.getSession().getAttribute("pagoBean");
			Pago pago = pagoBean.getPago();
			printReciboReport(request, response, "recibo",pago);
		} 
		if (reportName.equals("reciboUltimaCuota")){
			CuotaBean cuotaBean = (CuotaBean)request.getSession().getAttribute("cuotaBean");
			Cuota cuota = cuotaBean.getCuota(); 
			Pago pago = cuota.getUltimoPago();
			printReciboReport(request, response, "recibo",pago);
		}
		if (reportName.equals("reciboUltimoPago")){
			CuotaBean cuotaBean = (CuotaBean)request.getSession().getAttribute("cuotaBean");
			Pago pago = cuotaBean.getUltimoPago();
			printReciboReport(request, response, "recibo",pago);
		}
		
		if (reportName.equals("invitacionAlPago")){
			printInvitacionAlPago(request, response, reportName);
		} 
		if (reportName.equals("intimacion")){
			printInvitacionAlPago(request, response, reportName);
		} 
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public void printCreditoReport(HttpServletRequest request,HttpServletResponse response,String reportName) {
		try {
			CreditoBean creditoBean = (CreditoBean)request.getSession().getAttribute("creditoBean");
			Credito credito = creditoBean.getCredito();
			Cliente cliente = credito.getCliente();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("cliente", credito.getCliente().getNombreLargo());
			parameters.put("direccion", credito.getCliente().getDomicilioComercial());
			parameters.put("monto", credito.getMontoSolicitado());
			credito.setVenceDiaFromCuotas();
			parameters.put("diasVencimiento", credito.getDiasVencimientoString());
			String apellido = cliente.getApellidos();
			String nombre = cliente.getNombres();
			String nombreArchivo = "credito_"+apellido+"_"+nombre+".pdf";
			nombreArchivo=nombreArchivo.replaceAll(" ", "_");


			byte[] bytes = Report.getReport(reportName, credito.getCuotas(),parameters);
			response.setContentLength(bytes.length);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="+ nombreArchivo);
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void printReciboReport(HttpServletRequest request,HttpServletResponse response,String reportName,Pago pago) {
		if (pago==null)return;
		try {
			NumeroALetras numeroALetras = new NumeroALetras(); 
			Cliente cliente = pago.getCuota().getCredito().getCliente();
			
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("cliente", cliente.getNombreLargo());
			parameters.put("cobrador", pago.getCobrador().getNombreCompleto());
			parameters.put("fecha", pago.getFechaPago());
			parameters.put("vencimientoCuota", DateUtils.getSimpleFormattedDate(pago.getCuota().getFechaVencimiento()));
			parameters.put("monto", pago.getImporte());
			parameters.put("numeroCuota", pago.getCuota().getNumero());
			parameters.put("numero", String.format("%05d", pago.getId()));
			parameters.put("firmante", cliente.getNombreLargo());
			parameters.put("domicilio", cliente.getDomicilio());
			parameters.put("tipoCredito", pago.getCuota().getCredito().getTipoCredito().getTipo());
			
			String importeString = pago.getImporte().toString();
			parameters.put("letras", numeroALetras.Convertir(importeString, true));
			String apellido = cliente.getApellidos();
			String nombre = cliente.getNombres();
			String nombreArchivo = "recibo_"+apellido+"_"+nombre+"_"+pago.getId()+".pdf";
			nombreArchivo=nombreArchivo.replaceAll(" ", "_");
			
			List<Pago>pagoList = new ArrayList<Pago>();
			pagoList.add(pago);

			JasperPrint jp1 = Report.getJasperPrint(reportName, pagoList,parameters);
			parameters.put("firmante", "");
			JasperPrint jp2 = Report.getJasperPrint(reportName, pagoList,parameters);
			
			//byte[] bytes = Report.getReport(reportName, pagoList,parameters);
			byte[] bytes = generateReport(jp1, jp2);
			response.setContentLength(bytes.length);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="+ nombreArchivo);
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] generateReport(JasperPrint jasperPrint1, JasperPrint
			jasperPrint2) {
			  List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			  jasperPrintList.add(jasperPrint1);
			  jasperPrintList.add(jasperPrint2);


			  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			  JRPdfExporter exporter = new JRPdfExporter();
			  exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			  exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS,
			Boolean.TRUE);
			  exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			  try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
			  return baos.toByteArray();
			}
	
	public void printInvitacionAlPago(HttpServletRequest request,HttpServletResponse response,String reportName) {
		try {
			ClienteBean clienteBean = (ClienteBean)request.getSession().getAttribute("clienteBean");

			Map<String,Object> parameters = new HashMap<String, Object>();
			List<Cliente> clienteList = new ArrayList<Cliente>();
			clienteList.add(clienteBean.getCliente());
			parameters.put("FECHA", DateUtils.getFormalLetterDate(new Date()));


			byte[] bytes = Report.getReport(reportName, clienteList,parameters);
			response.setContentLength(bytes.length);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="+reportName+".pdf" );
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
