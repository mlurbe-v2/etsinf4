package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CookieServlet
 */
@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    Cookie cookie;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CookieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/**
	 *
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		String errPage = ServletUtilities.headWithTitle("No se ha podido crear") +
                "<BODY>\n" +
                "<H1 ALIGN=LEFT>" + "No se ha podido crear" + "</H1>\n" + 
                "<p>Valores incorrectos</p>\n" +
                "</body></html>";
		try {
			//Comprueba que los valores de los campos no estén vacios
			if(request.getParameter("Nombre") != null &&  request.getParameter("Valor") != null &&
					request.getParameter("Nombre") != "" &&  request.getParameter("Valor") != "") {
				String value = request.getParameter("Valor");
				
				//Comprobar que el valor es un entero
				boolean isNumber = true;
				
				for (int i = 0; i < value.length(); i++) {
		            if (!Character.isDigit(value.charAt(i))) {
		                isNumber = false;
		            }
				}
				//Si es un entero, se crea la cookie y se mete en la lista
				if (isNumber) {
					cookie = new Cookie(request.getParameter("Nombre"), request.getParameter("Valor"));
					cookies.add(cookie);
					String title = "Nuevo cookie:";
					out.println(ServletUtilities.headWithTitle(title) +
			                "<BODY>\n" +
			                "<H1 ALIGN=LEFT>" + title + "</H1>\n" + 
			                "<p>Cookie[" + cookie.getName() + "] = " + cookie.getValue() + "\n" +
			                "</body></html>");
				}else {out.println(errPage);}
	
			}
			else {out.println(errPage);}
		}catch(Exception e) {}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Iterator<Cookie> cookieIt = cookies.listIterator();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Lista de Cookies:";
		out.println(ServletUtilities.headWithTitle(title) +
                "<BODY>\n" +
                "<H1 ALIGN=LEFT>" + title + "</H1>\n");
		//Bucle para mostrar las cookies
		while(cookieIt.hasNext()) {
		      Cookie cookieElement = cookieIt.next();
		      out.println("<p>Cookie[" + cookieElement.getName() + "] = " +  cookieElement.getValue() + "\n</p>");
		}
		out.println("</BODY></HTML>");
	}

}
