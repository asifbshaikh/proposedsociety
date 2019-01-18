mobile_app.controller('VisitorApplyCtrl', function($window,$scope,$http,$rootScope,$state,$ionicSideMenuDelegate,$filter) {
    console.log("in visitor apply");
    $scope.name = "test test";
    $scope.number = "555"
    $scope.master = {};
    $scope.reset = function() {
        window.location.reload();
        $scope.user = angular.copy($scope.master);
    };

    $scope.user={
        applicant:{}, requirement:{} , locationsHiddenField:[{}]
    };

    $scope.user.registrationBy="self";
    $scope.user.shortFormApplicantIncomePer="monthly";
    $scope.user.shortFormCoApplicantIncomePer="monthly";
    $scope.user.applicant.title="Mr";
    $scope.user.requirement.requirementSubType="FlatOrTerraceFlat";
    $scope.user.requirement.gardenAreaRequired="Required";

    $scope.paymentForm= function (paymentForm) {
         console.log("==================",$rootScope.appconfig.serverUrl);
            var config = {
               url: $rootScope.appconfig.serverUrl + "/test",
               method: "POST",
               headers: {
                   'Content-type': 'application/json',
                   'apiKey': window.localStorage['apiKey']
               }
            }
            $http(config).success(function () {
                           console.log("Successfully submitted form: " );
                           alert("Successfully Submitted");
                       }).error(function () {
                          console.log("Error");
                           alert("Error");
                       });

    };

//    updateDefaultBatchShip: function (customer) {
//            var deferred = $q.defer();
//            var self = this;
//            $http({
//                method: 'PUT',
//                url: '/customer/' + customer.id + '/batch_ship/' + customer.default_batch_address_id
//            }).then(function (customerData) {
//                var customerBlock = new Customer(customerData.data);
//                var customerBlock = self._storeInstance(customerData.data.id,customerData.data)
//                return deferred.resolve(customerBlock);
//            },function  (error) {
//                deferred.reject(error);
//            });
//            return deferred.promise;
//        }

    $scope.signupForm= function (form) {

    console.log("form == ", form);
       console.log("user =======",$scope.user);
        if (form.$valid) {
            console.log("API KEY: " + window.localStorage['apiKey'])
            var config = {
//                url: $rootScope.appconfig.serverUrl + "/mobile/fill/short_personal_info/submit",
                url: $rootScope.appconfig.serverUrl + "/mobile/fill/short_personal_info/submit",
                data: JSON.stringify($scope.user),
                method: "POST",
                headers: {
                    'Content-type': 'application/json',
                    'apiKey': window.localStorage['apiKey']
                }
            }
            $http(config).success(function (data, status, headers, config) {
                console.log("Successfully submitted form: " + data);
                alert("Successfully Submitted");
                $state.go('app.visitorPayment');
            }).error(function (data, status, headers, config) {
               console.log("Error");
                alert("Error");
            });

        } else {
            console.log("invalid form ");
            alert("Please fill all field's first");
        }
    }
     
    $scope.templatePath="templates/propertyDetailsForm/form1.html";
    $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
    $scope.user.requirement.subType="1RK";
    $scope.user.requirement.choiceOfFloor="0";

    $('#dummyRequirementSubType').on('change', function() {
        $scope.user.requirement={};
        if ( this.value == 'FlatOrTerraceFlat')
        {
            $scope.user.requirement={};
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.subType="1RK";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form1.html";
        }
        if ( this.value == 'ChawlRoom')
        {
            $scope.user.requirement={};
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.subType="1RK";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form2.html";
        }
        if ( this.value == 'DuplexFlat')
        {
            $scope.user.requirement={};
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.subType="1RK";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form1.html";
        }
        if ( this.value == 'PentHouse')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
        if ( this.value == 'StudioApartment')
        {
            $scope.user.requirement={};
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.terraceCarpetArea="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form4.html";
        }
          if ( this.value == 'HighriseApartment')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceCarpetArea="AsPerPlan";
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form4.html";
        }
         if ( this.value == 'IndependentBungalow')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'JointBanglowOrTwinBungalow')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'RowHouse')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'RowHousePlot')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'IndependentBungalowPlot')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'JointBungalowPlot')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'FarmHouse')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'FarmHousePlot')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'HolidayHome')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'ServiceApartment')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceCarpetArea="AsPerPlan";
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form4.html";
        }
         if ( this.value == 'Villa')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'SecondHome')
        {
            $scope.user.requirement={};
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.subType="1RK";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form1.html";
        }
         if ( this.value == 'Other1')
        {
            $scope.user.requirement={};
            $scope.templatePath="templates/propertyDetailsForm/form10.html";
        }



        if ( this.value == 'Shop')
        {
            $scope.user.requirement={};
            $scope.user.requirement.loftRequired="true";
            $scope.user.requirement.otlaRequired="true";
            $scope.user.requirement.specifyHeightRequired="true";
            $scope.user.requirement.openSpaceRequired="true";
            $scope.user.requirement.terraceRequired="true";
            $scope.user.requirement.toiletType="common";
            $scope.user.requirement.isSeparateWaterConnectionRequired="true";
            $scope.user.requirement.isSeparatePowerConnectionRequired="true";
            $scope.user.requirement.powerConnectionType="SinglePhase";
            $scope.templatePath="templates/propertyDetailsForm/form9.html";
        }
         if ( this.value == 'OfficeInITParkOrSez')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'CommercialLand')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'CommercialOfficeOrSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'CommercialShowroom')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'Kiosk')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'ConstructedHotelSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'HotelPlot')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
         if ( this.value == 'GuestHouse')
        {
            $scope.user.requirement={};
            $scope.user.requirement.noOfBedrooms="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.choiceOfFloor="0";
            $scope.templatePath="templates/propertyDetailsForm/form3.html";
        }
         if ( this.value == 'BusinessCenterOrBusinessPark')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'Warehouse')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'IndustrialLand')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
         if ( this.value == 'IndustrialBuilding')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'IndustrialShed')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'BenquetHall')
        {
            $scope.user.requirement={};
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.templatePath="templates/propertyDetailsForm/form6.html";
        }
         if ( this.value == 'SpaceInRetailMall')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'Godown')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'ColdStorage')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'Factory')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'ManufacturingUnit')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.terraceChoice="AsPerPlan";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.user.requirement.toiletWcBathRequirements="AsPerPlan";
            $scope.user.requirement.gardenAreaRequired="NotRequired";
            $scope.templatePath="templates/propertyDetailsForm/form7.html";
        }
         if ( this.value == 'HotelSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
        if ( this.value == 'HospitalSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
         if ( this.value == 'SchoolSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
         if ( this.value == 'BankSpace')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.user.requirement.noOfRoomsRequired="1";
            $scope.templatePath="templates/propertyDetailsForm/form8.html";
        }
         if ( this.value == 'Other2')
        {
            $scope.user.requirement={};
            $scope.templatePath="templates/propertyDetailsForm/form10.html";
        }

         if ( this.value == 'AgriculturalLand')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'FarmLand')
        {
            $scope.user.requirement={};
            $scope.user.requirement.plotAreaMeasure="Square Feet";
            $scope.templatePath="templates/propertyDetailsForm/form5.html";
        }
         if ( this.value == 'Other3')
        {
            $scope.user.requirement={};
            $scope.templatePath="templates/propertyDetailsForm/form10.html";
        }

    });


    $scope.states=[];
    $scope.Districts=[];
    $scope.Talukas=[];
    $scope.Pincodes=[];

    var configStates = {
        url: $rootScope.appconfig.serverUrl + "/api/staticdata/pincodes/",

        method: "GET",
        headers: {
            'Content-type': 'application/json'
        }
    }
    $http(configStates).success(function (data, status, headers, config) {
        console.log("Get States Successfully.");
        $scope.states=data;
    }).error(function (data, status, headers, config) {
        console.log("Error while get States.")
    });

    $scope.changeState= function () {
            console.log("in changeState");

        var configDistricts = {
            url: $rootScope.appconfig.serverUrl + "/api/staticdata/pincodes/"+$scope.user.locationsHiddenField[0].state,

            method: "GET",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(configDistricts).success(function (data, status, headers, config) {
            console.log("Get Districts Successfully.");
            $scope.Districts=data;
        }).error(function (data, status, headers, config) {
            console.log("Error while get Districts.")
        });
    }

    $scope.changeDistrict= function () {
        console.log("in changeDistrict");

        var configTalukas = {
            url: $rootScope.appconfig.serverUrl + "/api/staticdata/pincodes/"+$scope.user.locationsHiddenField[0].state+"/"+$scope.user.locationsHiddenField[0].district,

            method: "GET",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(configTalukas).success(function (data, status, headers, config) {
            console.log("Get Talukas Successfully.");
            $scope.Talukas=data;
        }).error(function (data, status, headers, config) {
            console.log("Error while get Talukas.")
        });
    }

    $scope.changeTaluka= function () {
        console.log("in changeTaluka");

        var configPincodes = {
            url: $rootScope.appconfig.serverUrl + "/api/staticdata/pincodes/"+$scope.user.locationsHiddenField[0].state+"/"+$scope.user.locationsHiddenField[0].district+"/"+$scope.user.locationsHiddenField[0].taluka,

            method: "GET",
            headers: {
                'Content-type': 'application/json'
            }
        }
        $http(configPincodes).success(function (data, status, headers, config) {
            console.log("Get Pincodes Successfully.");
            $scope.Pincodes=data;
        }).error(function (data, status, headers, config) {
            console.log("Error while get Pincodes.")
        });
    }

})



