'use strict';
define(['pawgramApp', 'angular-slick-carousel', 'directives/postItem', 'services/restService', 'services/modalService', 'services/titleService'], function(pawgramApp) {

	pawgramApp.controller('postCtrl', ['authService', '$sce', 'titleService', 'restService', 'modalService', 'snackbarService', '$scope', '$location', 'post', function(auth, $sce, titleService, restService, modalService, snackbarService, $scope, $location, post) {

		titleService.setTitle(post.title.charAt(0).toUpperCase() + post.title.slice(1));

		$scope.post = post;
		$scope.description = post.description;
		$scope.creator = post.creator;
		$scope.image_urls = post.imageURLs;
		$scope.comments = post.comments;

		$scope.loggedUser = auth.getLoggedUser();
		$scope.isLoggedIn = auth.isLoggedIn();

		$scope.parentCommentForm = {};

		$scope.childCommentForm = [];

		$scope.parentCommentLengthError = false;

		$scope.childCommentLengthError = [];

    $scope.slickConfig = {
    		  draggable: true,
    		  enabled: true,
    		  dots: true,
    		  infinite: false,
    		  slidesToScroll : 1,
    		  slidesToShow: 1
  		};

		$.fn.goTo = function() {
			var offset = 100;
			$('html, body').animate({
				scrollTop: $(this).offset().top - offset + 'px'
			}, 'fast');
			return this; // for chaining...
		};

		function validChildCommentSubmit(index) {
			return $scope.childCommentForm[index].text.length < 512;
		};

		function validParentCommentSubmit(index) {
			return $scope.parentCommentForm.text.length < 512;
		};

		function childCommentSubmitNoError(parentCommentId, index) {

			$scope.comments[index].children.push(jsonFromComment($scope.childCommentForm[index]));

			restService.commentParentPost($scope.post.id, $scope.childCommentForm[index].text, parentCommentId).
			then(function(data) {
				$scope.childCommentLengthError[index] = false;
				$scope.childCommentForm[index].text = '';

				var len = $scope.comments[index].children.length -1;

				$scope.comments[index].children[len].id = data.id;
				$scope.comments[index].children[len].date = data.date;
				$scope.comments[index].children[len].parent_id = data.parent_id;

			})
			.catch(function() {
				snackbarService.showNoConnection();
				$scope.comments[index].children.pop();
			});	;

		};

		function parentCommentSubmitNoError() {

			$scope.comments.push(jsonFromComment($scope.parentCommentForm));

			//scroll to bottom of page
			angular.element(document.getElementsByClassName('footer')).goTo();

			restService.commentPost($scope.post.id, $scope.parentCommentForm.text).
			then(function(data) {
				$scope.parentCommentLengthError = false;
				$scope.parentCommentForm.text = '';
				$scope.comments[$scope.comments.length - 1].id = data.id;
				$scope.comments[$scope.comments.length - 1].date = data.date;

			})
			.catch(function() {
				snackbarService.showNoConnection();
				$scope.comments.pop();
			});
		};

		function jsonFromComment(form) {

			var newParentComment = {};

			newParentComment.author = $scope.loggedUser;
			newParentComment.children = [];
			newParentComment.content = form.text;

			return newParentComment;
		}

		$scope.showReplyForm = function(target) {
			// Hide all other open comment forms.
			if ($scope.isLoggedIn) {
				$('.reply-comment').hide();

				var replyform = angular.element(target).closest('.comment-and-replies').find('.reply-comment');
				replyform.css('display', 'inline');

				replyform.goTo();
			}
		};

		$scope.parentCommentSubmit = function() {

			if (validParentCommentSubmit())
				parentCommentSubmitNoError();
			else
				$scope.parentCommentLengthError = true;
		};

		$scope.childCommentSubmit = function(parentCommentId, index) {

			if (validChildCommentSubmit(index))
				childCommentSubmitNoError(parentCommentId, index);
			else
				$scope.childCommentLengthError[index] = true;
		};

		$scope.isOwnerLogged = function() {
			return $scope.isLoggedIn ? $scope.loggedUser.id === $scope.creator.id : false;
		};

		$scope.directToCreatorProfile = function() {
			$location.url('profile/' + $scope.creator.id);
		};

		$scope.directToLoggedProfile = function() {
			$location.url('profile/' + $scope.loggedUser.id);
		};

		$scope.directToAuthorProfile = function(id) {
			$location.url('profile/' + id);
		};

		$scope.directToHome = function() {
			$location.url('/');
		};

	}]);
});
