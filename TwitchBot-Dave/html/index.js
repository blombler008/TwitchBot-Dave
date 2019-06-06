
var jsonD;
var el = document.getElementById('pic');
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

function fadeObject(el, start, end, duration) {
    var range = end - start;
    var goingUp = end > start;
    var steps = duration / 20;   
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