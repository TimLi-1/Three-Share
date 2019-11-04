/*
	server.js

	@author: cheng jiang
	@date: november 2019

*/

// import modules
var http = require("http");
var url = require("url");
var path = require("path");
var fs = require("fs");

// http
var root = path.join(__dirname, '..', 'client');
var entrypath = path.join(root, "workspace.html");

var server = http.createServer(handler);
server.listen(8080);
console.log("Server started, listening on port 8080");
console.log("Root path is " + root);

function handler(request, response) {

	var pathname = request.url;

	if (pathname === "/") {
		var filepath = entrypath;
	}
	else {
		var filepath = path.join(root, pathname);
	}

	console.log("Requested file path is " + filepath);

	var ext = path.extname(filepath);

	var typeExt = {
		".html": "text/html",
		".js": "text/javascript",
		".css": "text/css"
	};

	var contentType = typeExt[ext] || "text/plain";

	fs.readFile(filepath, (err, data) => {
		if (err) {
			response.writeHead(500);
			return response.end("Error loading " + pathname);
		}

		response.writeHead(200, { "Content-Type" : contentType });
		response.end(data);
	});
}

// socket.io
var io = require("socket.io").listen(server);

io.sockets.on("connection", (socket) => {
	console.log("We have a new client: " + socket.id);

	// When another user is drawing, receive data
	socket.on("pencildraw", (data) => {
		socket.broadcast.emit("draw", data);
	});

	socket.on("disconnect", () => {
		console.log("Client has disconnected");
	});
});