define(['common', 'bean', 'modules/userPrefs'], function (common, bean, userPrefs) {

    /*
        #gu.prefs.adplacement=inline-5
        #gu.prefs.adplacement=inline-7
     */

    function AdPlacementTest(config, context) {
        userPrefs.setPrefs(window.location); // Run this to ensure the prefs are set first

        var adplacement = userPrefs.get('adplacement').split('-'),
            adType      = adplacement[0];

        this.context = context;

        switch(adType) {
            case 'inline': this.insertInlineAds(adplacement[1]); break;
        }
    }

    AdPlacementTest.prototype.insertInlineAds = function(interval) {
        var advertHtml  = '<div class="ad-slot ad-slot--placeholder">'
                        + '  <div class="ad-container">ADVERT</div>'
                        + '</div>';

        common.$g('.article-body p:nth-of-type('+interval+'n)', this.context).after(advertHtml);
    };


    return AdPlacementTest;
});