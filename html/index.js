//function start() {
	bcd();
	setInterval(bcd, 1000);
//}

function bcd() {
	fetch('/json') 
		.then(res => res.json())
		.then((out) => { 	
			console.log('Output: ',updateTile(JSON.stringify(out)));
	}).catch(err => console.error(err));
}

function updateTile(json) {
	var tile = document.getElementById('tile');
	tile.innerHTML = json;
}



