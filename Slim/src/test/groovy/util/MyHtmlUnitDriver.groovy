package util

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class MyHtmlUnitDriver extends HtmlUnitDriver {
    MyHtmlUnitDriver() {
        super(true)
    }

    @Override
    protected WebClient newWebClient(BrowserVersion version) {
        WebClient webClient = super.newWebClient(version)
        webClient.options.throwExceptionOnScriptError = false
        webClient
    }

}