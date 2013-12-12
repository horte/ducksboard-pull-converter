import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class DucksboardPullConverter extends HttpServlet {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        //Valid url
        String url = req.getParameter("url");
        String jsonpath = req.getParameter("jsonpath");
        if(url == null || jsonpath == null) {
            resp.getWriter().print("invalid url or jsonpath");
        }

        //Get the content from the url. Should return a json string
        final String content = getContent(url);

        //take the json-path and read the value from the content
        //eg. $.['com.yammer.metrics.web.WebappMetricsFilter'].requests.rate.count
        final Object actual = JsonPath.read(content, jsonpath);


        //Create a ducksboard response
        Map map = new HashMap<String, Object>() {{
            this.put("timestamp", System.currentTimeMillis()/1000);
            this.put("value", actual);
        }};

        //Return response
        resp.getWriter().print(objectMapper.writeValueAsString(map));
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new DucksboardPullConverter()),"/*");
        server.start();
        server.join();
    }


    private String getContent(String a) {

        URL url;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            // get URL content

//            String a="http://localhost:8080/TestWeb/index.jsp";
            url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
//                System.out.println(inputLine);
            }
            br.close();

//            System.out.println("Done");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }
}