/*
	handler.js

	@author: cheng jiang
	@date: november 2019

*/

var url = require("url");
var path = require("path");
var fs = require("fs");

var root = path.join(__dirname, '..', 'client');
var entrypath = path.join(root, "workspace.html");

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

module.exports = handler;