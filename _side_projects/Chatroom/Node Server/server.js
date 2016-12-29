var http = require('http'),
    fs = require('fs');

var server = http.createServer(function(req, res) {
    fs.createReadStream(__dirname + '/index.html').pipe(res);
});

require('socket.io')(server)
    .use(require('chat-anarchy-server')());

server.listen(3001, '10.0.0.54', function() {
    console.log("Chat anarchy is listening on port %d", server.address().port);
});
