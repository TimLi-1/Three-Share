/*
	server.js

	@author: cheng jiang
	@date: november 2019

	refactored version

*/

// import modules
var sockets = require('./sockets.js')
	, log = require("./log.js").log
	, handler = require('./request_handler.js')
	, http = require('http');


// initialize server and sockets
var app = http.createServer(handler);
var io = sockets.start(app);

// define port which the server will listen
var PORT = parseInt(process.env['PORT']) || 8080;

// start server
app.listen(PORT);
log("Server started", { port: PORT });