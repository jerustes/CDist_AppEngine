package es.uc3m.it.cdGAE.myDistributedWebApp;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HTTPRequestParser {
    boolean get = false;
    // to store MIME content type (if the request has a body) 
    String requestContentType = null;
    // to store content length (if the request has a body) 
    int requestContentLength;
    // to store HTTP parameters 
    Map parameters = null;
    // to store HTTP headers 
    Map<String, String> headers = null;
    String remoteIP;
    String remoteHost;
    int remotePort;
    String contextPath;
    String requestURI;
    String queryString;
    String printableBody;
    private final static Logger log = Logger.getLogger(HTTPRequestParser.class
            .getName());

    public HTTPRequestParser(HttpServletRequest req) {

        get = HTTPRequestParser.isGET(req);
        parameters = HTTPRequestParser.processParameters(req);

        headers = HTTPRequestParser.processHeaders(req);

        requestContentLength = req.getContentLength();
        requestContentType = req.getContentType();
        remoteHost = req.getRemoteHost();
        remoteIP = req.getRemoteAddr();
        remotePort = req.getRemotePort();
        contextPath = req.getContextPath();
        requestURI = req.getRequestURI();
        queryString = req.getQueryString();

        printableBody = HTTPRequestParser.getPrintableBody(req);

    }

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public int getRequestContentLength() {
        return requestContentLength;
    }

    public void setRequestContentLength(int requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        String result = "Incoming request from " + remoteHost + "(" + remoteIP
                + ") at " + remotePort + "\n";
        result += "\t" + (isGet() ? "GET" : "POST") + " " + contextPath + "\n";

        result += "\t" + "Browser URI:" + requestURI + ""
                + ((queryString != null) ? queryString : "") + "\n";
        result += "\t" + "Parameters:" + "\n";
        result += "\t" + "Content Type:" + requestContentType + "\n";
        result += "\t" + "Content Length:" + requestContentLength + "\n";
        
        for (Object paramKey_obj : parameters.keySet()) {
            String paramKey_printable;
            // assuming String as key
            try {
                paramKey_printable = (String) paramKey_obj;

            } catch (ClassCastException cce) {
                // cannot print it
                paramKey_printable = paramKey_obj.getClass().getCanonicalName();
            }
            // get parameter values
            Object paramValue_obj = parameters.get(paramKey_obj);
            String paramValue_printable[];
            String paramRepresentation = "";
            try {
                paramValue_printable = (String[]) paramValue_obj;
                for(String val : paramValue_printable)
                    paramRepresentation += "" + val + ",";

            } catch (ClassCastException cce) {
                // cannot print it
                paramRepresentation = paramValue_obj.getClass()
                        .getCanonicalName();
            }
            result += "\t" + "\t" + paramKey_printable + " : "
                    + paramRepresentation + "\n";

        }
        result += "\t" + "Headers:" + "\n";
        for (String headerName : headers.keySet()) {
            result += "\t" + "\t" + headerName + " : "
                    + headers.get(headerName) + "\n";
        }
        result += "\t" + "Body:" + "\n";
        result += printableBody;
        return result;
    }

    // static methods for comfortable access to HTTP processing 

    public static String getPrintableBody(HttpServletRequest req) {
        // would only print text/plain, text/html ... 
        // lazy check, first null, then text ;) 
        if ((req.getContentType() != null)
                && (req.getContentType().startsWith("text"))) {
            try {
                BufferedReader br = req.getReader();
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (IOException e) {
                //better to return an empty string than showing exceptions to the client

                return new String("");
            }

        }
        log.info("No body");
        return new String("");
    }

    public static int getContentLength(HttpServletRequest req) {
        return req.getContentLength();
    }

    public static String getContentType(HttpServletRequest req) {
        return req.getContentType();
    }

    public static boolean isGET(HttpServletRequest req) {
        return req.getMethod().equalsIgnoreCase("GET");
    }

    public static boolean isPOST(HttpServletRequest req) {
        return !isGET(req);
    }

    public static Map<String, String> processHeaders(HttpServletRequest req) {
        Enumeration<String> headerNames = req.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    public static Map processParameters(HttpServletRequest req) {
        return req.getParameterMap();
    }
    
	private final static Logger log = Logger.getLogger(RequestParserServlet.class.getName());

}