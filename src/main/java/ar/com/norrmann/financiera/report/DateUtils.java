
package ar.com.norrmann.financiera.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateUtils {
	private final static long HOURS = 24;
	private final static long MINUTES = 60;
	private final static long SECONDS = 60;
	private final static long MILLISECONDS = 1000;

	public DateUtils() {
	}
	
	public static Date currentDate() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			return df.parse(df.format(new Date()));
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Date currentDateTime() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
			return df.parse(df.format(new Date()));
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Recibe un string de fecha en formato dd/MM/yyyy y convierte en un objeto
	 * de la clase java.util.Date. 
	 * 
	 * @return java.util.Date
	 * @param dateFormat
	 *            java.lang.String
	 */
	public static Date getDate(String dateFormat) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			return df.parse(dateFormat);
		} catch (ParseException ex) {
			return null;
		}
	}

	public static Date getDate(String dateFormat,String format) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(format,Locale.getDefault());
			return df.parse(dateFormat);
		} catch (ParseException ex) {
			return null;
		}
	}
	
	/**
	 * Recibe un a�o, mes y dia en formato string y lo convierte en un objeto de
	 * la clase java.util.Date.
	 * 
	 * @return java.util.Date
	 * @param year
	 *            java.lang.String
	 * @param month
	 *            java.lang.String
	 * @param day
	 *            java.lang.String
	 */
	public static Date getDate(String year, String month, String day) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());
			return df.parse(year + "/" + month + "/" + day);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Recibe a�o, mes, dia, hora y minutos en formato string y lo convierte en un objeto de
	 * la clase java.util.Date.
	 * 
	 * @return java.util.Date
	 * @param year
	 *            java.lang.String
	 * @param month
	 *            java.lang.String
	 * @param day
	 *            java.lang.String
	 * @param hour
	 *            java.lang.String
	 * @param minutes
	 *            java.lang.String
	 */
	public static Date getDate(String year, 
								String month, 
								String day, 
								String hour,
								String minutes) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
			return df.parse(day + "/" + month + 
							"/" + year + " " + hour + 
							":" + minutes + ":" + "00");
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Recibe una fecha con formato dd/MM/yyyy, hora y minutos en formato string y lo convierte en un objeto de
	 * la clase java.util.Date.
	 * 
	 * @return java.util.Date
	 * @param fecha
	 *            java.lang.String
	 * @param hour
	 *            java.lang.String
	 * @param minutes
	 *            java.lang.String
	 */
	public static Date getDateTime(String fecha, 
								String hour,
								String minutes,
								String segundos) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
			return df.parse(fecha + " " + hour + ":" + minutes + ":" + segundos);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con
	 * la hora en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d java.util.Date
	 */
	public static String getHour(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("HH",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con
	 * los minutos en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d java.util.Date
	 */
	public static String getMinutes(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("mm",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con
	 * los segundos en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d java.util.Date
	 */
	public static String getSeconds(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("ss",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con el
	 * dia en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d java.util.Date
	 */
	public static String getDay(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con el
	 * mes en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getMonth(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("MM",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con el
	 * nombre del mes.
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getMonthName(Date d) {
		String monthValue = getMonth(d);
		if (monthValue.equals("01"))
			return "Enero";
		if (monthValue.equals("02"))
			return "Febrero";
		if (monthValue.equals("03"))
			return "Marzo";
		if (monthValue.equals("04"))
			return "Abril";
		if (monthValue.equals("05"))
			return "Mayo";
		if (monthValue.equals("06"))
			return "Junio";
		if (monthValue.equals("07"))
			return "Julio";
		if (monthValue.equals("08"))
			return "Agosto";
		if (monthValue.equals("09"))
			return "Septiembre";
		if (monthValue.equals("10"))
			return "Octubre";
		if (monthValue.equals("11"))
			return "Noviembre";
		if (monthValue.equals("12"))
			return "Diciembre";
		return "";
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con la
	 * fecha formateada: dd/MM/yyyy.
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getSimpleFormattedDate(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}
	
	public static String getFormalLetterDate(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy",new Locale("es","AR"));
			return df.format(d);
		} else {
			return "";
		}
	}
	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con a�o
	 * en valor numerico.
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getYear(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con la
	 * fecha formateada: dd/MM/yyyy HH:mm:ss. 
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getCompleteFormattedDateTime(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe una fecha en un objeto java.util.Date y devuelve un string con la
	 * hora formateada: HH:mm:ss.
	 * 
	 * @return java.lang.String
	 * @param d
	 *            java.util.Date
	 */
	public static String getSimpleFormattedHour(Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
			return df.format(d);
		} else {
			return "";
		}
	}

	/**
	 * Recibe fecha y hora en un string con formato dd/MM/yyyy HH:mm:ss y
	 * devuelve un objeto java.util.Date.
	 * 
	 * @return java.util.Date
	 * @param date
	 *            java.lang.String
	 */
	public static Date getCompleteDateTime(String date) {
		if (date != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss",Locale.getDefault());
				return df.parse(date);
			} catch (ParseException ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Recibe dos fechas en objetos java.util.Date: birthDate (fecha de
	 * nacimiento) y currentDate (fecha actual) <br>
	 * y calcula la edad en a�os, devolviendo un string: " <i>N </i> a�os"
	 * 
	 * @return boolean
	 * @param birthDate
	 *            java.util.Date
	 * @param currentDate
	 *            java.util.Date
	 */
	public static String getAge(java.util.Date birthDate,
			java.util.Date currentDate) {
		if (birthDate != null && currentDate != null) {
			SimpleDateFormat year = new SimpleDateFormat("yyyy",Locale.getDefault());
			SimpleDateFormat month = new SimpleDateFormat("MM",Locale.getDefault());

			Integer intYear = new Integer(
					(new Integer(year.format(currentDate))).intValue()
							- (new Integer(year.format(birthDate))).intValue());
			Integer diffMonth = new Integer((new Integer(month
					.format(currentDate))).intValue()
					- (new Integer(month.format(birthDate))).intValue());
			String age = intYear.toString() + ","
					+ percentDiffMonths(diffMonth) + " a�os";

			return age;
		} else {
			return "";
		}
	}

	private static String percentDiffMonths(Integer diff) {
		return (diff.intValue() == 0 ? "0"
				: (diff.intValue() < 0 ? (new Integer(
						(int) ((diff.intValue() + 12) * 10 / 12))).toString()
						: (new Integer((int) (diff.intValue() * 10 / 12)))
								.toString()));
	}

	/**
	 * Recibe dos fechas en objetos java.util.Date y devuelve true si la primera
	 * es menor a la segunda, <br>
	 * caso contrario devuelve false.
	 * 
	 * @return boolean
	 * @param firstDate
	 *            java.util.Date
	 * @param secondDate
	 *            java.util.Date
	 */
	public static boolean isLess(Date firstDate, Date secondDate) {
		return (firstDate.getTime() < secondDate.getTime());
	}
	
	public static boolean isEqual(Date firstDate, Date secondDate) {
		return (firstDate.getTime() == secondDate.getTime());
	}

	/**
	 * Recibe dos fechas en objetos java.util.Date y devuelve la diferencia en
	 * dias. <br>
	 * 
	 * @return int
	 * @param firstDate
	 *            java.util.Date
	 * @param secondDate
	 *            java.util.Date
	 */
	public static int getDifferenceDays(Date firstDate, Date secondDate) {
		Date newDate = secondDate;
		long mTime = secondDate.getTime();
		long plusTime = 0;
		int i = 0;
		while (!newDate.equals(firstDate)
				&& !getSimpleFormattedDate(newDate).equals(
						getSimpleFormattedDate(firstDate))) {
			i++;
			plusTime = (new Long((long) HOURS * MINUTES * SECONDS
					* MILLISECONDS * i)).longValue();
			if (isLess(firstDate, secondDate))
				newDate = new Date(mTime - plusTime);
			else
				newDate = new Date(mTime + plusTime);
		}
		return i;
	}

	/**
	 * Recibe una fecha en objeto java.util.Date y la una cantidad de dias 
	 * y devuelve una nueva fecha que es la fecha recibida + los dias. <br>
	 * 
	 * @return java.util.Date
	 * @param aDate
	 *            java.util.Date
	 * @param days
	 *            int
	 */
	public static Date getAdditionDays(Date aDate, int days) {
		long plusTime = days * HOURS * MINUTES * SECONDS * MILLISECONDS;
		Date newDate = new Date(aDate.getTime() + plusTime);
		return newDate;
	}
	
	public static Date getAdditionYears(Date aDate, int years) {
		GregorianCalendar gc=new GregorianCalendar();
		gc.setTime(aDate);
		gc.add(GregorianCalendar.YEAR, years);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		return gc.getTime();
	}	

	public static Date removeTime(Date aDate){
		GregorianCalendar gc=new GregorianCalendar();
		gc.setTime(aDate);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		return gc.getTime();
		
	}
	
	public static boolean esMenorIgual(Date firstDate, Date secondDate) {
		return (firstDate.getTime() <= secondDate.getTime());
	}
	
}
