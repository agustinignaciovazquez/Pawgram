'use strict';

/**
 * @ngdoc directive
 * @name pawgramApp.directive:postItem
 * @description
 * # postItem
 */
 define(['pawgramApp', 'services/authService', 'services/snackbarService', 'services/modalService', 'controllers/DeleteModalCtrl'], function(pawgramApp) {
    pawgramApp.directive('postItem', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/postItem.html',
      scope: {post: '=', hideCategory: '=', hideDelete: '=', onSubscribed: '&', onAdd: '&', onDelete: '&', borderHover: '=', order: '=', orderBy: '='},
      controller: ['$scope', '$location', '$route', 'authService', 'restService', 'modalService', 'snackbarService',
      function($scope, $location, $route, authService, restService, modalService, snackbarService, defaultSortCriteria) {
        var post = $scope.post;
				$scope.loggedUser = authService.loggedUser;

        $scope.directToCategory = function() {
					var orderBy = $scope.orderBy || defaultSortCriteria.orderBy;
					var order = $scope.order || defaultSortCriteria.order;
					$location.url('/?category=' + post.category + '&orderBy=' + orderBy + '&order=' + order);
				};

				$scope.directToPost = function() {
					$location.url('post/' + post.id);
				};

				$scope.isLoggedIn = authService.isLoggedIn();

        $scope.toggleSubscribe = function() {
					if ($scope.isLoggedIn) {
						$scope.post.subscribed = !$scope.post.subscribed;
						$scope.onSubscribed({subscribed: $scope.post.subscribed});

						if ($scope.post.subscribed) {
							restService
								.subscribePost(post.id)
								.catch(function() {revertSubscription()});
						}
						else {
							restService
								.unsubscribePost(post.id)
								.catch(function() {revertSubscription()});
						}
					}
				};

				function revertSubscription() {
					$scope.post.subscribed = !$scope.post.subscribed;
					snackbarService.showNoConnection();
				}

        $scope.deleteModal = function() {
					var modal = modalService.deleteModal($scope.post);
					modal.result.then(function(isDeleted) {
						if (isDeleted)
							$scope.onDelete();
					});
				};

      }]
    };
  });
 });
