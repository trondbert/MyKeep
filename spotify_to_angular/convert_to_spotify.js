app = angular.module('angularapp', ['ngRoute']);

app.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
    addRoute($routeProvider, '/play', 'PlayCtrl', 'play.html');
    $routeProvider.otherwise({redirectTo: '/'});

    $httpProvider.defaults.useXDomain = true;
}]);

app.controller('PlayCtrl', playController);

function addRoute(provider, url, controller, templateUrl) {
    provider.when(url, {controller: controller, templateUrl: templateUrl});
}



function search(what, forWhat) {
    return {
        what: what,
        forWhat : forWhat,
        replaceWith : function(replacement) {
            var regex = new RegExp(forWhat, "g");
            return what.replace(regex, replacement);
        }
    };
}

function bestMatch(tracks) {
    var earliestRelease = 3000, theBestMatch = null;
    tracks.forEach(function(it) {
        if (earliestRelease > parseInt(it.album.released)) {
            earliestRelease = parseInt(it.album.released);
            theBestMatch = it;
        }
    });
    return theBestMatch;
}

playController = ['$scope', '$location', 'fbUrls', '$http',
    function ($scope, $location, fbUrls, $http) {

        $scope.readWimpList = function() {
            var currSong = null;
            $scope.songs = [];
            var lines = $scope.wimpList.split("\n");
            lines.forEach(function (line) {
                try {
                    if (/<item>/.test(line)) {
                        currSong = {};
                        $scope.songs.push(currSong);
                    }
                    if (currSong && /<title>/.test(line)) {
                        var temptitle = search(line, "<title>(.*?)</title>").replaceWith("$1");
                        currSong.title = search(temptitle, "\\(.*?\\)").replaceWith("");
                    }
                    else if ( currSong && /<artist>/.test(line)) {
                        currSong.artist = search(line, "<artist>(.*?)</artist>").replaceWith("$1");
                    }
                }
                catch(err) { console.log("error on line: " + line); throw (err); }
            });
        };

        $scope.convert = function () {
            if (!$scope.songs) { $scope.readWimpList(); }

            $scope.songs.forEach(function(song) {
                song.searchTerms = song.searchTerms || (song.artist + " " + song.title);
                var url = "http://ws.spotify.com/search/1/track.json?q=" + song.searchTerms;
                $http.get(url).
                    success(function (data, status, headers, config) {
                        if (data.tracks && data.tracks.length > 0) {
                            data.tracks.forEach(function(t) { 
                                t.label = t.artists[0].name.substring(0,10) +
                                    ": " + t.name.substring(0,20) + " - " + t.album.name.substring(0,20)
                                    + " (" + t.album.released + ")";
                            });
                            song.tracks = data.tracks;
                            song.chosenTrack = bestMatch(data.tracks);
                        } else {
                            song.tracks = null;
                        }
                    }).
                    error(function (data, status, headers, config) {
                        console.log("Error " + song.title + ": " + data);
                    });
            });
        };

        $scope.allSpotifyHrefs = function() {
            var allSongs = "";
            $scope.songs && $scope.songs.forEach(function(pyesna) {
                if (pyesna.chosenTrack)
                    allSongs += pyesna.chosenTrack.href + "\n";
            });
            return allSongs;
        };

        $scope.wimpList = wimpData;

        window.scope = $scope;
    }
];


