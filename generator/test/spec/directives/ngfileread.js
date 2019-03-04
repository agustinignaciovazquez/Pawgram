'use strict';

describe('Directive: ngFileRead', function () {

  // load the directive's module
  beforeEach(module('pawgramApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<ng-file-read></ng-file-read>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the ngFileRead directive');
  }));
});
