// -----------------------------------------------------------------------------------------------------------------
// Documentation of showHide.js
// -----------------------------------------------------------------------------------------------------------------

/*
This javascript is used to show or hide substeps of the Canoo Webtest 'group' steptype in a HTML Webtest Resultfile.

The script provides functions to show or hide substeps for different levels in the hierarchically structured html tree:

	1) For a single group step: 	showSubstepsOfGroup / hideSubstepsOfGroup / changeLevelOfDetailForGroup
	2) For a testspec: 		showAllSubstepsOfTestspec / hideAllSubstepsOfTestspec
	3) For the entire document: 	hideAllSubstepsOnPage

The function's algorithm is as follows:

 1)   Functions for a single group step:

      The functions for a single group step get as argument the node which represents
      the image that has been clicked in the browser to show or hide the substeps of a group step.
      These functions navigate in the DOM tree to the node which is the parent of all substeps of the group.
      For each substep the attribute 'style.display' is assigned the appropriate value to show or hide the substep.

 2/3) Functions for a testspec or the entire document:

      These functions get as argument the node which represents the image that has been clicked
      in the browser to show or hide the substeps of the group steps within a tespec or within the entire
      document. They determine each image in the scope of the testpec respectively in the document
      that is used to show or hide the substeps of a single group step and use each of these images
      to call the according function for a single group step to show or hide the substeps for each
      of the group steps.

The functions are based on the following structure of the HTML document:

 1)   showSubstepsOfGroup/hideSubstepsOfGroup:

		<tr> <!-- tablerow for one step in a testspec -->
			<td> 	<!-- (( 2 )) SKRIPT: td node = imageForGroup.parentNode -->
				<!-- position of step in testspec -->
					<b>1</b>
					<br>
					<img onclick="changeLevelOfDetailForGroup(this)" name="collapseButton" src="./images/expandall.png">
					<!-- (( 1 )) SKRIPT: img node = imageForGroup -->
			</td>
			<td>	<!-- result of step execution (failed, successful, not executed) -->
				<img src="./images/ok.gif">
			</td>
			<td>	<!-- stepType and stepID -->
				<b>group</b>
				<br>
				stepID here and optional lastResult
			</td>
			<td> <!-- (( 3 )) SKRIPT: td node = tdContainingImage.nextSibling.nextSibling.nextSibling -->
				<table>
					<tr><th>No</th><th>Result</th><th>Name</th><th>Parameter</th><th>Duration</th></tr>
					<tr> ... substeps of group step ... </tr>

 2)   showAllSubstepsOfTestspec/hideAllSubstepsOfTestspec:

			<p>  <!-- (( 2 )) SKRIPT: p node: pContainingImage = imageForTestSpec.parentNode
				<img alt="show all substeps of 'group' steps in testspec" src="./images/expandall.png" onclick="showAllSubstepsOfTestspec(this)">
				<img alt="hide all substeps of 'group' steps in testspec" src="./images/collapseall.png" onclick="hideAllSubstepsOfTestspec(this)">
				<!-- (( 1 )) SKRIPT: img node = imageForTestSpec -->
			</p>

			<table> <!-- (( 3 )) SKRIPT: table node =  pContainingImage.nextSibling -->
				<tr><th>No</th><th>Result</th><th>Name</th><th>Parameter</th><th>Duration</th></tr>
				<tr> ... steps of testspec ... <tr>
*/

var fExpandSource, fCollapseSource;

/**
 * Function must be called early, such as 'onLoad', to set the two global variables with the two parameters.
 * @param expandSourceVar name of the icon file to show when the group is closed.
 * @param collapseSourceVar name of the icon file to show when the group is open.
 */
function initExpandCollapse(expandSourceVar, collapseSourceVar) {
	fExpandSource = expandSourceVar;
	fCollapseSource = collapseSourceVar;
}

/**
 * Hide all sub-steps of a test spec.
 * The desired test spec is identified by the 'collapse' image displayed above the table.
 */
function hideAllSubstepsOfTestspec(imageForTestSpec) {
	hideAllSubsteps(locateTestSpecTable(imageForTestSpec));
}

function hideAllSubsteps(nodeContainingStepsOfTestspec) {
    var images = nodeContainingStepsOfTestspec.getElementsByTagName("img");
	for (var i = 0; i < images.length; i++) {
		if (images[i].getAttribute("name") == "collapseButton") {
			hideSubstepsOfGroup(images[i]);
		}
	}
}

/**
 * Show all sub-steps of a test spec.
 * The desired test spec is identified by the 'expand' image displayed above the table.
 */
function showAllSubstepsOfTestspec(imageForTestSpec) {
	showAllSubsteps(locateTestSpecTable(imageForTestSpec));
}

