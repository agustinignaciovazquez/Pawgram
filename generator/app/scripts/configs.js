'use strict';
let configs = {};
configs.translate = function($translateProvider){
     var userLang, listOfSupportedLanguages = ['en', 'es'];
      // to avoid being called in non browser environments
      if (typeof navigator === 'object') {
        userLang = navigator.language || navigator.userLanguage;
        userLang = userLang.split('-')[0];
      }

      // Set English as default language
      if (userLang === undefined || listOfSupportedLanguages.indexOf(userLang) < 0) {
        userLang = 'en';
      }
      
      $translateProvider.useStaticFilesLoader({
          prefix: 'i18n/locale-',
          suffix: '.json'
      });

      $translateProvider.preferredLanguage(userLang);
      $translateProvider.forceAsyncReload(true);
      $translateProvider.useSanitizeValueStrategy('sanitize');
};  

configs.essential = function($qProvider, $locationProvider, cfpLoadingBarProvider){
      $qProvider.errorOnUnhandledRejections(false);

      $locationProvider.hashPrefix('');

      cfpLoadingBarProvider.latencyThreshold = 100;
      cfpLoadingBarProvider.includeSpinner = false;
};

configs.run = function($rootScope, $location,authService) {
      $rootScope.isViewLoading = false;

      $rootScope.$on('$routeChangeStart', function() {
        $rootScope.isViewLoading = true;

         if (!authService.isLoggedIn()) {
          if($location.path().toLowerCase() !== '/register' && $location.path().toLowerCase() !== '/login'){
            console.log('DENY : Redirecting to Login');
            authService.saveAttemptUrl();
            $location.path('/login');
          }
        }

      });

      $rootScope.$on('$routeChangeSuccess', function() {
        $rootScope.isViewLoading = false;

        document.body.scrollTop = document.documentElement.scrollTop = 0;
      });

      $rootScope.$on('$routeChangeError', function() {
        $rootScope.isViewLoading = false;

        $location.path('/404');
      });
 };