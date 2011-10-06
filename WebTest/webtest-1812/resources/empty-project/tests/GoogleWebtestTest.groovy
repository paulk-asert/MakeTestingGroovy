import com.canoo.webtest.WebtestCase
import org.junit.Test

/**
The same tests as in googleWebTest.xml and googleWebTestSteps.xml but expressed as Groovy code
*/
public class GoogleWebtestTest extends WebtestCase
{
	private static xpathSearchResults = "//a[@class='l']"

	
	void testGoogle()
	{
		webtest("check that WebTest is Google's top 'WebTest' result [test written as Groovy code]")
		{
			invoke url: "http://www.google.com/ncr", description: "Go to Google (in English)"
			verifyTitle "Google"
			setInputField name: "q", value: "WebTest"
			clickButton "I'm Feeling Lucky"
			verifyTitle "Canoo WebTest"
		}
	}


	void testSearchForGoogleSteps()
	{
		webtest("Search some WebTest steps using Google restricted to webtest.canoo.com [test written as Groovy code]")
		{
			googleOnWebTest "clickLink",
			{
				verifyXPath xpath: xpathSearchResults, text:"Core Step: clickLink", 
					description: "Verify that clickLink's documentation is the first result"
			}
			googleOnWebTest "setFileField",
			{
				verifyXPath xpath: xpathSearchResults, text:"Core Step: setFileField", 
					description: "Verify that setFileField's documentation is the first result"
			}
			googleOnWebTest "notExistingStep",
			{
				verifyText text: "Your search .* did not match any documents.", regex: "true"
			}
		}
	}

	/**
	 * Extracted for reuse
	 * @param searchText the text to search for
	 * @param verification the steps to executed once the results page is reached
	 */
	private googleOnWebTest(String searchText, Closure verification)
	{
		ant.group description: "search for $searchText",
		{
			invoke url: "http://www.google.com/ncr", description: "Go to Google (in English)"
			clickLink "Advanced Search"
			setInputField description: "Set the search value", name: "as_q", value: searchText
			setInputField description: "Restrict search to Webtest's website", name: "as_sitesearch", value: "webtest.canoo.com"
			clickButton label: "Advanced Search"
			verification.delegate = ant
			verification()
		}
	}
}
