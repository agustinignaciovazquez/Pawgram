'use strict';
define(['pawgramApp', 'services/restService', 'services/snackbarService'], function(pawgramApp) {
	pawgramApp.controller('DeleteModalCtrl', ['$scope', '$uibModalInstance', 'restService', 'snackbarService', 'post', function($scope, $uibModalInstance, restService, snackbarService, post) {

		$scope.post = post;

		$scope.delete = function() {
			$scope.deleting = true;
			restService.deletePost(post.id)
			.then(function() {
				$scope.deleting = false;
				snackbarService.showSnackbar('postDeleted');
				$uibModalInstance.close(true);
			})
			.catch(function() {
				$scope.deleting = false;
				snackbarService.showNoConnection();
			});
		};

		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};

	}]);

});
