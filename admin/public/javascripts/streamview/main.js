define([
    'js!threejs!order',
    'js!components/threejs/build/optimer_regular.typeface.js!order',
    'streamview/server-emitter',
    'streamview/fastly-emitter'
], function(
    THREEJS,
    Optimer,
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
        /*
        attributes: {
            size: { type: 'f', value: [] },
            customColor: { type: 'c', value: [] }
        },

        uniforms: {
            amplitude: { type: "f", value: 1.0 },
            color:     { type: "c", value: new THREE.Color( 0xffffff ) },
            texture:   { type: "t", value: THREE.ImageUtils.loadTexture( "/assets/images/spark1.png" ) },
        },*/

        shaderMaterial: new THREE.ShaderMaterial( {

            uniforms: {
                amplitude: { type: "f", value: 1.0 },
                color:     { type: "c", value: new THREE.Color( 0xffffff ) },
                texture:   { type: "t", value: THREE.ImageUtils.loadTexture( "/assets/images/spark2.png" ) },
            },

            attributes: {
                size: { type: 'f', value: [] },
                customColor: { type: 'c', value: [] }
            },

            vertexShader:   document.getElementById( 'vertexshader' ).textContent,
            fragmentShader: document.getElementById( 'fragmentshader' ).textContent,

            blending:       THREE.AdditiveBlending,
            depthTest:      false,
            transparent:    true

        }),


        Pool: {
            __pools: [],

            // Get a new Vector
            get: function() {
                if (this.__pools.length>0) {
                    return this.__pools.pop();
                }

                console.log("pool ran out!")
                return null;

            },

            // Release a vector back into the pool
            add: function(v) {
                this.__pools.push(v);
            }

        },

        newpos: function(x, y, z) {
            return new THREE.Vector3(x, y, z);
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

            particles = new THREE.Geometry();
            particleCloud = new THREE.ParticleSystem( particles, this.shaderMaterial );
            particleCloud.dynamic = true;

            vertices = particleCloud.geometry.vertices;
            values_size = this.shaderMaterial.attributes.size.value;
            values_color = this.shaderMaterial.attributes.customColor.value;


            scene.add( particleCloud );

            for (i=0; i<2000; i++ ) {
                particles.vertices.push(this.newpos(Math.random() *200 - 100, Math.random() *100+150, Math.random() *50));

                values_size[i] = 55;
                values_color[i] = new THREE.Color( 0xaaff00 );
                values_color[i].setHSL( 0, 1, 0 );

                this.Pool.add(i);
            }

            /*console.log(vertices);
            for (v=0; v<vertices.length; v++) {
                particles.vertices[v].set(Number.POSITIVE_INFINITY,Number.POSITIVE_INFINITY, Number.POSITIVE_INFINITY);
            }*/

            console.log(particleCloud);
            console.log(this.Pool.__pools);


            renderer = new THREE.WebGLRenderer({antialias:true});
            //renderer = new THREE.CanvasRenderer();
            //renderer.setSize( window.innerWidth, window.innerHeight );
            renderer.setSize(container.offsetWidth, container.offsetHeight);
            container.appendChild( renderer.domElement );

            stats = new Stats();
            stats.domElement.style.position = 'absolute';
            stats.domElement.style.top = '0px';
            container.appendChild( stats.domElement );
            var self = this;


            var Europe = FastlyEmitter.init({
                            title: 'Fastly EU',
                            group: group,
                            metrics: fastlyMetrics.Europe,
                            shaderMaterial: this.shaderMaterial,
                            particles: particles,
                            pool: self.Pool,
                            pos: {
                                sx: 250,
                                sy: 300,
                                dx: 250,
                                dy: 50
                            }
                        });

            var USA = FastlyEmitter.init({
                            title: 'Fastly USA',
                            group: group,
                            metrics: fastlyMetrics.USA,
                            shaderMaterial: this.shaderMaterial,
                            particles: particles,
                            pool: self.Pool,
                            pos: {
                                sx: 250,
                                sy: -50,
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
                                shaderMaterial: this.shaderMaterial,
                                particles: particles,
                                pool: self.Pool,
                                pos: {
                                    sx: -50,
                                    sy: 300,
                                    dx: -50,
                                    dy: -300
                                }
                            })

            // App Servers
            var serverTopY = 250;
            _.each(serverMetrics, function(metrics, serverName) {
                if (serverName == 'Router') return;

                var hue;

                switch(serverName) {
                    case 'Applications': hue = 0.2; break;
                    case 'Article':      hue = 0.3; break;
                    case 'Front':        hue = 0.4; break;
                    case 'Discussion':   hue = 0;   break;
                    case 'Image':        hue = 0.1; break;
                    case 'Football':        hue = 0.8; break;
                }

                var server = ServerEmitter({
                                title:   serverName,
                                group:   serverGroup,
                                hue:     hue,
                                metrics: metrics,
                                shaderMaterial: self.shaderMaterial,
                                pool: self.Pool,
                                particles: particles,
                                pos: {
                                    sx: -350,
                                    sy: serverTopY,
                                    dx: -350,
                                    dy: serverTopY - 20
                                }
                            })

                serverTopY -= 100;
            });


            animate();

            function animate() {
                requestAnimationFrame(animate);
                render();
                stats.update();
            }


            function render() {
                var time = Date.now() * 0.005;

                for( var i = 0; i < self.shaderMaterial.attributes.size.value.length; i++ ) {
                    self.shaderMaterial.attributes.size.value[ i ] = 55 + 13 * Math.sin( 0.1 * i + time );
                }

                //particleCloud.rotation.y += 0.001;

                particleCloud.geometry.verticesNeedUpdate = true;
                particleCloud.geometry.colorsNeedUpdate = true;
                self.shaderMaterial.attributes.size.needsUpdate = true; //attributes.size.needsUpdate = true;
                self.shaderMaterial.attributes.customColor.needsUpdate = true;

                renderer.render( scene, camera );
            }

        }
    }

});