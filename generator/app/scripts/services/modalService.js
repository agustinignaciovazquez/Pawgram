'use strict';
define(['pawgramApp', 'services/restService', 'services/authService', 'controllers/DeleteModalCtrl'], function(pawgramApp) {
	pawgramApp.service('modalService', ['$uibModal', 'restService', 'authService', function($uibModal, restService, authService) {

		this.deleteModal = function(post) {
			return $uibModal.open({
				templateUrl: 'views/modals/deleteModal.html',
				controller: 'DeleteModalCtrl',
				size: 'sm',
				resolve: {
					post: function() {
						return post;
					}
				}
			});
		};


	}]);
});
