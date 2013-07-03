module.exports = function(karma) {
    karma.set({
        basePath: '',

        frameworks: ['jasmine', 'requirejs'],

        browsers: ['PhantomJS'],

        // list of files / patterns to load in the browser
        files: [
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/call.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/spy.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/stub.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/util/event.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/util/fake_timers.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/util/fake_server.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/sinon/lib/sinon/util/fake_xml_http_request.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/jasmine-sinon/lib/jasmine-sinon.js', included: true},
            {pattern: 'common/test/assets/javascripts/components/seedrandom/index.js', included: true},
            {pattern: 'common/test/assets/javascripts/helpers/cors.js', included: true},
            
            {pattern: 'common/app/assets/javascripts/common.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/bonzo/src/bonzo.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/qwery/mobile/qwery-mobile.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/bean/bean.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/reqwest/src/reqwest.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/dom-write/dom-write.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/eventEmitter/EventEmitter.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/swipe/swipe.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/swipeview/src/swipeview.js', included: false},
            {pattern: 'common/app/public/javascripts/vendor/omniture.js', included: false},
            {pattern: 'common/test/assets/javascripts/helpers/fixtures.js', included: false},
            {pattern: 'common/test/assets/javascripts/components/requirejs-text/text.js', included: false},
            {pattern: 'common/app/assets/javascripts/components/gu-id/id.js', included: false},

            {pattern: 'common/app/assets/javascripts/*.js', included: false},
            {pattern: 'common/app/assets/javascripts/modules/*.js', included: false},
            {pattern: 'common/app/assets/javascripts/modules/**/*.js', included: false},
            {pattern: 'common/test/assets/javascripts/spec/*.spec.js', included: false},
            {pattern: 'common/test/assets/javascripts/fixtures/*', included: false, served: true},
            {pattern: 'common/test/assets/javascripts/fixtures/**/*.js', included: false, served: true},
            {pattern: 'common/test/assets/javascripts/helpers/*.js', included: false},
            'common/test/assets/javascripts/run.js'
        ],

        exclude: [
            'common/test/assets/javascripts/spec/Cookies.spec.js',
            'common/test/assets/javascripts/spec/Aware.spec.js',
            'common/test/assets/javascripts/spec/DocumentWrite.spec.js',
            'common/test/assets/javascripts/spec/Gallery.spec.js',
            'common/test/assets/javascripts/spec/GallerySwipe.spec.js',
            'common/test/assets/javascripts/spec/OmnitureLib.spec.js',
            'common/test/assets/javascripts/spec/Swipe.spec.js',
            'common/test/assets/javascripts/spec/TrailBlockShowMore.spec.js'
        ],

        plugins: [
            'karma-jasmine',
            'karma-requirejs',
            'karma-phantomjs-launcher',
            'karma-teamcity-reporter',
            'karma-chrome-launcher'
            // 'karma-firefox-launcher',
            // 'karma-junit-reporter'
        ]
    });
};