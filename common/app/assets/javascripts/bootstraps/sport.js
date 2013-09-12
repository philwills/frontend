/*global guardian:false */
define([
    //Common libraries
    "common",
    "bonzo",
    "modules/tabs"
], function (
    common,
    bonzo,
    Tabs
) {

    var modules = {

        showTabs:  function(context){
            var tabs = new Tabs();
            var container = context.querySelector('.sport-oppm__body');
            tabs.init(container);
        }
    };

    var ready = function(req, config, context) {
        modules.showTabs(context);
    };

    return {
        init: ready
    };

});
