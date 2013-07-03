define(function() {

    var html = '<style>';
    html += '   ul.flex {';
    html += '       transition: background-color 0.5s linear;';
    html += '       -moz-transition: background-color 0.5s linear;';
    html += '       -o-transition: background-color 0.5s linear;';
    html += '       -webkit-transition: background-color 0.5s linear;';
    html += '   }';
    html += '   .shut ul.flex { background-color: #999; } /* illustrative */';
    html += '   ul.flex { background-color: #ccc; }';
    html += '</style>';
    html += '<!-- trail -->';
    html += '<div id="trail-a" data-count="5">';
    html += '   <ul>';
    html += '       <li class="t1">one</li>';
    html += '       <li class="t2">two</li>';
    html += '   </ul>';
    html += '   <ul class="flex">';
    html += '   <li class="t3">three</li>';
    html += '   <li class="t4">four</li>';
    html += '   <li class="t5">five</li>';
    html += '   </ul>';
    html += '</div>';
    html += '<div id="trail-b" data-count="3">';
    html += '   <ul>';
    html += '       <li class="t1">one</li>';
    html += '       <li class="t1">two</li>';
    html += '   </ul>';
    html += '   <ul class="flex">';
    html += '   <li class="t2">three</li>';
    html += '   </ul>';
    html += '</div>';

    html += '<div id="trail-c" data-count="3" class="shut">';
    html += '</div>';

    html += '<div id="trail-d" data-count="1" class="shut">';
    html += '</div>';

    html += '<div id="trail-e" data-count="3">';
    html += '   <ul>';
    html += '       <li class="t1">one</li>';
    html += '       <li class="t2">two</li>';
    html += '   </ul>';
    html += '   <ul class="flex">';
    html += '       <li class="t3">three</li>';
    html += '   </ul>';
    html += '</div>';

    html += '<div id="trail-f" data-count="-1">';
    html += '</div>';

    html += '<div id="trail-g" data-count="2">';
    html += '</div>';

    html += '<div id="trail-h" data-count="3">';
    html += '   <ul>';
    html += '       <li class="t1">one</li>';
    html += '       <li class="t2">two</li>';
    html += '   </ul>';
    html += '   <ul class="flex">';
    html += '       <li class="t3">three</li>';
    html += '   </ul>';
    html += '</div>';

    return html;
});