/**
Dynamically enhance the links "Resulting page" from the report to provide a simplified visualisation of the captured responses
*/
var tabResultLinks = [];
var mapHRef2Links = new Object(); // main link href -> link object
function loadJob()
{
	var baseUrl = readBaseUrl();
	var cookie = readCookie();

	var colLinks = document.getElementsByTagName("A");
	var nb = 0;
	for (var i=0; i<colLinks.length; ++i)
	{
		var oLink = colLinks[i];
		// improve XSL to have an other criterium, eg a class
		var bResultPageLink = (oLink.innerHTML == "Resulting page")
		var bGroupResultPageLink = (oLink.innerHTML == "last result page of this group")
		if (bResultPageLink || bGroupResultPageLink)
		{
			oLink.onclick = handleResultLinkClick;
			oLink.title += " (Ctrl+mouse over: preview floating over this page, Ctrl+Click: opens in results browser)";
			oLink.onmouseover = handleResultMouseOver;
			oLink.onmouseout = handleResultMouseOut;
			addReferenceParameters(oLink, baseUrl, cookie); 

			if (bResultPageLink)
			{
				var newResult = {
					resultFilename: oLink.href, 
					link: oLink, 
					index: tabResultLinks.length,
					tabResultLinks: tabResultLinks
				};
				tabResultLinks[tabResultLinks.length] = newResult;
				mapHRef2Links[oLink.href] = oLink
			}
			++nb;
		}
	}
}

function addEvent(_obj, _eventType, _function)
{
	if (_obj.addEventListener)
		_obj.addEventListener(_eventType, _function, false); 
	else if (_obj.attachEvent)
		_obj.attachEvent("on" + _eventType, _function);
}
addEvent(window, 'load', loadJob);

/**
Handles the click on a link to a result file
If no special key is pressed, nothing special is done and the link can be normally opened
*/
function handleResultLinkClick(_event)
{
	var event = _event ? _event : window.event;
	if (event.ctrlKey)
	{
		window.selectedLink = mapHRef2Links[this.href];
		// compute the location of the html file: in the same dir as this js file but maybe not in the same dir as the report
		var oScript = document.getElementById("scriptResponseBrowser");
		var strResponseBrowserHref = oScript.src.replace(".js", ".html"); // -> responseBrowser.html in the right dir
		// Shift gives the possibility to open a new Overview window. This may be usefull to compare 2 results films
		var targetWindowName = event.shiftKey ? "_blank" : "overview";
		window.open(strResponseBrowserHref, targetWindowName);
		event.cancelBubble = true;
		return false;
	}
	// normal click, do nothing
	return true;
}

/**
Shows a preview in a floating div if the ctr key is pressed while moving the pointer on a link
*/
function handleResultMouseOver(_event)
{
	var event = _event ? _event : window.event;
	if (event.ctrlKey)
	{
		showPreview(this);
	}
}

/**
Hides the preview (if it was visible)
*/
function handleResultMouseOut(_event)
{
	if (window.oPreviewDiv != null)
	{
		window.oPreviewDiv.style.visibility = "hidden";
	}
}

/**
Load the href of the link in a floating preview frame
*/
function showPreview(_oLink)
{
	var bIsIE = (document.all != null); // IE CSS support is not that good
	if (window.oPreviewDiv == null)
	{
		window.oPreviewDiv = document.createElement("div");
		oPreviewDiv.id = "previewDiv";
		document.body.appendChild(oPreviewDiv);
		oPreviewDiv.style.position = "fixed";
		oPreviewDiv.style.backgroundColor = "white";
		oPreviewDiv.style.borderStyle = "solid";
		oPreviewDiv.style.borderWidth = "5pt";
		oPreviewDiv.style.borderColor = "blue";

		oPreviewDiv.style.width = "70%";
		oPreviewDiv.style.height = "90%";
		oPreviewDiv.style.left = "28%";
		oPreviewDiv.style.top = "5%";
		oPreviewDiv.style.zIndex = 100;

		window.oIframe = document.createElement("iframe");
		oPreviewDiv.appendChild(oIframe);
		oIframe.style.width = "100%";
		oIframe.style.height = "100%";
		oIframe.style.overflow = "hidden";

		if (bIsIE)
		{
			oPreviewDiv.style.position = "absolute";
			oPreviewDiv.style.width = screen.width * 0.7;
			oPreviewDiv.style.height = screen.height * 0.9;
			oIframe.style.overflowY = "hidden";
			oIframe.style.overflowX = "hidden";
		}
	}

	oIframe.src = _oLink.href;
	oPreviewDiv.style.visibility = "visible";
	if (bIsIE)
	{
		oPreviewDiv.style.top = document.body.parentElement.scrollTop + screen.height * 0.05;
	}
}

/**
Reads the value of the (first) base url
*/
function readBaseUrl()
{
	return evaluateXPath("//a[@class='baseUrl']/@href");
}

/**
Read the name and value of the session cookie of the WebTest (if any).
This cookie need to have been read with <storeCookie property="wtCookie" .../>
*/
function readCookie()
{
	var cookieProperty = "wtSessionCookie";
	var baseXpath = "//td[@class = 'parameterValue' and text() = '" + cookieProperty + "']/../../tr/td[@class = 'parameterName' and text() = '@myParam@']/following-sibling::td/text()";
	var value = evaluateXPath(baseXpath.replace("@myParam@", "-> cookie value"));
	if (!value)
		return null;
	var name = evaluateXPath(baseXpath.replace("@myParam@", "name"));
	return {'name': name, 'value': value};
}

/**
Evaluates the string value of the xpath expression
*/
function evaluateXPath(_strXPath)
{
	if (document.evaluate)
		return document.evaluate(_strXPath, document, null, XPathResult.STRING_TYPE, null).stringValue;
	// TODO: make it for IE too
	return null;
}

/**
Adds the base url for the test and the session cookie information (if any) to the link.
These information may be read afterwards, for instance by the WebTestRecorder to allow to jump into the session
*/
function addReferenceParameters(_oLink, _baseUrl, _cookie)
{
	if (!_baseUrl)
		return;

	var newHref = _oLink.getAttribute("href") + "?baseUrl=" + escape(_baseUrl)
	if (_cookie)
	{
		newHref += "&cookieName=" + escape(_cookie.name)
		newHref += "&cookieValue=" + escape(_cookie.value)
	}
	_oLink.setAttribute("href", newHref)
}