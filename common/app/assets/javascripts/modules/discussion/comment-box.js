define(['modules/component', 'ajax'], function(Component, ajax) {

/**
 * @constructor
 * @extends Component
 */
function CommentBox(opts) {}
Component.create(CommentBox);

/** @override */
CommentBox.prototype.ready = function() {
    this.on('.js-comment-box', 'submit', this.addComment);
};

CommentBox.prototype.addComment = function(e) {
    e.preventDefault();

//    var formEl = e.currentTarget
//
//    alert(formEl.elements)
//    alert(formEl.elements.insight.value)
//
//    if (formEl.elements.insight.value) {
//        if (confirm('Are you sure you want to claim that ' + formEl.elements.insight.value + '?')) {
//            comment = {
//                body: formEl.elements.body.value
//            };

            // ajax()
//            formEl.submit()
//        }
//    }
};

return CommentBox;

}); // define