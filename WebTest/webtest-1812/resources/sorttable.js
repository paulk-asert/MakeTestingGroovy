/**
Adapted from sorttable
http://kryogenix.org/code/browser/sorttable/
*/
addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

// IE doesn't know push
if (!Array.prototype.push)
{
	Array.prototype.push = function(_obj)
	{
		this[this.length] = _obj;
	}
}

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    var tbls = document.getElementsByTagName("table");
    for (var ti=0; ti<tbls.length; ti++) {
        var thisTbl = tbls[ti];
        if (((' '+thisTbl.className).indexOf("sortable") != -1)) 
        {
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    if (!table.rows || table.rows.length == 0) {
    	return;
    }
    var firstRow = table.rows[0];
    
    table.sortable_spans = new Array();
    
    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) 
    {
        var cell = firstRow.cells[i];
        var txt = ts_getInnerText(cell);
        cell.onclick = ts_resortTable; 
        cell.title = "Sort by: " + txt;

        var oSpan = document.createElement("SPAN");
        oSpan.className = "sortarrow"
        oSpan.appendChild(document.createTextNode("   "));
        cell.sortable_span = oSpan;
        cell.sortable_colid = i;
        cell.sortable_table = table;
        cell.appendChild(oSpan);
        cell.style.cursor = "pointer";
        table.sortable_spans.push(oSpan);
    }
}

function ts_getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") return el;
	if (el.innerText) return el.innerText;	//Not needed but it is faster
	var str = "";
	
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				if (cs[i].tagName == "IMG")
					str += cs[i].alt;
				str += ts_getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function ts_resortTable() 
{
    // get the span
    var span = this.sortable_span;
    var td = this;
    var column = this.sortable_colid;
    var table = this.sortable_table;
    
    // Work out a type for the column
    if (table.rows.length <= 1) return;
    var itm = ts_getInnerText(table.tBodies[0].rows[0].cells[column]);
    var sortfn = ts_sort_caseinsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^[?$]/)) sortfn = ts_sort_currency;
    if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    SORT_COLUMN_INDEX = column;
   	var colRows = table.rows;
   	var startIndex = 1;
    if (table.tHead)
    {
    	colRows = table.tBodies[0].rows;
    	var startIndex = 0;
    }
    var newRows = new Array();
    for (var i=startIndex; i<colRows.length; ++i) 
    { 
    	var curRow = colRows[i];
    	if (!curRow.sortable_originalPosition)
    		curRow.sortable_originalPosition = i;
    	newRows.push(curRow);
    }

    newRows.sort(sortfn);

    if (span.getAttribute("sortdir") == 'down') {
        ARROW = '&nbsp;&nbsp;&uarr;';
        newRows.reverse();
        span.setAttribute('sortdir', 'up');
    } else {
        ARROW = '&nbsp;&nbsp;&darr;';
        span.setAttribute('sortdir', 'down');
    }

    // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    // don't do sortbottom rows
    for (var i=0;i<newRows.length;i++)
    {
    	var curRow = newRows[i];
    	if (!curRow.className || (curRow.className && (curRow.className.indexOf('sortbottom') == -1))) 
    		table.tBodies[0].appendChild(curRow);
    }
    // do sortbottom rows only
    for (var i=0;i<newRows.length;i++) 
    { 
    	var curRow = newRows[i];
    	if (curRow .className && (curRow .className.indexOf('sortbottom') != -1)) 
    		table.tBodies[0].appendChild(curRow );
    }
    
    // Delete any other arrows there may be showing
    for (var i=0; i<table.sortable_spans.length; i++) 
    {
    	table.sortable_spans[i].innerHTML = '&nbsp;&nbsp;&nbsp;';
    }
        
    span.innerHTML = ARROW;
}

function ts_sort_date(a,b) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    var aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    var bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return ts_sort_originalPosition(a, b);
    if (dt1<dt2) return -1;
    return 1;
}

function ts_sort_currency(a,b) { 
    var aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    var bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
    return ts_defaultPositionCompareIfNull(aa - bb, a, b)
}

function ts_sort_numeric(a,b) { 
    var aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    var bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX])); 
    if (isNaN(bb))
    	bb = 0;
    return ts_defaultPositionCompareIfNull(aa - bb, a, b)
}

function ts_sort_caseinsensitive(a,b) {
    var aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    var bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa == bb) return ts_sort_originalPosition(a, b);
    if (aa<bb) return -1;
    return 1;
}

function ts_sort_default(a,b) {
    var aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    var bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa == bb) return ts_sort_originalPosition(a, b);
    if (aa < bb) return -1;
    return 1;
}

function ts_defaultPositionCompareIfNull(_result, _rowA, _rowB)
{
	return _result != 0 ? _result : ts_sort_originalPosition(_rowA, _rowB);
}

function ts_sort_originalPosition(a, b)
{
	return a.sortable_originalPosition - b.sortable_originalPosition;
}


function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
{
  if (elm.addEventListener) {
    elm.addEventListener(evType, fn, useCapture);
  }
  else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
  }
  else {
    alert("Handler could not be added");
  }
} 
