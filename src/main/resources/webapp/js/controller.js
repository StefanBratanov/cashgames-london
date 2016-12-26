angular.module('poker-games', [])
.controller('pokerGamesController', function($scope, $http) {
    $http.get('/api/poker-games').
        then(function(response) {
            $scope.details = response.data;
        });
});