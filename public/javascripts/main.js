
var app = angular.module("app", ["ngResource", "ngRoute"])
	.constant("apiUrl", "http://localhost:9000/api")
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/views/main",
			controller: "ListCtrl"
		}).when("/create", {
			templateUrl: "/views/detail",
			controller: "CreateCtrl"
	    }).when("/edit/:id", {
			templateUrl: "/views/detail",
			controller: "EditCtrl"
	    }).otherwise({
			redirectTo: "/"
		});
	}
	]).config([
	"$locationProvider", function($locationProvider) {
		return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and history API
	}
]);

// the global controller
app.controller("AppCtrl", ["$scope", "$location", function($scope, $location) {
	// the very sweet go function is inherited by all other controllers
	$scope.go = function (path) {
		$location.path(path);
	};
}]);

// MINE:  the list controller
app.controller("ListCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
	var Celebrities = $resource(apiUrl + "/donelist/2014093"); // a RESTful-capable resource object
	$scope.celebrities = Celebrities.query(); // for the list of celebrities in public/html/main.html
	var date = new Date();
	
	// MINE: add()
	$scope.add = function() {
		var create = $resource(apiUrl + "/donelist/new"); // a RESTful-capable resource object
		create.save({'donetext' : $scope.donetext}); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { alert('ooops TODO'); }); // go back to public/html/main.html
	};

	$scope.datestring = function() {
		var month = date.getMonth() + 1;
		var day = date.getDate();
		return date.getFullYear() + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "") + day;
	}
	
	$scope.increasedate = function() {
		var result = new Date(date);
	    result.setDate(date.getDate() + 1);
	    date = result;
	    //alert($scope.datestring());
	}

	$scope.decreasedate = function() {
		var result = new Date(date);
	    result.setDate(date.getDate() - 1);
	    date = result;
	    //var millisecondOffset = 24 * 60 * 60 * 1000;
	    //date.setTime(date.getTime() - millisecondOffset);
	    //alert($scope.datestring());
	}

}]);

// the create controller
app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
	// to save a celebrity
	$scope.save = function() {
		var CreateCelebrity = $resource(apiUrl + "/donelist/new"); // a RESTful-capable resource object
		CreateCelebrity.save($scope.celebrity); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);

// the edit controller
app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
	var ShowCelebrity = $resource(apiUrl + "/celebrities/:id", {id:"@id"}); // a RESTful-capable resource object
	if ($routeParams.id) {
		// retrieve the corresponding celebrity from the database
		// $scope.celebrity.id.$oid is now populated so the Delete button will appear in the detailForm in public/html/detail.html
		$scope.celebrity = ShowCelebrity.get({id: $routeParams.id});
		$scope.dbContent = ShowCelebrity.get({id: $routeParams.id}); // this is used in the noChange function
	}
	
	// decide whether to enable or not the button Save in the detailForm in public/html/detail.html 
	$scope.noChange = function() {
		return angular.equals($scope.celebrity, $scope.dbContent);
	};

	// to update a celebrity
	$scope.save = function() {
		var UpdateCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		UpdateCelebrity.save($scope.celebrity); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
	
	// to delete a celebrity
	$scope.delete = function() {
		var DeleteCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		DeleteCelebrity.delete();
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);