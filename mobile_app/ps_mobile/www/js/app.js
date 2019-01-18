
mobile_app = angular.module('starter', ['ionic'])

.run(function($ionicPlatform,$state,$ionicModal,$rootScope,$ionicPopup,$ionicHistory,$ionicViewService) {

        $ionicPlatform.registerBackButtonAction(function () {
            if ($rootScope.backButtonPressedOnceToExit) {
                ionic.Platform.exitApp();
            }
            else if (($state.current.name == "app.visitorConcept") || ($state.current.name == "tab.login") || ($state.current.name == "appApplicants.applicantsHome") ) {
                $rootScope.backButtonPressedOnceToExit = true;
                window.plugins.toast.showShortCenter(
                    "Press back button again to exit",function(a){},function(b){}
                );
                setTimeout(function(){
                    $rootScope.backButtonPressedOnceToExit = false;
                },2000);

            }else if (($state.current.name == "tab.authentication") || ($state.current.name == "tab.applicants") || ($state.current.name == "tab.agent")) {
                $ionicHistory.goBack();

            }
            else if (($state.current.name == "app.visitorConcept") || ($state.current.name == "app.visitorBenefits") || ($state.current.name == "app.visitorApply")) {
                $ionicViewService.nextViewOptions({
                    disableBack: true
                });
                $state.go("app.visitorHome");
            }
            else if (($state.current.name == "appApplicants.changePass") || ($state.current.name == "appApplicants.sendQuery") || ($state.current.name == "appApplicants.referFriend")) {
                $ionicViewService.nextViewOptions({
                    disableBack: true
                });
                $state.go("appApplicants.applicantsHome");
            }
        }, 100);


  $ionicPlatform.ready(function() {

    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      StatusBar.styleLightContent();
    }
  });
})

.run(function($rootScope) {
  $rootScope.appconfig = {
   // serverUrl: 'http://192.168.11.108:9000',
    serverUrl: 'http://192.168.0.77:9000',
  };
})

.run(function($ionicPlatform, $ionicPopup) {
    $ionicPlatform.ready(function() {
        if(window.Connection) {
            if(navigator.connection.type == Connection.NONE) {
                console.log("No Internet connection")
                $ionicPopup.confirm({
                    title: "Internet Disconnected",
                    content: "The internet is disconnected on your device."
                })
                    .then(function(result) {
                        if(!result) {
                            ionic.Platform.exitApp();
                        }
                    });
            }
        }
    });
})


.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  $stateProvider
      // setup an abstract state for the tabs directive
      .state('tab', {
          url: '/tab',
          abstract: true,
          templateUrl: 'templates/tabs.html',
          controller: 'tabCtrl'
      })
      // Each tab has its own nav history stack:

      .state('tab.login', {
          url: '/login',
          views: {
              'tab-login': {
                  templateUrl: 'templates/registraionVisitor.html',
                  controller: 'registrationVisitorCtrl'
              }
          }
      })
      .state('tab.authentication', {
          url: '/authentication',
          views: {
              'tab-authentication': {
                  templateUrl: 'templates/authentication.html',
                  controller: 'authentication'
              }
          }
      })
      .state('tab.applicants', {
          url: '/applicants',
          views: {
              'tab-applicants': {
                  templateUrl: 'templates/applicantsLogin.html',
                  controller: 'applicantsLoginCtrl'
              }
          }
      })
      .state('tab.agent', {
          url: '/agent',
          views: {
              'tab-agent': {
                  templateUrl: 'templates/agentLogin.html',
                  controller: 'agentLoginCtrl'
              }
          }
      })


      .state('app', {
          url: '/app',
          abstract: true,
          templateUrl: 'templates/menu.html',
          controller: 'AppCtrl'
      })

      .state('app.visitorHome', {
          url: '/visitor-home',
          cache:false,
          views: {
              'menuContent': {
                  templateUrl: 'templates/visitor-home.html',
                  controller: 'VisitorHomeCtrl'
              }
          }
      })
      .state('app.visitorConcept', {
          url: '/visitor-concept',
          cache:false,
          views: {
              'menuContent': {
                  templateUrl: 'templates/visitor-concept.html',
                  controller: 'VisitorConceptCtrl'

              }
          }
      })
      .state('app.visitorBenefits', {
          url: '/visitor-benefits',
          views: {
              'menuContent': {
                  templateUrl: 'templates/visitor-benefits.html',
                  controller: 'VisitorBenefitsCtrl'

              }
          }
      })
      .state('app.visitorApply', {
          url: '/visitor-apply',
          views: {
              'menuContent': {
                  templateUrl: 'templates/visitor-apply.html',
                  controller: 'VisitorApplyCtrl'

              }
          }
      })

      .state('app.visitorPayment', {
          url: '/visitor-payment',
          views: {
              'menuContent': {
                  templateUrl: 'templates/payment.html',
                  controller: 'VisitorApplyCtrl'

              }
          }
      })

      .state('appApplicants', {
          url: '/appApplicants',
          abstract: true,
          templateUrl: 'templates/menuApplicants.html',
          controller: 'AppApplicantsCtrl'
      })
      .state('appApplicants.applicantsHome', {
          url: '/applicants-home',
          views: {
              'menuContent': {
                  templateUrl: 'templates/applicants-home.html',
                  controller: 'applicantsHomeCtrl'

              }
          }
      })
      .state('appApplicants.changePass', {
          url: '/visitor-changePass',
          views: {
              'menuContent': {
                  templateUrl: 'templates/applicants-changePass.html',
                  controller: 'applicantsChangePassCtrl'

              }
          }
      })
      .state('appApplicants.sendQuery', {
          url: '/visitor-sendQuery',
          views: {
              'menuContent': {
                  templateUrl: 'templates/applicants-sendQuery.html',
                  controller: 'applicantsSendQueryCtrl'

              }
          }
      })
      .state('appApplicants.referFriend', {
          url: '/visitor-referFriend',
          views: {
              'menuContent': {
                  templateUrl: 'templates/applicants-referFriend.html',
                  controller: 'applicantsReferFriendCtrl'

              }
          }
      });

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/tab/login');


});


