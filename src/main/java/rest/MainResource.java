package rest;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * create time 27.02.2018
 *
 * @author nponosov
 */
public class MainResource {

    public static void publicResource(){
        get("/test", (req, res) -> test());

    }

    private static String test(){
        return "This is test message by rest from easySparkApplication";
    }
}
