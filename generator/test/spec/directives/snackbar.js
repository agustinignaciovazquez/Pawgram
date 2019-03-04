'use strict';

describe('Directive: snackbar', function () {

  // load the directive's module
  beforeEach(module('pawgramApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<snackbar></snackbar>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the snackbar directive');
  }));
});
