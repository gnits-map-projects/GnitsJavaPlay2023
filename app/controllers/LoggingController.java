package controllers;

import play.mvc.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Result;

public class LoggingController extends Controller {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer riskyCalculation(){
        logger.warn("Will result in java.lang.ArithmeticException: / by zero");
        return 10/0;
    }

    private Integer riskyCalculation1(){
        return 10/5;
    }

    public Result executeLoggers(){
        // Log some debug info
        logger.info("Attempting risky calculation.");

        try {
            final int result = riskyCalculation();

            // Log result if successful
            logger.info("Result={}", result);
//            return ok(views.html.logging.render("info", "Result is " + Integer.toString(result)));
        } catch (Throwable t) {
            // Log error with message and Throwable.
            logger.error("Exception with riskyCalculation", t);
//            return ok(views.html.logging.render("danger", "Problem is " + t.getMessage()));
        }
        return ok(views.html.logging.render("info", "Execution done"));
    }

}
