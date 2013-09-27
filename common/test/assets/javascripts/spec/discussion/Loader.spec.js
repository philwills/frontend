define([
    'common',
    'ajax',
    'bean',
    'helpers/fixtures',
    'fixtures/discussion',
    'modules/discussion/loader'
], function(
    common,
    ajax,
    bean,
    fixtures,
    discussionJson,
    Loader
) {

    describe('Discussion Loader', function() {
        // Setup
        var context, button, server, loader,
            fixturesId = 'discussion-loader',
            fixture = {
                id: fixturesId,
                fixtures: [
                    '<a class="'+ Loader.CONFIG.classes.component +' d-show-cta" href="/discussion/p/3ht42" data-is-ajax data-link-name="View all comments" data-discussion-id="/p/3ht42">View all comments <span class="d-commentcount speech-bubble"><span class="js-commentcount__number"></span></span></a>'
                ]
            };

        // setup
        ajax.init({page: {
            ajaxUrl: '',
            edition: 'UK'
        }});

        // rerender the button each time
        beforeEach(function() {
            server = sinon.fakeServer.create();
            fixtures.render(fixture);
            context = document.getElementById(fixturesId);
            loader = new Loader(context);
            loader.attachTo();
        });

        afterEach(function() {
            server.restore();
            fixtures.clean(fixturesId);
        });

        describe('init', function() {

            it('should bind load click events on component elements', function() {
                var callback = jasmine.createSpy();
                loader.on('loading', callback);

                runs(function() {
                    server.respondWith([200, {}, discussionJson]);
                    bean.fire(loader.elem, 'click');
                });

                waitsFor(function() {
                    server.respond();
                    return callback.calls.length > 0;
                }, 500);
            });

        });



    });

}); //define