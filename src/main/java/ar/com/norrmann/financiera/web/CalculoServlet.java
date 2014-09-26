package ar.com.norrmann.financiera.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.norrmann.financiera.model.CalculadorMontos;
import ar.com.norrmann.financiera.model.Credito;


public class CalculoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String metodo = request.getParameter("metodo");
		String montoTotal = request.getParameter("montoTotal");
		String montoCuota = request.getParameter("montoCuota");
		String cantidadCuotas = request.getParameter("cantidadCuotas");
		String tasa = request.getParameter("tasa");

		if (metodo==null)return;
		String respuesta = null;
//		CreditoBean creditoBean = (CreditoBean)request.getSession().getAttribute("creditoBean");
//		Credito credito = creditoBean.getCredito();

		try {
			
			if (metodo.equals("calcularMontoTotal")){
				respuesta = CalculadorMontos.calcularMontoTotal(montoCuota, cantidadCuotas, tasa);
//				credito.setMontoSolicitado(getBigDecimal(respuesta));
			}
			if (metodo.equals("calcularTasa")){
				respuesta = CalculadorMontos.calcularTasa(montoTotal, montoCuota, cantidadCuotas);
//				credito.setTasa(getBigDecimal(respuesta));
			}
			if (metodo.equals("calcularCantidadCuotas")){
				respuesta = CalculadorMontos.calcularCantidadCuotas(montoTotal, montoCuota, tasa);
//				credito.setCantidadCuotas(getBigDecimal(respuesta).intValue());
			}
			if (metodo.equals("calcularMontoCuota")){
				respuesta = CalculadorMontos.calcularMontoCuota(montoTotal, cantidadCuotas, tasa);
//				credito.setMontoDeCuota(getBigDecimal(respuesta));
			}
		} catch (Exception e){
			respuesta = "0";
		}

		PrintWriter out = response.getWriter();
		if (respuesta==null){
			respuesta = "0";
		}
	    out.print(respuesta);
	    out.flush();
	}
	
}
