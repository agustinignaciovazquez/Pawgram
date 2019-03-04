'use strict';

describe('Controller: NotfoundctrlCtrl', function () {

  // load the controller's module
  beforeEach(module('pawgramApp'));

  var NotfoundctrlCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    NotfoundctrlCtrl = $controller('NotfoundctrlCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(NotfoundctrlCtrl.awesomeThings.length).toBe(3);
  });
});
