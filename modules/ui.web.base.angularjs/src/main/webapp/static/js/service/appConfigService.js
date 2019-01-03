'use strict';

App.service('AppConfigService', function($http) {
    var appConfig = null;

	var promiseConfig = $http.get('userConfig/').success(function (data) {
		appConfig = data;
    });
	
    return {
      promise:promiseConfig,
      setData: function (data) {
    	  appConfig = data;
      },
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