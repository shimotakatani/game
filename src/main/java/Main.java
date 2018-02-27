import logger.SparkUtils;
import org.apache.log4j.BasicConfigurator;
import rest.MainResource;

import org.apache.log4j.Logger;

import static spark.Spark.port;

/**
 * create time 27.02.2018
 *
 * Метод для запуска всех служб и основных процессов
 * @author nponosov
 */
public class Main {
    public static Logger logger = Logger.getLogger(MainResource.class);

    public static void main(String[] args) {


        BasicConfigurator.configure();

        SparkUtils.createServerWithRequestLog(logger);
        port(4567);

        MainResource.publicResource();

        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    logger.info("Hello from infinity loop\n");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });

        gameThread.start();

    }
}
