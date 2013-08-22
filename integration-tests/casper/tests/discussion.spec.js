/* global document */
'use strict';

/**
 *
 * Discussion feature tests 
 *
 **/
var casper = require('casper').create(),
    host = casper.cli.get('host') || "http://m.code.dev-theguardian.com/";


// Test that comment counts appear on the front
    
casper.start()
    
casper.open(host + '/uk').then(function() {
    
    casper.test.comment('Comment counts on the front');
    
    casper.waitForSelector('.trail__count--commentcount', function() {
        return this.test.assertEvalEquals(function () {
            // should read '812 comments'
            var hasCommentCount = /^\d+\s+comments$/.test(document.querySelector('.trail__count--commentcount').textContent)
            // href should contain '#comments'
            var hasCommentAnchor = /\#comments$/.test(document.querySelector('.trail__count--commentcount a').getAttribute('href'))
            return (hasCommentCount && hasCommentAnchor)
        }, true, "See the number of comments in the page and link through to the #comments article state")
    }, function () {
        casper.test.fail('Comment count did not show');
    })

});

casper.open(host + 'world/2013/jun/06/obama-administration-nsa-verizon-records').then(function() {

    casper.test.comment('Read top level comments');
//wait for comment count to be visible
    casper.waitForText('1678', function() {
    casper.click('.js-show-discussion.js-top');
    },function timeout(){

    casper.captureSelector('body.png', 'body');
        casper.test.fail('failed to find comment bubble');

    });


    casper.waitForSelector('.d-discussion',function(){

        this.test.assertEvalEquals(function() {

            return document.querySelectorAll('.d-comment--top-level').length;

        }, 10, 'Then I can see 10 top level comments');


        this.test.assertSelectorHasText('.d-comment__author', 'tenacity', 'And the first comment is authored by "tenacity"');

    },function timeout(){
        casper.test.fail('Comments failed to load');
    });

});

casper.open(host + 'world/2013/jun/06/obama-administration-nsa-verizon-records').then(function() {

    casper.test.comment('Show more comments');

    casper.click('.js-show-more-comments');

    this.waitFor(function check() {

        return this.evaluate(function(){

            return document.querySelectorAll('.d-comment--top-level').length === 20;

        });

    }, function then(){

        this.test.assertEvalEquals(function() {

            return document.querySelectorAll('.d-comment--top-level').length;

        }, 20, 'Then I can see 20 top level comments');

    }, function timeout(){
        casper.test.fail('Comments failed to load');
    });

});

casper.run(function() {
    this.test.renderResults(true, 0, this.cli.get('save') || false);
});

