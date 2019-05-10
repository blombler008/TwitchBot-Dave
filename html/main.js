function start() {
	setInterval(bcd, 1000);
}

function bcd() {
	fetch('/json') 
		.then(res => res.json())
		.then((out) => { 	
			console.log('Output: ',JSON.stringify(out));
	}).catch(err => console.error(err));
}

