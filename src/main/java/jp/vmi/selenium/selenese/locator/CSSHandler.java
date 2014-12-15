package jp.vmi.selenium.selenese.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

class CSSHandler implements LocatorHandler {

    @Override
    public String locatorType() {
        return "css";
    }

    @Override
    public List<WebElement> handle(WebDriver driver, String arg) {
        /*
            EQF change
            IDE records in some cases invalid css selectors which gets fixed once executed by the IDE
            Example:
            css=div.tag-1. > div.col-md-12 > div.form-group > div.col-md-5 > div.bootstrap-tagsinput > input[type="text"]
            needs to be
            css=div.tag-1 > div.col-md-12 > div.form-group > div.col-md-5 > div.bootstrap-tagsinput > input[type="text"]
         */
        arg = arg.replaceAll("\\. ", " ");
        return driver.findElements(By.cssSelector(arg));
    }
}
