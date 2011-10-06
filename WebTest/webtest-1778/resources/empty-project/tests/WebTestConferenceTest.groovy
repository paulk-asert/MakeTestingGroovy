import com.canoo.webtest.WebtestCase
import org.junit.Test

/**
The same tests as in webTestConference.groovy but expressed as Groovy code
*/
public class WebTestConferenceTest extends WebtestCase
{

	void testRegisterCommittersToWebtestConference()
	{
		webtest("[should fail!] Example of WebTest usage in TDD for a not yet existing website [test written as Groovy code]")
		{
			invoke "http://conference.webtest.canoo.com", description: "Go to the conference site"
			verifyTitle "WebTest Conference"
			verifyText "The most effective way to test your web application"

			clickLink "Register..."

			group description: "Register WebTest committers for the conference (aren't they already speakers?)",
			{
				setInputField forLabel: "Number of participant",  "4"
				clickButton "continue"
				fillParticipantData firstName: "Denis", lastName: "Antonioli"
				fillParticipantData firstName: "Dierk", lastName: "König", index: "2"
				fillParticipantData firstName: "Marc", lastName: "Guillemot", index: "3"
				fillParticipantData firstName: "Paul", lastName: "King", index: "4"

				clickLink "Complete registration"
				verifyText "Registration completed"
			}
		}
	}
}