function showAllSubsteps(nodeContainingStepsOfTestspec) {
    var images = nodeContainingStepsOfTestspec.getElementsByTagName("img");
	for (var i = 0; i < images.length; i++) {
		if (images[i].getAttribute("name") == "collapseButton") {
			showSubstepsOfGroup(images[i]);
		}
	}
}

function changeLevelOfDetailForGroup(imageForGroup) {
	if (imageForGroup.src.indexOf(fCollapseSource) > -1) {
		hideSubstepsOfGroup(imageForGroup);
	} 
	else {
		showSubstepsOfGroup(imageForGroup);
	}
}

function hideSubstepsOfGroup(imageForGroup) {
	changeSubstepsOfGroup(imageForGroup, "collapsed", fExpandSource);
}

function showSubstepsOfGroup(imageForGroup) {
	changeSubstepsOfGroup(imageForGroup, "expanded", fCollapseSource);
}

function changeSubstepsOfGroup(imageForGroup, displayStyle, imageName) {
	// how to locate table[@class="expanded"] or table[@class="collapsed]?

	// moves two nodes up from the image to the row, then gets the 4th td
	// this handle any possible child that aren't element
	var tdContainingSubsteps = imageForGroup.parentNode.parentNode.getElementsByTagName('TD')[3];
	var substeps = tdContainingSubsteps.childNodes;
	for (var j = 1; j < substeps.length; j++) {
		// Note: nodeType==1 denotes Element; see e.g. <http://www.zytrax.com/tech/dom/nodetype.html>
		if (substeps[j].nodeType == 1) {
			substeps[j].className = displayStyle;
		}
	}
	imageForGroup.src = imageForGroup.src.substr(0, imageForGroup.src.lastIndexOf("/") + 1) + imageName;
}

/**
 * Locate the table that belongs to a global 'collapse' or 'expand' button.
 */
function locateTestSpecTable(imageForTestSpec) {
	var pContainingStepsOfTestspec = imageForTestSpec.parentNode;
	do	{
		pContainingStepsOfTestspec = pContainingStepsOfTestspec.nextSibling;
		// Note: nodeType==1 denotes Elements; see e.g. <http://www.zytrax.com/tech/dom/nodetype.html>
	}
	while (pContainingStepsOfTestspec.nodeType != 1);
	return pContainingStepsOfTestspec;
}

/**
 * Make sure that the target of the link is visible.
 * @param link An anchor to an error.
 */
function showTargetStep(link) {
	if (link.href.indexOf("#") > -1) {
		showStep(getNamedAnchor(link.href.substr(link.href.indexOf("#") + 1)));
	}
	// lets the browser follows the link
	return false;
}

/**
 * Locate the error on the page.
 * @param name The name of the anchor
 * @return node The anchor for the error. Is null if the anchor isn't on the page.
 */
function getNamedAnchor(name) {
	var elements = document.getElementsByName(name);
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].nodeName == "A") {
			return elements[i];
		}
	}
	return null;
}

/**
 * Expand all levels from a failing node to the root of the webtest table.
 * @param node The anchor for the error.
 */
function showStep(node) {
	if (node == null) {
		return;
	}

	do {
		node = node.parentNode;
	} while (node.nodeName != "TABLE");

	if (node.className == "collapsed") {
		showStep(node);
		// moves two nodes up from the table to the row, then gets the 1st (and only) image in the 1st td
		// this handle any possible child that aren't element
		showSubstepsOfGroup(node.parentNode.parentNode.getElementsByTagName('TD')[0].getElementsByTagName('IMG')[0]);
	}
}

/**
 Toggle the display of the first nextSibling of _node with nodeName _type (if any)
 */
function toggleDisplayNext(_node, _type) {
	var oNode = findNext(_node, _type);
	if (oNode) {
		var bDisplayed = (oNode.style.display != "none");
		oNode.style.display = bDisplayed ? "none" : "inline";
		var oImg = findToggleImage(_node);
		if (oImg) {
			var newFileName = bDisplayed ? "expandPlus.png" : "expandMinus.png";
			oImg.src = oImg.src.replace(/[^/]*$/, newFileName);
			oImg.alt = bDisplayed ? "Show " : "Hide ";
		}
	}
}

/**
 Finds the first nextSibling of _node with the provided node name
 */
function findNext(_node, _nodeName) {
	var nextNode = _node.nextSibling;
	while (nextNode != null && nextNode.nodeName != _nodeName) {
		nextNode = nextNode.nextSibling;
	}
	return nextNode;
}

/**
 Find the toggle image for the node. This is the node itself (if it is an image) else the first image
 child of the node.
 */
function findToggleImage(_oNode) {
	if (_oNode.tagName == "IMG") {
		return _oNode;
	}

	var imgs = _oNode.getElementsByTagName("img");
	return (imgs.length > 0) ? imgs[0] : null;
}
