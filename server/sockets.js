/*
	sockets.js

	@author: cheng jiang
	@date: november 2019

*/

var socket = require("socket.io");

function startListening(server) {
	io = socket.listen(server);

	io.on("connection", (socket) => {
		socketConnection(socket);
	});
}

function socketConnection(socket) {

	console.log(socket.id + " has connected");

	socket.on("pencildraw", (data) => {
		socket.broadcast.emit("draw", data);
	});

	socket.on("disconnect", () => {
		console.log(socket.id + " has disconnected");
	})
}

if (exports) {
	exports.listen = startListening;
}