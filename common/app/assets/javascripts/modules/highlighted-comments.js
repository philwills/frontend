define(['common', 'modules/lazyload'], function (common, LazyLoad) {

    function getHighlighted(config, context) {
        var container = context.querySelector('.highlighted-comments-placeholder');

        if (container) {
            var url = '/discussion/highlighted.json';
            new LazyLoad({
                url: url,
                container: container,
                success: function () {
                    common.mediator.emit('modules:highlighted:loaded', container);
                },
                error: function(req) {
                    common.mediator.emit('module:error', 'Failed to load highlighted: ' + req.statusText, 'modules/highlighted-comments.js');
                }
            }).load();
        }
    }
    return getHighlighted;
});