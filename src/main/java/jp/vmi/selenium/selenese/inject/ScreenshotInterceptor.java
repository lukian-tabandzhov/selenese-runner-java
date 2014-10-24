package jp.vmi.selenium.selenese.inject;

import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.ScreenshotHandler;
import jp.vmi.selenium.selenese.command.ICommand;
import jp.vmi.selenium.selenese.result.Result;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriverException;

/**
 * Interceptor for screenshot.
 */
public class ScreenshotInterceptor implements MethodInterceptor {

    private static final int CONTEXT = 0;
    private static final int COMMAND = 1;

    /*
     * target signature:
     * Result doCommand(Context context, ICommand command, String... curArgs)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        Context context = (Context) args[CONTEXT];
        ICommand command = (ICommand) args[COMMAND];

        Result result = (Result) invocation.proceed();

        // START - EQF CHANGE
        //if (context instanceof ScreenshotHandler && command.mayUpdateScreen()) {
        if (context instanceof ScreenshotHandler) {

            Thread.sleep(1000);
        // END - EQF CHANGE
            ScreenshotHandler handler = (ScreenshotHandler) context;
            String baseName = context.getCurrentTestCase().getBaseName();

            boolean exceptionFired = false;
            try {
                handler.takeScreenshotAll(baseName, command.getIndex());
            } catch (NoSuchWindowException e) {
                // ignore if failed to capturing.
                exceptionFired = true;
            } catch (WebDriverException e) {
                e.printStackTrace();
                exceptionFired = true;
                // ignore if failed to capturing
            }
            if (!result.isSuccess() && !exceptionFired)
                handler.takeScreenshotOnFail(baseName, command.getIndex());
        }

        return result;
    }
}
