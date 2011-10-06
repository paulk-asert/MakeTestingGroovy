tellurium{
    //embedded selenium server configuration
    embeddedserver {
        //port number
        port = "4444"
        //whether to use multiple windows
        useMultiWindows = false
        //whether to run the embedded selenium server. If false, you need to manually set up a selenium server
        runInternally = true
        //profile location
        profile = ""
        //user-extension.js file
        userExtension = "target/classes/extension/user-extensions.js"
    }
    //event handler
    eventhandler{
        //whether we should check if the UI element is presented
        checkElement = false
        //wether we add additional events like "mouse over"
        extraEvent = true
    }
    //data accessor
    accessor{
        //whether we should check if the UI element is presented
        checkElement = true
    }
    //the configuration for the connector that connects the selenium client to the selenium server
    connector{
        //selenium server host
        //please change the host if you run the Selenium server remotely
        serverHost = "localhost"
        //server port number the client needs to connect
        port = "4444"
        //base URL
        baseUrl = "http://localhost:8080"
        //Browser setting, valid options are
        //  *firefox [absolute path]
        //  *iexplore [absolute path]
        //  *chrome
        //  *iehta
//        browser = "*chrome"
        browser = "*firefox3 C:/Program Files (x86)/Mozilla Firefox/firefox.exe"
        //user's class to hold custom selenium methods associated with user-extensions.js
        //should in full class name, for instance, "com.mycom.CustomSelenium"
        customClass = ""
    }
    datadriven{
        dataprovider{
            //specify which data reader you like the data provider to use
            //the valid options include "PipeFileReader", "CVSFileReader" at this point
            reader = "PipeFileReader"
        }
    }
    test{
        //at current stage, the result report is only for tellurium data driven testing
        //we may add the result report for regular tellurium test case
        result{
            //specify what result reporter used for the test result
            //valid options include "SimpleResultReporter", "XMLResultReporter", and "StreamXMLResultReporter"
            reporter = "XMLResultReporter"
            //the output of the result
            //valid options include "Console", "File" at this point
            //if the option is "File", you need to specify the file name, other wise it will use the default
            //file name "TestResults.output"
            output = "Console"
            //test result output file name
            filename = "TestResult.output"
        }
        exception{
            //whether Tellurium captures the screenshot when exception occurs.
            //Note that the exception is the one thrown by Selenium Server
            //we do not care the test logic errors here
            captureScreenshot = true
            //we may have a series of screenshots, specify the file name pattern here
            //Here the ? will be replaced by the timestamp and you might also want to put
            //file path in the file name pattern
            filenamePattern = "Screenshot?.png"
        }
    }
    uiobject{
        builder{
            //user can specify custom UI objects here by define the builder for each UI object
            //the custom UI object builder must extend UiObjectBuilder class
            //and implement the following method:
            //
            // public build(Map map, Closure c)
            //
            //For container type UI object, the builder is a bit more complicated, please
            //take the TableBuilder or ListBuilder as an example

            //example:
//           Icon="org.tellurium.builder.IconBuilder"

        }
    }
    widget{
        module{
            //define your widget modules here, for example Dojo or ExtJs
//            included="dojo, extjs"
            included=""
        }
    }
}
