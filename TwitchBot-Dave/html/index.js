//function start() {
	/*$(document).ready(function() {
		bcd();
		setInterval(bcd, 1000);
	});*/
//}
var jsonD;
var el = document.getElementById('tile');
var e2 = document.getElementById('tile2');
var faded = false;
function bcd() {
	fetch('/json') 
		.then(res => res.json())
		.then((out) => { 	
			console.log('Output: ',updateTile(JSON.stringify(out)));
	}).catch(err => console.error(err));
}

function updateTile(json) {
	this.jsonD = JSON.parse(json);
	tile2.innerHTML = json;
	if(jsonD.catch === "true") {
		if(faded === false) {
			faded = true;
			fadeObject(el, 0, 1, 2000);
		}
	} else {
		if(faded === true) {
			faded = false;
			fadeObject(el, 1, 0, 2000);
		}
	}
	return jsonD;
}
function toggleOpacity(id) {
    var el = document.getElementById(id);
    if (el.style.opacity == 1) {
        fadeObject(el, 0, 1, 2000);
    } else {
        fadeObject(el, 1, 0, 2000);
    }
}

function fadeObject(el, start, end, duration) {
    var range = end - start;
    var goingUp = end > start;
    var steps = duration / 20;   // arbitrarily picked 20ms for each step
    var increment = range / steps;
    var current = start;
    var more = true;
    function next() {
        current = current + increment;
        if (goingUp) {
            if (current > end) {
                current = end;
                more = false;
            }
        } else {
            if (current < end) {
                current = end;
                more = false;
            }
        }
        el.style.opacity = current;
        if (more) {
            setTimeout(next, 20);
        }
    }
    next();
}



