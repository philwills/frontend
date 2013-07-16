define(['common','bonzo', 'bean'], function (common, bonzo, bean) {


    function DataViz() {
        var self = this;
        this.selector = 'table[data-viz-type]';

        var vizNodes = document.querySelectorAll(this.selector);

        if (vizNodes.length) {
            self.bindEvents();

            require(['js!d3'], function(d3) {
                Array.prototype.forEach.call(vizNodes, function(node) {
                    //console.log(node.offsetTop);
                    var data = self.serializeTable(node);
                    self.doughnut(node, data);
                });
            });
        }
    }

    function toRadians(angle) {
        return angle * (Math.PI / 180);
    }

    /*function arcTween(b) {
        var i = d3.interpolate({value: b.previous}, b);
        return function(t) {
            return arc(i(t));
        };
    }*/

    function isVisible(node) {
        var vScroll    = window.pageYOffset || document.documentElement.scrollTop,
            viewBottom = vScroll + window.innerHeight,
            nodeTop    = node.offsetTop;

        return nodeTop < viewBottom;
    }


    DataViz.prototype.bindEvents = function() {
        bean.add(window, 'scroll', function (e) {
            vScroll = window.pageYOffset || document.documentElement.scrollTop;

            console.log(vScroll);
        });
    };

    DataViz.prototype.serializeTable = function(el) {
        var tableData = [];

        for (var i=0; i < el.tBodies[0].rows.length; i++) {
            var tableRow = el.tBodies[0].rows[i];
            tableData.push(
                {
                    "Value": tableRow.cells[1].innerHTML,
                    "Color": tableRow.cells[0].getAttribute('data-viz-color')
                }
            );
        }

       return tableData;
    };


    DataViz.prototype.doughnut = function(node, data) {
        var displayNode = bonzo.create('<div class="dataviz" style="text-align: center"></div>')[0],
            title = node.getAttribute('data-viz-title') || "",
            doughnutSize = 180,
            doughnutRadius = doughnutSize * 0.5,
            doughnut = d3.layout.pie().sort(null).value(function(d) { return d.Value }),
            arc = d3.svg.arc().innerRadius(65).outerRadius(doughnutRadius),
            width = doughnutSize * 2,
            height = doughnutSize;

            svg = d3.select(displayNode)
                .append("svg:svg")
                .attr("width", "100%")
                .attr("height", height)
                .attr("viewBox", "0 0 " + width + " " + height )
                .attr("preserveAspectRatio", "xMidYMid meet")
                .append("svg:g")
                .attr("transform", "translate(" + width/2 + "," + doughnutRadius + ")"),

            arcs = svg.selectAll("path")
                .data(doughnut(data))
                .enter()
                .append("svg:g")

        // Draw the data arcs
        arcs.append("svg:path")
            .each(function() { this._current = {startAngle: 0, endAngle: 0}; })
            .attr("fill", function(d, i) { return data[i].Color; })
            .transition()
            .duration(2000)
            .attrTween("d", function(d) {
                var interpolate = d3.interpolate(this._current, d);
                this._current = interpolate(0);
                return function(t) {
                    //var result = arc(interpolate(t)).replace(/(L.*)/, '');
                    //return result;
                    t.startAngle = t.endAngle - toRadians(2);
                    return arc(interpolate(t));
                };
            });

            //.duration(1000)
            /*.attr("d", function(d) {
               var result = arc(d);
               result = result.replace(/(L.*)/, '');
                console.log(result);
               return result;
               //console.log(arc(d));
               //return arc(d);
            });*/

        // Add Labels
        var pos = d3.svg.arc().innerRadius(65 + 40).outerRadius(doughnutRadius + 40);
        arcs.append("svg:text")
            .attr("transform", function(d) { return "translate(" +
                pos.centroid(d) + ")"; })
            .attr("dy", 5)
            .attr("text-anchor", "middle")
            //.attr("fill", function(d, i) { return data[i].Color; })
            .attr("fill", "#666666")
            .attr("fill-opacity", "0")
            .text(function(d, i) { return d.value.toFixed(0) + "%"})
            .transition()
            .duration(1000)
            .delay(1500)
            .attr("fill-opacity", "1");

        // This draws a thicker edge after each arc
        /*arcs.append("svg:path")
            .attr("fill", "#ffffff")
            .attr("d", function(d) {
                d.startAngle = d.endAngle - toRadians(2);
                return arc(d);
            });*/


        svg.append('svg:text')
            .attr('dx', 0)
            .attr('dy', 5)
            .attr("class", "doughnut__unit")
            .attr("text-anchor", "middle")
            .text(title);

        var markerSizeInDegs = 8,
            markers = svg.append("svg:g")
                         .attr("class", "doughnut__markers");


        svg.append('svg:circle')
           //.attr("cx", "125")
           // .attr("cy", "125")
            .attr("r", 65)
           .attr("fill", "none")
           .attr("stroke",  "#ffffff")
           .attr("stroke-width", "50")
           .attr("stroke-dasharray", "1, 5")
            .attr("stroke-opacity", "0.5")
           .attr("stroke-location", "inside")

        /*for (var i=0; i<=360; i+=markerSizeInDegs) {
            markers.append("svg:path")
                .attr("fill", "#ffffff")
                .attr("fill-opacity", "0.5")
                .attr("d", arc({
                    startAngle: toRadians(i),
                    endAngle:   toRadians(i+0.5)
                }));
        }*/


        bonzo(node).replaceWith(displayNode);
    };


    return DataViz;
});


/*

var doughnutDemoTable = document.querySelector('[data-viz-type=doughnut]'),
    doughnutDemoData = [];

for (var i=0; i < doughnutDemoTable.tBodies[0].rows.length; i++) {
    var tableRow = doughnutDemoTable.tBodies[0].rows[i];
    doughnutDemoData.push(
        {
            "Value": +tableRow.cells[1].innerHTML,
            "Color": tableRow.cells[0].getAttribute('data-viz-color')
        }
    );
}
console.log(doughnutDemoData);



//var DougnutDemo = function(){

//this.init = function() {
this.displayObject = document.createElement('div');

var doughnutSize = 200,
    doughnutRadius = doughnutSize * 0.5,
    doughnut = d3.layout.pie().sort(null).value(function(d) { return d.Value }),
    arc = d3.svg.arc().innerRadius(50).outerRadius(doughnutRadius),

    svg = d3.select(this.displayObject)
        .append("svg:svg")
        .attr("width", doughnutSize)
        .attr("height", doughnutSize)
        .append("svg:g")
        .attr("transform", "translate(" + doughnutRadius + "," + doughnutRadius + ")"),

    arcs = svg.selectAll("path")
        .data(doughnut(doughnutDemoData))
        .enter().append("svg:path")
        .attr("fill", function(d, i) { return doughnutDemoData[i].Color; })
        .attr("d", arc)
        .each(function(d) { this._current = d; })
        .attr("transform", "rotate(-145)");

svg.append('svg:text')
    .attr('dx', -25)
    .attr('dy', 20)
    .attr("class", "doughnut__unit")
    .text("%");

document.querySelector('.article-body').appendChild(this.displayObject)


//}
//};

//return DoughnutDemo;

//});

//require(['doughnut'], function(doughnut) {
//    doughnut.init();
//});

*/