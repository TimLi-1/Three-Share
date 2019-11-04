/*
	server.js

	@author: cheng jiang
	@date: november 2019

*/

// import modules
var http = require("http");
var sockets = require("./sockets.js");
var handler = require("./handler.js");

// http
var server = http.createServer(handler);
server.listen(8080);
console.log("Server started, listening on port 8080");
sockets.listen(server);