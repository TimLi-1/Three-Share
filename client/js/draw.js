/*
	draw.js

	@author: cheng jiang
	@date: november 2019

*/

var socket;

var current = {
	color: "#000000",
	weight: 1
};

var mode = {
	pencil_draw: 0
}

var pencil = document.getElementById("pencil-tool");
pencil.addEventListener("click", selectPencil);

function selectPencil() {
	mode.pencil_draw = 1;
} 














var weightSlider = document.getElementById("weight-slider");
weightSlider.addEventListener("change", changeWeight, false);

function changeWeight(event) {
	current.weight = event.target.value;
}

function changeColor(jscolor) {
	current.color = '#' + jscolor;
}

function setup() {
	createCanvas(windowWidth, windowHeight);

	socket = io.connect();

	socket.on("draw", (data) => {
		stroke(data.color);
		strokeWeight(data.weight);
		line(data.x0, data.y0, data.x1, data.y1);
	});
}

function draw() {

}

function drawLine(x0, y0, x1, y1) {
	stroke(current.color);
	strokeWeight(current.weight);
	line(x0, y0, x1, y1);
	
	// send to server
	console.log("Client drawing line, sending data to server");
	socket.emit("pencildraw", {
		x0: x0,
		y0: y0,
		x1: x1,
		y1: y1,
		color: current.color,
		weight: current.weight
	});
}

function mousePressed() {
	if (mouseButton === LEFT) {
		current.x = mouseX;
		current.y = mouseY;
	}
}

function mouseDragged(event) {
	if (mouseButton === LEFT) {
		if (event.target.tagName === "CANVAS") {
			if (mode.pencil_draw === 1) {
				drawLine(current.x, current.y, mouseX, mouseY);
				current.x = mouseX;
				current.y = mouseY;
			}
		}
	}
}

// function mouseReleased() {
// 	if (mode.pencil_draw === 1) {
// 		mode.pencil_draw = 0;
// 	}
// }