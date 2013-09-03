define([
    'js!threejs',
    'streamview/server-emitter',
    'streamview/fastly-emitter'
], function(
    THREEJS,
    ServerEmitter,
    FastlyEmitter
) {

    var fastlyMetrics = {},
        serverMetrics = {};

    return {
        init: function(opts) {
            this.opts = opts;
            console.log('streamview init', opts)

            // Process our stats
            _.each(opts.serverMetrics, function(vals, key, list) {
                serverMetrics[key] = _.object(_.first(vals), _.last(vals));
            });


            // Process fastly stats
            _.each(opts.fastlyMetrics, function(vals, key, list) {
                if (key.indexOf('Europe') != -1) {
                    fastlyMetrics.Europe = _.object(_.first(vals), _.last(vals));
                } else if (key.indexOf('USA') != -1) {
                    fastlyMetrics.USA = _.object(_.first(vals), _.last(vals));
                }
            });

            console.log(fastlyMetrics, serverMetrics);

            this.start3D();
        },


        start3D: function() {

            var container, stats;
            var camera, scene, renderer, group, particle;

            var lasttime = Date.now(), elapsed;

            container = document.querySelector('.streamview');

            camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 1, 3000 );
            camera.position.z = 500; //1000

            scene = new THREE.Scene();

            group = new THREE.Object3D();
            scene.add( group );

            serverGroup = new THREE.Object3D();
            scene.add( serverGroup );


            renderer = new THREE.CanvasRenderer();
            //renderer.setSize( window.innerWidth, window.innerHeight );
            renderer.setSize(container.offsetWidth, container.offsetHeight);
            container.appendChild( renderer.domElement );

            stats = new Stats();
            stats.domElement.style.position = 'absolute';
            stats.domElement.style.top = '0px';
            container.appendChild( stats.domElement );


            var Europe = FastlyEmitter.init({
                            group: group,
                            metrics: fastlyMetrics.Europe,
                            pos: {
                                sx: 250,
                                sy: 100,
                                dx: 250,
                                dy: 300
                            }
                        });

            var USA = FastlyEmitter.init({
                            group: group,
                            metrics: fastlyMetrics.USA,
                            pos: {
                                sx: 250,
                                sy: -100,
                                dx: 250,
                                dy: -300
                            }
                        });

            // Router
            var router = ServerEmitter({
                                title:   'Router',
                                group:   serverGroup,
                                hue:     0.6,
                                metrics: serverMetrics.Router,
                                pos: {
                                    sx: -50,
                                    sy: 200,
                                    dx: -50,
                                    dy: -200
                                }
                            })

            // App Servers
            var serverTopY = 200;
            _.each(serverMetrics, function(metrics, serverName) {
                if (serverName == 'Router') return;

                var hue;

                switch(serverName) {
                    case 'Applications': hue = 0.2; break;
                    case 'Article':      hue = 0.3; break;
                    case 'Front':        hue = 0.4; break;
                }

                var server = ServerEmitter({
                                title:   serverName,
                                group:   serverGroup,
                                hue:     hue,
                                metrics: metrics,
                                pos: {
                                    sx: -350,
                                    sy: serverTopY,
                                    dx: -350,
                                    dy: serverTopY - 50
                                }
                            })

                serverTopY -= 150;
            });


            animate();

            function animate() {
                requestAnimationFrame(animate);
                render();
                stats.update();
            }


            function render() {
                renderer.render( scene, camera );
            }

        }
    }

});