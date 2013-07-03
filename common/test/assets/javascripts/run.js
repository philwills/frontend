var specs = Object.keys(window.__karma__.files).filter(function (file) {
    return (/Clickstream\.spec\.js$/).test(file);
});
require.config({
    'baseUrl': "/base/common/app/assets/javascripts/",
    'paths': {
        'common':       'common',
        'bonzo':        'components/bonzo/src/bonzo',
        'qwery':        'components/qwery/mobile/qwery-mobile',
        'bean':         'components/bean/bean',
        'reqwest':      'components/reqwest/src/reqwest',
        'domwrite':     'components/dom-write/dom-write',
        'EventEmitter': 'components/eventEmitter/EventEmitter',
        'swipe':        'components/swipe/swipe',
        'swipeview':    'components/swipeview/src/swipeview',
        'omniture':     '../../../app/public/javascripts/vendor/omniture',
        'Fixtures':     '../../../test/assets/javascripts/helpers/fixtures',
        'fixtures':     '../../../test/assets/javascripts/fixtures',
        'text':         '../../../test/assets/javascripts/components/requirejs-text/text'
    },

    // ask Require.js to load these files (all our tests)
    deps: specs,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});