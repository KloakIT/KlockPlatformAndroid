
//process.chdir ( argv[2]);
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const localWebServer_1 = require("./app/localWebServer");
new localWebServer_1.default( 3000,'');
/*

var http = require('http');
new local.kkkk ()
var versions_server = http.createServer( (request, response) => {
  response.end('Versions: ' + JSON.stringify(process.versions));
});
versions_server.listen(3000);
*/