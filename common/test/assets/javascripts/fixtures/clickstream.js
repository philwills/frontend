define(function(){
    var html =  '';
    html += '<div data-link-name="outer div">';
    html += '    <p id="not-inside-a-link">';
    html += '        <a href="/foo" id="click-me" data-link-name="the link">The link</a>';
    html += '        <a href="/foo" id="click-me-ancestor" data-link-name="the ancestor">';
    html += '            <span>';
    html += '                    <span>';
    html += '                        <span id="click-me-descendant" data-link-name="the descendant">The link descendant</span>';
    html += '                    </span>';
    html += '                </span>';
    html += '            </a>';
    html += '            <a href="/foo" id="click-me-quick" data-is-ajax="true" data-link-name="xhr link">Xhr Link</a>';
    html += '        <button id="click-me-button" data-link-name="the button">Span Link</button>';
    html += '            <p href="#hello" id="click-me-slow" data-link-name="paragraph">Paragraph Link</p>';
    html += '            <a href="/foo" id="click-me-internal" data-link-name="internal link">Same-host link</a>';
    html += '            <a href="http://google.com/foo" id="click-me-external" data-link-name="external link">Other-host link</a>';
    html += '            <span data-link-context="the outer context">';
    html += '                <span data-link-context="the inner context">';
    html += '                    <button href="/foo" id="click-me-link-context" data-link-name="the contextual link">The link</button>';
    html += '                </span>';
    html += '            </span>';
    html += '        </p>';
    html += '</div>';

    return html;
});