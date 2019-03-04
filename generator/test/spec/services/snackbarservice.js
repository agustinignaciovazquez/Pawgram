'use strict';

describe('Service: snackbarService', function () {

  // load the service's module
  beforeEach(module('pawgramApp'));

  // instantiate service
  var snackbarService;
  beforeEach(inject(function (_snackbarService_) {
    snackbarService = _snackbarService_;
  }));

  it('should do something', function () {
    expect(!!snackbarService).toBe(true);
  });

});
