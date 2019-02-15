'use strict';

App.service('AppConfigService', function($http) {
    var appConfig = null;

    // FIXME: replace concrete userId
	var promiseConfig = $http.get('userConfig/userId/1').success(function (data) {
		appConfig = data;
    });
	
    return {
      promise:promiseConfig,
      getApplicationConfig: function () {
          return appConfig;
      }
    };
});


/**
 * Definition for routeProvider: Before loading MainController and showing grid load all user 
 * specific server based properties for configuring client based constants
 *
 */
App.config(function($routeProvider){
  $routeProvider
    .when('/',{
		controller:'MainController',
		templateUrl:'../static/includes/mwBody_include.html',
		resolve:{
			'MyServiceData':function(AppConfigService){
				return AppConfigService.promise;
			}
		}})
  });