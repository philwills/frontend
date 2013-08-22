/* global document */
'use strict';

/**
 *
 * Navigation feature tests
 *
 **/
var casper = require('casper').create(),
    host = "http://m.code.dev-theguardian.com/";

casper.start(host + 'world/2013/jun/06/obama-administration-nsa-verizon-records');

/**
 *   Scenario: Navigation buttons
 *     Given I am on an article page
 *     When I choose to click top stories button
 *     Then I can see 10 top stories
 **/

casper.then(function() {

    casper.test.comment('Load top stories');

    casper.waitForSelector('.nav-popup-topstories.lazyloaded',function(){
        this.test.assertEvalEquals(function() {
            return document.querySelectorAll('.nav-popup-topstories li').length;
        }, 10, 'Then I can see 10 top stories');

    }, function timeout(){
        casper.test.fail('Top stories failed to load');
    });

});

casper.then(function() {

    casper.test.comment('Top stories title is inserted');

    casper.waitForSelector('.nav-popup-topstories.lazyloaded',function(){
        this.test.assertEvalEquals(function() {
            return document.querySelector('.nav-popup-topstories :first-child').textContent;
        }, 'Top stories', 'Then the top stories title is inserted');

    }, function timeout(){
        casper.test.fail('Top stories title is not in');
    });

});

casper.run(function() {
    this.test.renderResults(true, 0, this.cli.get('save') || false);
});