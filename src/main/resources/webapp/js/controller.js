angular.module('poker-games', [])
    .controller('pokerGamesController', function($scope, $http) {
        $http.get('/api/poker-venues').
        then(function(response) {
            var jsonData = response.data;
            var timeFormat = "YYYY-MM-DD HH:mm:ss"
            var now = moment().tz("Europe/London").format(timeFormat)
            $scope.details = jsonData.map(function(detail) {
                if (detail.latestUpdateTime !== null) {
                    var updatedAt = moment(detail.latestUpdateTime, timeFormat);
                    var hours = Math.abs(updatedAt.diff(now, 'hours'))
                    var minutes = Math.abs(updatedAt.diff(now, 'minutes'))
                    detail.latestUpdateTime = readableLastUpdated(hours, minutes)
                }
                var gameDetails = detail.pokerGameDetails;
                if (gameDetails.length > 0) {
                    var firstGameDetail = gameDetails[0];
                    detail.twitterUrl = firstGameDetail.twitterUrl;
                    var updatedAt = moment(firstGameDetail.updatedAt, timeFormat);
                    var hours = Math.abs(updatedAt.diff(now, 'hours'))
                    var minutes = Math.abs(updatedAt.diff(now, 'minutes'))
                    detail.lastUpdated = readableLastUpdated(hours, minutes)
                    detail.hasGameDetails = true;
                } else {
                    detail.hasGameDetails = false;
                }
                return detail;
            });
        });
    });

function readableLastUpdated(h, m) {
    var output = '';
    if (h > 0) {
        output = output.concat(h + ' h and ')
    }
    var mins;
    if (m > 59) {
        mins = m - (h * 60)
    } else {
        mins = m;
    }
    return output.concat(mins + ' mins ago');
}