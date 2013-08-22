/* global window, document */
'use strict';

/**
 * Feature: Section Fronts - Culture
 *   As a Guardian user
 *   I want to get a further break-down of sections on the culture section front
 *   So that I can navigate content easier
 **/
var casper = require('casper').create(),
    host = casper.cli.get('host') || "http://m.code.dev-theguardian.com";

casper.start(host + '/culture');

var clearLocalStorage = function() {
    casper.evaluate(function() { window.localStorage.clear(); });
};

var cultureBlockSelector = 'section[data-link-name*="block | Culture"]';

/**
 * Scenario: Page contains a culture block
 *   Given I am on the 'culture' section front
 *   Then I should see a block called culture
 **/
casper.then(function() {
    casper.test.comment('Page contains a culture block');

    casper.test.assertExists(
        cultureBlockSelector,
        'Culture should have a section block'
    );

    casper.test.assertSelectorHasText(
      cultureBlockSelector + ' > h1', 'Culture',
      'Culture section block should have a title');
});

/**
 * Scenario: Culture block contains five trails
 *   Given I am on the 'culture' section front
 *   Then I should see a Culture block with five trails
 **/
casper.then(function() {
    casper.test.comment('Culture block contains five trails');

    clearLocalStorage();
    casper.reload();

    var trailCount = this.evaluate(function(selector) {
        return document.querySelectorAll(selector + ' ul li').length;
    }, cultureBlockSelector);

    casper.test.assertEqual(trailCount, 5, 'Culture block should contain five trails.');
});


/**
  * Scenario Outline: Users can view more top stories for the Culture block
  *   Given I am on the 'culture' section front
  *   Then the '<section>' section should have a 'Show more' cta that loads in more top stories
 **/
casper.then(function() {
    casper.test.comment('Culture block has a Show More call to action');

    clearLocalStorage();
    casper.reload();

    casper.waitForSelector(cultureBlockSelector + ' .cta',function(){
        casper.click(cultureBlockSelector + ' .cta');

        casper.waitFor(function check() {
            return this.evaluate(function(selector) {
                return document.querySelectorAll(selector + ' ul li').length > 5;
            }, cultureBlockSelector)
        });

    });
});


casper.run(function() {
    this.test.renderResults(true, 0, this.cli.get('save') || false);
});
