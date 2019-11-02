/*

	request_handler.js

	@author: cheng jiang
	@date: November 2019

	Refactored version

*/

// import modules
var log = require("./log.js").log
	, path = require('path')
	, url = require('url')
	, fs = require("fs")
	, crypto = require("crypto")
	, nodestatic = require("node-static")
	, createSVG = require("./createSVG.js")
	, handlebars = require("handlebars");

var WEBROOT = path.join(__dirname, "../client-data");


/**
 * Associations from language to translation dictionnaries
 * @const
 * @type {object}
 */
var TRANSLATIONS = JSON.parse(fs.readFileSync(path.join(__dirname, "translations.json")));

var CSP = "default-src 'self'; style-src 'self' 'unsafe-inline'; connect-src 'self' ws: wss:";

var fileserver = new nodestatic.Server(WEBROOT, {
	"headers": {
		"X-UA-Compatible": "IE=Edge",
		"Content-Security-Policy": CSP,
	}
});

var BOARD_HTML_TEMPLATE = handlebars.compile(
	fs.readFileSync(WEBROOT + '/board.html', { encoding: 'utf8' })
);
handlebars.registerHelper({
	translate: function (translations, str) {
		return translations[str] || str;
	},
	json: JSON.stringify.bind(JSON)
});


// wrapper
function handler(request, response) {
	try {
		var parsedUrl = url.parse(request.url, true);
		var parts = parsedUrl.pathname.split('/');

		if (parts[0] === '') parts.shift();

		if (parts[0] === "boards") {
			// "boards" refers to the root directory
			if (parts.length === 1 && parsedUrl.query.board) {
				// '/boards?board=...' This allows html forms to point to boards

				logRequest(request);
				redirect(request, response, parsedUrl, parts);
			}
			else if (parts.length === 2 && request.url.indexOf('.') === -1) {
				// If there is no dot and no directory, parts[1] is the board name
				
				logRequest(request);
				serveWorkspace(request, response, parsedUrl, parts);
			}
			else { 
				// Else, it's a resource
				logRequest(request);
				serveError(request, response);
			}
		}
		else {
			logRequest(request);
			serveError(request, response);
		}
	} catch (err) {
		console.trace(err);
		response.writeHead(500, { 'Content-Type': 'text/plain' });
		response.end(err.toString());
	}
}



// helper functions
function serveError(request, response) {
	fileserver.serve(request, response, function (err, res) {
		if (err) {
			console.warn("Error serving '" + request.url + "' : " + err.status + " " + err.message);
			fileserver.serveFile('error.html', err.status, {}, request, response);
		}
	});
}

function logRequest(request) {
	log('connection', {
		ip: request.connection.remoteAddress,
		original_ip: request.headers['x-forwarded-for'] || request.headers['forwarded'],
		user_agent: request.headers['user-agent'],
		referer: request.headers['referer'],
		language: request.headers['accept-language'],
		url: request.url,
	});
}

function baseUrl(req) {
	var proto = req.headers['X-Forwarded-Proto'] || (req.connection.encrypted ? 'https' : 'http');
	var host = req.headers['X-Forwarded-Host'] || req.headers.host;
	return proto + '://' + host;
}

function redirect(request, response, url) {
	var headers = { Location: 'boards/' + encodeURIComponent(url.query.board) };
	response.writeHead(301, headers);
	response.end();
}

function serveWorkspace(request, response, url, parts) {
	var lang = (
		url.query.lang ||
		request.headers['accept-language'] ||
		''
	).slice(0, 2);
	var board = decodeURIComponent(parts[1]);
	var body = BOARD_HTML_TEMPLATE({
		board: board,
		boardUriComponent: parts[1],
		baseUrl: baseUrl(request),
		languages: Object.keys(TRANSLATIONS).concat("en"),
		language: lang in TRANSLATIONS ? lang : "en",
		translations: TRANSLATIONS[lang] || {}
	});
	var headers = {
		'Content-Length': Buffer.byteLength(body),
		'Content-Type': 'text/html'
	};
	response.writeHead(200, headers);
	response.end(body);
}

module.exports = handler;