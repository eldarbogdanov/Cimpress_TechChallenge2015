http = require('http');
exec = require('child_process').exec;
var baseUrl = 'techchallenge.cimpress.com';
var iterations = 1;
var env = 'trial';
var apiKey = 'ac523c3ff7764703afb180b09f06279b';
for(var it = 0; it < iterations; it++) {
    console.log('Downloading puzzle #' + it);
    var options = {
        host: baseUrl,
        path: '/' + apiKey + '/' + env + '/puzzle',
        method: 'GET'
    };
    var solve = function(res) {
        var puzzle = '';
        res.on('data', function(body) {
            puzzle += body.toString();
        });
        res.on('end', function() {
            var json = JSON.parse(puzzle);
            console.log('Puzzle:');
            console.log(puzzle);

            var stringify = function(boolArray) {
                var ret = '';
                for(var i = 0; i < boolArray.length; i++) {
                    ret += boolArray[i] ? 'X' : '.';
                }
                return ret;
            }

            var grid = '';
            var gridArr = []
            for(var row = 0; row < json.puzzle.length; row++) {
                var rowStr = stringify(json.puzzle[row]);
                console.log(rowStr);
                grid += rowStr;
                gridArr.push(rowStr);
            }

            exec('java -jar cimpress.jar ' + json.height + ' ' + json.width + ' ' + grid + ' ' + json.id, function afterSolving(error, stdout, stderr) {
                if (error != null) {
                    console.log(error);
                    return ;
                }
                console.log('Answer:');
                var squares = JSON.parse(stdout.toString()).squares;
                console.log(squares);
                for(var k = 0; k < squares.length; k++) {
                    var squareId = String.fromCharCode(Math.floor(Math.random() * 26) + 65);
                    for(var i = squares[k].Y; i < squares[k].Y + squares[k].Size; i++)
                        for(var j = squares[k].X; j < squares[k].X + squares[k].Size; j++) {
                            gridArr[i] = gridArr[i].substr(0, j) + squareId + gridArr[i].substr(j + 1);
                        }
                }
                for(var i = 0; i < gridArr.length; i++)
                    console.log(gridArr[i]);
            });

        });
    }
    http.request(options, solve).end();
}
