'use strict';

describe('Service: jQueryService', function () {

  // load the service's module
  beforeEach(module('pawgramApp'));

  // instantiate service
  var jQueryService;
  beforeEach(inject(function (_jQueryService_) {
    jQueryService = _jQueryService_;
  }));

  it('should do something', function () {
    expect(!!jQueryService).toBe(true);
  });

});
