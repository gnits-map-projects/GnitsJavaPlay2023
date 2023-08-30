package controllers;

import akka.Done;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.cache.*;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ThreadController extends Controller {

    private HttpExecutionContext httpExecutionContext;
    private AsyncCacheApi cache;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public ThreadController(HttpExecutionContext ec, AsyncCacheApi cache) {
        this.httpExecutionContext = ec;
        this.cache = cache;
    }

    public Result index() throws InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        sleep();
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        Long diff = timestamp1.getTime() - timestamp.getTime();
        return ok("time taken is " + (diff/1000.0) + "s");
    }

    public String sleep() throws InterruptedException {
        // 15 seconds sleep
        logger.info("Inside sleep method!");
        Thread.sleep(15000);
        logger.info("Outside sleep method!");
        return "Done";
    }

    public CompletionStage<Result> indexFuture() {
        // Use a different task with explicit EC
        return calculateResponse()
                .thenApplyAsync(
                        answer -> {
                            return ok("answer was " + answer);
                        },
                        httpExecutionContext.current());
    }

    private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }

    public CompletionStage<Result> indexFutureSleep() {
        return sleepWrap().thenApplyAsync(
                response -> {
                    return ok("Future executed");
                },
                httpExecutionContext.current()
        );
    }

    private CompletionStage<String> sleepWrap() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletionStage<Result> userIndex(Http.Request request, String firstName) {
        return cache.get(firstName).thenApplyAsync(
                lastNameOpt -> {
                    if (lastNameOpt.isPresent()) {
                        return ok("Welcome " + firstName + lastNameOpt.get());
                    } else {
                        Optional<String> lastNameQueryOpt = request.queryString("lastName");
                        if (lastNameQueryOpt.isPresent()) {
                            CompletionStage<Done> result = cache.set(firstName, lastNameQueryOpt.get());
                            return ok("Welcome " + firstName + lastNameQueryOpt.get());
                        } else return ok("Welcome " + firstName);
                    }
                },
                httpExecutionContext.current());
    }
}
