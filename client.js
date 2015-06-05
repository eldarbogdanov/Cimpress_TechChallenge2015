/*
Usage: node client.js <jar file with solution> [-v] [-c]
*/

http = require('http');
exec = require('child_process').exec;
var baseUrl = 'techchallenge.cimpress.com';
var iterations = 100;
var env = 'trial';
var apiKey = '971fbdd4618741b4aff4bb4f92afa78c';
console.log(process.argv);
var jar = process.argv[2];
var verbose = process.argv.indexOf("-v") != -1;
var compare = process.argv.indexOf("-c") != -1;

// aggregators for comparison
var rating = 0.0;
var size = 0;
var bruteWins = 0;

var loopFunction = function(iterationsLeft) {
    solveOneTestcase(iterations - iterationsLeft, function afterSolve() {
        if (iterationsLeft > 1)
            loopFunction(iterationsLeft - 1);
        if (compare) {
            // write to file the average rating, number of puzzles and ratio of brute wins
            fs = require('fs');
            var iterationsSoFar = (iterations - iterationsLeft + 1)
            var data = "Average rating: " + rating / iterationsSoFar + "\nPuzzles solved: " + iterationsSoFar +
                       "\nBruteforce won: " + bruteWins + "\nAverage board size: " + size / iterationsSoFar;
            fs.writeFile(jar + '.txt', data, function(err) {});
        }
    });
}
var solveOneTestcase = function(it, inTheEnd) {
    console.log('Processing puzzle #' + (it + 1));
    
    var stringify = function(boolArray) {
        var ret = '';
        for(var i = 0; i < boolArray.length; i++) {
            ret += boolArray[i] ? 'X' : '.';
        }
        return ret;
    }

    var printPuzzleRepresentation = function(puzzle) {
        for(var row = 0; row < puzzle.length; row++) {
            console.log(stringify(puzzle[row]));
        }
    }

    var printAnswerRepresentation = function(gridArr, squares) {
        for(var k = 0; k < squares.length; k++) {
            var squareId = String.fromCharCode(Math.floor(Math.random() * 26) + 65);
            for(var i = squares[k].Y; i < squares[k].Y + squares[k].Size; i++)
                for(var j = squares[k].X; j < squares[k].X + squares[k].Size; j++) {
                    gridArr[i] = gridArr[i].substr(0, j) + squareId + gridArr[i].substr(j + 1);
             }
        }
        for(var i = 0; i < gridArr.length; i++) {
            console.log(gridArr[i]);
        }
    }

    var solve = function(httpPuzzle) {
        
        var puzzle = '';
        httpPuzzle.on('data', function(body) {
            puzzle += body.toString();
        });
        
        httpPuzzle.on('end', function() {
            var json = JSON.parse(puzzle);
            if (verbose) {
                console.log('Puzzle:');
                printPuzzleRepresentation(json.puzzle);
            }

            var grid = '';
            var gridArr = []
            for(var row = 0; row < json.puzzle.length; row++) {
                var rowStr = stringify(json.puzzle[row]);
                grid += rowStr;
                gridArr.push(rowStr);
            }

            exec('java -jar ' + jar + ' ' + json.height + ' ' + json.width + ' ' + grid, function afterSolving(error, stdout, stderr) {
                if (error != null) {
                    console.log(error);
                    return ;
                }

                if (verbose) {
                    console.log('Answer:');
                    printAnswerRepresentation(gridArr, JSON.parse(stdout.toString()).squares);
                }

                if (compare) {
                    var brute = parseInt(stderr.toString().split("\n")[0]);
                    var contour = parseInt(stderr.toString().split("\n")[1]);
                    if (brute < contour)
                        ++bruteWins;
                    rating += brute / (brute + contour);
                    size += gridArr.length * gridArr[0].length;
                    console.log(brute + " " + contour + " " + gridArr.length + " " + gridArr[0].length + " " + rating / (it + 1));
                }

                var postOptions = {
                    host: baseUrl,
                    path: '/' + apiKey + '/' + env + '/solution',
                    method: 'POST'
                };

                var postData = JSON.parse(stdout.toString());
                postData.id = json.id;

                var post = http.request(postOptions, function(res) {
                    res.on('data', function(data) {
                        console.log(data.toString());
                        inTheEnd();
                    });
                });
                post.write(JSON.stringify(postData));
                post.end();
            });

        });
    }

    var getOptions = {
        host: baseUrl,
        path: '/' + apiKey + '/' + env + '/puzzle',
        method: 'GET'
    };

    http.request(getOptions, solve).end();
}

loopFunction(iterations);
